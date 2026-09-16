package de.muenchen.isi.configuration;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.StreamReadFeature;

@Configuration
public class JacksonConfiguration {

    @Bean
    public JsonMapperBuilderCustomizer strictDuplicateDetectionCustomizer() {
        // Nutzt den Builder deklarativ, da der fertige Mapper unveränderlich ist
        return builder -> builder.enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION);
    }
}
