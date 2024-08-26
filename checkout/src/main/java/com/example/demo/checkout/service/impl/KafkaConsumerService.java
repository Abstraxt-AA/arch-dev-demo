package com.example.demo.checkout.service.impl;

import com.example.demo.checkout.pojo.MaxwellKey;
import com.example.demo.checkout.pojo.MaxwellValue;
import com.example.demo.checkout.pojo.StockAndStatus;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

@Service
public class KafkaConsumerService implements ApplicationListener<ApplicationReadyEvent>, DisposableBean {

    private static final String[] COUNTRY_CODE_LIST = new String[]{"KW"};

    private final ReactiveKafkaConsumerTemplate<MaxwellKey, MaxwellValue> template;
    private final ReactiveRedisOperations<String, StockAndStatus> redisOps;
    private Disposable disposable;

    public KafkaConsumerService(final ReactiveKafkaConsumerTemplate<MaxwellKey, MaxwellValue> template,
                                final ReactiveRedisOperations<String, StockAndStatus> redisOps) {
        this.template = template;
        this.redisOps = redisOps;
    }

    @Override
    public void destroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) {
            this.disposable.dispose();
        }
    }

    @Override
    public void onApplicationEvent(@NonNull final ApplicationReadyEvent event) {
        this.disposable = template.receive()
                .flatMap(kEvent -> {
                    final var databaseAndTable = kEvent.key().database() + "." + kEvent.value().table();
                    final var productId = kEvent.key().primaryKey().get("pk.product_id");
                    if (productId == null) {
                        return Flux.empty();
                    }
                    switch (databaseAndTable.toLowerCase()) {
                        case "catalog.product":
                            return Flux.fromStream(Arrays.stream(COUNTRY_CODE_LIST)
                                            .map(val -> StockAndStatus.CHECKOUT_REDIS_KEY + ":" + productId + ":" +
                                                    val))
                                    .flatMap(key -> redisOps.opsForValue()
                                            .get(key)
                                            .switchIfEmpty(Mono.just(new StockAndStatus(false, 0L)))
                                            .flatMap(val -> redisOps.opsForValue()
                                                    .set(key, val.withStatus(Objects.equals("1",
                                                            kEvent.value().data().get("is_enabled"))))));
                        case "stock.ticker":
                            final var countryCode = kEvent.key().primaryKey().get("pk.country_code");
                            if (countryCode == null) {
                                return Flux.empty();
                            }
                            final var key = StockAndStatus.CHECKOUT_REDIS_KEY + ":" + productId + ":" +
                                    countryCode.toUpperCase();
                            return redisOps.opsForValue()
                                    .get(key)
                                    .switchIfEmpty(Mono.just(new StockAndStatus(false, 0L)))
                                    .flatMap(val -> redisOps.opsForValue()
                                            .set(key, val.withStock(Long
                                                    .parseLong(kEvent.value().data().get("available_stock")))));
                        default:
                            return Flux.empty();
                    }
                })
                .subscribe();
    }
}
