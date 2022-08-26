package com.bloomreach.brcms.client;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NodePropertyForm {

  @NotNull
  @NotEmpty
  @Size(min = 1, max = 255)
  private String key;

  @NotNull private PropertyType type;

  private Object value;

  public String getKey() {
    return this.key;
  }

  public void setKey(final String key) {
    this.key = key;
  }

  public PropertyType getType() {
    return this.type;
  }

  public void setType(final PropertyType type) {
    this.type = type;
  }

  public Object getValue() {
    return this.value;
  }

  public void setValue(final Object value) {
    this.value = value;
  }
}
