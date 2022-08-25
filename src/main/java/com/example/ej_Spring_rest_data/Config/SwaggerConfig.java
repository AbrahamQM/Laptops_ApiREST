package com.example.ej_Spring_rest_data.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import java.util.Collections;

/**
 * Configuración Swagger para que se genere documentación de la API REST
 *
 * En la siguiente url podremos acceder a la documentación de nuestro proyecto
 * <a href="http://localhost:8080/swagger-ui/">Link a swagger </a>
 */

@Configuration
public class SwaggerConfig {

    //Bean para el objeto Docket
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiDetails())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    //Este método crea los detalles de la ApiInfo "La información añadida es toda inventada para este ejemplo"
    private ApiInfo apiDetails(){
        return new ApiInfo("Spring Boot book API REST",
                "Library Api rest docs",
                "1.0",
                "http://www.google.es",
                new Contact("Abraham", "http://www.google.es", "abraham@example.com"),
                "MIT",
                "http://www.google.es",
                Collections.emptyList());
    }
}
