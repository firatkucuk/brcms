package com.bloomreach.brcms.client;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NodeForm {

  @NotNull
  @NotEmpty
  @Size(min = 1, max = 255)
  private String name;

  @Valid private List<NodePropertyForm> properties;

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public List<NodePropertyForm> getProperties() {
    return this.properties;
  }

  public void setProperties(final List<NodePropertyForm> properties) {
    this.properties = properties;
  }
}
