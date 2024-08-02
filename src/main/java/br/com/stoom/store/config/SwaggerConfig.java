package br.com.stoom.store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${info.api.title}")
    private String title;

    @Value("${info.api.description}")
    private String description;

    @Value("${info.api.version}")
    private String version;

    @Value("${info.api.contact.name}")
    private String name;

    @Value("${info.api.contact.url}")
    private String url;


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .contact(new Contact().name(name).url(url))
                );
    }

    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            ApiResponses apiResponses = operation.getResponses();
            apiResponses.addApiResponse("500", createApiResponse("Internal Server Error"));
            apiResponses.addApiResponse("403", createApiResponse("Forbidden"));
            apiResponses.addApiResponse("401", createApiResponse("Unauthorized"));
            apiResponses.addApiResponse("400", createApiResponse("Bad Request"));
            apiResponses.addApiResponse("200", createApiResponse("Ok"));
        }));
    }

    private ApiResponse createApiResponse(String description) {
        return new ApiResponse().description(description);
    }

}
