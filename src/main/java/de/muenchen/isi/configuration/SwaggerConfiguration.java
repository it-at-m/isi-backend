package de.muenchen.isi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;


@Configuration
@Profile({"local", "dev"})
public class SwaggerConfiguration {

    private final String authServer;

    private final String realm;

    private final String buildVersion;

    @Autowired
    public SwaggerConfiguration(@Value("${keycloak.auth-server-url}") final String authServer,
                                @Value("${realm}") final String realm,
                                @Value("${info.application.version}") final String buildVersion) {
        this.authServer = authServer;
        this.realm = realm;
        this.buildVersion = buildVersion;
    }

    @Bean
    public OpenAPI openAPI() {
        final var authUrl = String.format("%s/realms/%s/protocol/openid-connect", this.authServer, this.realm);

        return new OpenAPI()
                .components(new Components())
                .security(Arrays.asList(
                        new SecurityRequirement().addList("spring_oauth")))
                .info(new Info()
                        .title("ISI Backend API")
                        .version(this.buildVersion)
                        .description("ISI Backend - Serivce für das Informationssystem für soziale Infrastrukturplanung")
                        .contact(new Contact()
                                .name("ISI Management")
                                .email("noreply@mail.de")));
    }

    @Bean
    @Profile("!prod")
    public String[] whitelist() {
        return new String[]{
                // -- swagger ui
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
        };
    }

    @Bean
    @Profile("prod")
    public String[] whitelistProd() {
        return new String[]{};
    }

}
