package com.bloomreach.brcms.jcr.node;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
interface NodeRepository extends JpaRepository<Node, String> {

  boolean existsByNameAndParentId(@NonNull String nodeName, @Nullable String parentId);

  @NonNull
  Optional<Node> findByNameAndParentId(@NonNull String nodeName, @Nullable String parentId);

  @NonNull
  List<Node> findByParentIdOrderByName(@NonNull String parentId);
}
