package com.PDP.heatdistributionproblembe.Config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("App Title")
                                .description("App description")
                                .version("App version"))
            .externalDocs(
                new ExternalDocumentation()
                        .description("Documentation name")
                        .url("https://example.com"));
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder()
                .group("Category API")
                .pathsToMatch("/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi expenseApi() {
        return GroupedOpenApi.builder()
                .group("Expense API")
                .pathsToMatch("/expenses/**")
                .build();
    }
}