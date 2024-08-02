package com.br.eliza.spaceprobe.util.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Space probe - Java API")
                        .version("1.0")
                        .termsOfService("Terms of service URL")
                        .contact(new Contact()
                                .name("Eliza Pimentel")
                                .email("elizapimentel@hotmail.com"))
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("License of API")
                                .url("API license URL")));
    }

}
