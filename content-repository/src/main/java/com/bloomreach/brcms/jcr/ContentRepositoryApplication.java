package com.bloomreach.brcms.jcr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.Nullable;

@SpringBootApplication
public class ContentRepositoryApplication {

  public static void main(final @Nullable String[] args) {
    SpringApplication.run(ContentRepositoryApplication.class, args);
  }
}
