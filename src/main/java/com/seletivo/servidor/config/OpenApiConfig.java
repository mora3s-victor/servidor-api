package com.seletivo.servidor.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Servidores")
                        .description("API para gerenciamento de servidores efetivos e temporários")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@exemplo.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Por favor, insira o token JWT")));
    }

    @Bean
    public OpenApiCustomizer customizer() {
        return openApi -> {
            // Customizar o endpoint de upload de foto
            openApi.getPaths().forEach((path, item) -> {
                if (path.contains("/foto")) {
                    Operation post = item.getPost();
                    if (post != null) {
                        post.setRequestBody(new RequestBody()
                                .content(new Content()
                                        .addMediaType("multipart/form-data",
                                                new MediaType()
                                                        .schema(new Schema<>()
                                                                .type("object")
                                                                .addProperties("foto",
                                                                        new Schema<>()
                                                                                .type("string")
                                                                                .format("binary"))))));
                    }
                }
            });
        };
    }
} 
