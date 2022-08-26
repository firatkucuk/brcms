package com.bloomreach.brcms.jcr.node;

import com.bloomreach.brcms.client.PropertyType;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "NODE_PROPERTY")
@TypeDef(name = "property_type_enum", typeClass = PropertyTypeSqlEnumType.class)
public class NodeProperty {

  private String id;
  private String nodeId;
  private Node node;
  private String key;
  private PropertyType type;
  private String value;
  private Instant createdAt;

  @Id
  @Column(name = "ID", unique = true, length = 26, nullable = false)
  public String getId() {
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  @Column(name = "NODE_ID", length = 26)
  public String getNodeId() {
    return this.nodeId;
  }

  public void setNodeId(final String nodeId) {
    this.nodeId = nodeId;
  }

  @JoinColumn(name = "NODE_ID", insertable = false, updatable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  public Node getNode() {
    return this.node;
  }

  public void setNode(final Node node) {
    this.node = node;
  }

  @Column(name = "KEY", length = 255, nullable = false)
  public String getKey() {
    return this.key;
  }

  public void setKey(final String key) {
    this.key = key;
  }

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 50, nullable = false, columnDefinition = "PROPERTY_TYPE")
  @Type(type = "property_type_enum")
  public PropertyType getType() {
    return this.type;
  }

  public void setType(final PropertyType type) {
    this.type = type;
  }

  @Column(name = "VALUE", length = 2000)
  public String getValue() {
    return this.value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  @CreatedDate
  @Column(name = "CREATED_AT", nullable = false)
  public Instant getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(final Instant createdAt) {
    this.createdAt = createdAt;
  }
}
