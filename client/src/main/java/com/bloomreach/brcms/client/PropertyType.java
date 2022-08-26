package com.bloomreach.brcms.client;

import java.time.Instant;
import java.util.ArrayList;

public enum PropertyType {
  TEXT(String.class),
  NUMBER(Number.class),
  ARRAY(ArrayList.class),
  BOOLEAN(Boolean.class),
  TIMESTAMP(Instant.class);

  private final Class<?> clazz;

  PropertyType(final Class<?> clazz) {
    this.clazz = clazz;
  }

  public Class<?> getClazz() {
    return this.clazz;
  }
}
