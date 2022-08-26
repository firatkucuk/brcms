package com.bloomreach.brcms.jcr;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerUiWebMvcConfigurer implements WebMvcConfigurer {

  private final String baseUrl;

  public SwaggerUiWebMvcConfigurer(
      @Value("${springfox.documentation.swagger-ui.base-url:}") final @Nullable String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  public void addCorsMappings(final @NonNull CorsRegistry registry) {

    registry.addMapping("/api/pet").allowedOrigins("http://editor.swagger.io");
    registry.addMapping("/v2/api-docs.*").allowedOrigins("http://editor.swagger.io");
  }

  @Override
  public void addResourceHandlers(final @NonNull ResourceHandlerRegistry registry) {

    final String baseUrl =
        StringUtils.trimTrailingCharacter(Objects.requireNonNull(this.baseUrl), '/');

    registry
        .addResourceHandler(baseUrl + "/swagger-ui/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
        .resourceChain(false);
  }

  @Override
  public void addViewControllers(final @NonNull ViewControllerRegistry registry) {

    registry
        .addViewController(this.baseUrl + "/swagger-ui/")
        .setViewName("forward:" + this.baseUrl + "/swagger-ui/index.html");
  }

  @Bean
  public @NonNull Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.ant("/nodes/**"))
        .build();
  }
}
