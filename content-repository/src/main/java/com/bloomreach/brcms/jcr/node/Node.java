package com.bloomreach.brcms.jcr.node;

import java.time.Instant;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "NODE")
public class Node {

  private String id;
  private String name;
  private String parentId;
  private Node parent;
  private Instant createdAt;
  private List<NodeProperty> properties;

  @Id
  @Column(name = "ID", unique = true, length = 26, nullable = false)
  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  @Column(name = "NAME", nullable = false)
  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Column(name = "PARENT_ID", length = 26)
  public String getParentId() {
    return this.parentId;
  }

  public void setParentId(final String parentId) {
    this.parentId = parentId;
  }

  @JoinColumn(name = "PARENT_ID", insertable = false, updatable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  public Node getParent() {
    return this.parent;
  }

  public void setParent(final Node parent) {
    this.parent = parent;
  }

  @CreatedDate
  @Column(name = "CREATED_AT", nullable = false)
  public Instant getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(final Instant createdAt) {
    this.createdAt = createdAt;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "node")
  public List<NodeProperty> getProperties() {
    return this.properties;
  }

  public void setProperties(final List<NodeProperty> properties) {
    this.properties = properties;
  }
}
