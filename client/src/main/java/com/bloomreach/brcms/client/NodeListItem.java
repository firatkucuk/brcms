package com.bloomreach.brcms.client;

import java.time.Instant;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public record NodeListItem(String id, String parentId, String name, Instant createdAt) {

  public NodeListItem(
      final @NonNull String id,
      final @Nullable String parentId,
      final @NonNull String name,
      final @NonNull Instant createdAt) {

    this.id = id;
    this.parentId = parentId;
    this.name = name;
    this.createdAt = createdAt;
  }
}
