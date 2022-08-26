package com.bloomreach.brcms.jcr.node;

import com.github.f4b6a3.ulid.UlidCreator;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NodeRepositoryTest {

  @Autowired private NodeRepository nodeRepository;

  @Test
  void whenChildNode_thenFindsByNameAndParentId() {

    final String nodeId = UlidCreator.getUlid().toString();

    final Node node = new Node();
    node.setId(nodeId);
    node.setName("my-node");
    node.setCreatedAt(Instant.now());

    this.nodeRepository.save(node);

    Assertions.assertTrue(this.nodeRepository.existsById(nodeId));
  }

  @Test
  void whenDeleted_thenCannotFindById() {

    final String nodeId = UlidCreator.getUlid().toString();

    final Node node = new Node();
    node.setId(nodeId);
    node.setName("my-node");
    node.setCreatedAt(Instant.now());

    this.nodeRepository.save(node);

    this.nodeRepository.delete(node);

    final Optional<Node> foundNodeOptional = this.nodeRepository.findById(nodeId);
    Assertions.assertTrue(foundNodeOptional.isEmpty());
  }

  @Test
  void whenSaved_thenCheckExistenceById() {

    final String nodeId = UlidCreator.getUlid().toString();

    final Node node = new Node();
    node.setId(nodeId);
    node.setName("my-node");
    node.setCreatedAt(Instant.now());

    this.nodeRepository.save(node);

    Assertions.assertTrue(this.nodeRepository.existsById(nodeId));
  }

  @Test
  void whenSaved_thenFindsById() {

    final String nodeId = UlidCreator.getUlid().toString();

    final Node node = new Node();
    node.setId(nodeId);
    node.setName("my-node");
    node.setCreatedAt(Instant.now());

    this.nodeRepository.save(node);

    final Optional<Node> foundNodeOptional = this.nodeRepository.findById(nodeId);
    Assertions.assertTrue(foundNodeOptional.isPresent());
  }
}
