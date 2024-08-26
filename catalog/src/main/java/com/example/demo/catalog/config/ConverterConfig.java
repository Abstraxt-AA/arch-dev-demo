package com.example.demo.catalog.config;

import com.google.protobuf.util.JsonFormat;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.protobuf.ProtobufJsonDecoder;
import org.springframework.http.codec.protobuf.ProtobufJsonEncoder;

@Configuration
public class ConverterConfig {

    @Bean
    public CodecCustomizer protobufJsonCodec() {
        return configurer -> {
            final var printer = JsonFormat.printer().preservingProtoFieldNames()
                    .alwaysPrintFieldsWithNoPresence().omittingInsignificantWhitespace();
            configurer.customCodecs().register(new ProtobufJsonEncoder(printer));
            configurer.customCodecs().register(new ProtobufJsonDecoder());
        };
    }
}
