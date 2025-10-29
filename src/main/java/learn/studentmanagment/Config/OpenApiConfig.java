package learn.studentmanagment.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * This configuration bean tells Swagger UI to add an "Authorize" button
     * and defines how to use Basic Authentication.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "basicAuth";

        return new OpenAPI()
                // 1. Add a global security requirement
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // 2. Define the security scheme in the "components"
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP) // Type is HTTP
                                .scheme("basic") // Scheme is "basic"
                                .description("Basic Authentication with username and password")
                        )
                );
    }
}