package com.bloomreach.brcms.client;

import java.time.Instant;
import java.util.Map;

public class NodeInfo {

  private String id;
  private String name;
  private String parentId;
  private String parentName;
  private Instant createdAt;
  private Map<String, Object> properties;

  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getParentId() {
    return this.parentId;
  }

  public void setParentId(final String parentId) {
    this.parentId = parentId;
  }

  public String getParentName() {
    return this.parentName;
  }

  public void setParentName(final String parentName) {
    this.parentName = parentName;
  }

  public Instant getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(final Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Map<String, Object> getProperties() {
    return this.properties;
  }

  public void setProperties(final Map<String, Object> properties) {
    this.properties = properties;
  }
}
