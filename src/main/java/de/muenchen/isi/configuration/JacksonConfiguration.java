package de.muenchen.isi.configuration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class JacksonConfiguration {

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void objectMapper() {
        this.objectMapper.enable(
                /**
                 * Beim deserialisieren werden Attributduplikationen erkannt.
                 * Bei einer vorhandenen Attributduplikation wird durch den Parser eine {@link JsonParseException} geworfen.
                 */
                JsonParser.Feature.STRICT_DUPLICATE_DETECTION
        );
    }

}
