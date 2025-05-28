package br.com.avila.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Webconfig implements WebMvcConfigurer {

    @Value("${cors.originPatterns:default}")
    private String corsOriginPattern = "";

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

          /* // Via QUERY PARAM localhost:8080/api/person/v1?mediaType=json
          configurer.favorParameter(true)
                  .parameterName("mediaType").ignoreAcceptHeader(true)
                  .useRegisteredExtensionsOnly(false)
                  .defaultContentType(MediaType.APPLICATION_JSON)
                  .mediaType("json", MediaType.APPLICATION_JSON)
                  .mediaType("xml", MediaType.APPLICATION_XML);
          */


        // via HEADER PARAM localhost:8080/api/person/v1
        configurer.favorParameter(false)
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("yaml", MediaType.APPLICATION_YAML);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var allowedOrigins = corsOriginPattern.split(",");
        registry.addMapping("/**").allowedOrigins(allowedOrigins)
                //.allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedMethods("*")
                .allowCredentials(true)
        ;

        ;
    }
}
