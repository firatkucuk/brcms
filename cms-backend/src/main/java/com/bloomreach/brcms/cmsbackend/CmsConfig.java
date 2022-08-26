package com.bloomreach.brcms.cmsbackend;

import com.bloomreach.brcms.client.ContentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CmsConfig {

  @Bean
  public @NonNull RestTemplate restTemplate(
      final @NonNull RestTemplateBuilder restTemplateBuilder,
      @Value("${content-repository.host}") final @NonNull String contentRepositoryHost) {

    return restTemplateBuilder.rootUri(contentRepositoryHost).build();
  }

  @Bean
  public @NonNull ContentRepository contentRepository(final @NonNull RestTemplate restTemplate) {
    return new ContentRepository(restTemplate);
  }

  @Bean
  @ConditionalOnProperty(prefix = "cms", name = "load-test-data", havingValue = "true")
  public @NonNull InitialDataLoader loadTestData(
      final @NonNull ContentRepository contentRepository,
      @Value("classpath:initial-data.json") final @NonNull Resource initialDataFile,
      final ObjectMapper objectMapper)
      throws IOException {

    final List<NodeCreationRequest> requests =
        objectMapper.readValue(initialDataFile.getInputStream(), new TypeReference<>() {});

    final InitialDataLoader initialDataLoader = new InitialDataLoader(contentRepository);
    initialDataLoader.load(requests);

    return initialDataLoader;
  }
}
