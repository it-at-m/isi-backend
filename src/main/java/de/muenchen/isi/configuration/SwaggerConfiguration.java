package de.muenchen.isi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class SwaggerConfiguration {

    private final String buildVersion;

    @Autowired
    public SwaggerConfiguration(@Value("${info.application.version}") final String buildVersion) {
        this.buildVersion = buildVersion;
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(
                new Info()
                    .title("ISI Backend API")
                    .version(this.buildVersion)
                    .description("ISI Backend - Service für das Informationssystem für soziale Infrastrukturplanung")
                    .contact(new Contact().name("ISI Management").email("noreply@mail.de"))
            );
    }

    @Bean
    public String[] whitelist() {
        return new String[] {
            // -- swagger ui
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
        };
    }
}
