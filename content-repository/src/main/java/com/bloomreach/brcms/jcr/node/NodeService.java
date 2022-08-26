package com.bloomreach.brcms.jcr.node;

import com.bloomreach.brcms.client.NodeForm;
import com.bloomreach.brcms.client.NodeInfo;
import com.bloomreach.brcms.client.NodeListItem;
import com.bloomreach.brcms.client.NodePropertyForm;
import com.bloomreach.brcms.client.PropertyType;
import com.bloomreach.brcms.client.TraversedNodeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
class NodeService {

  private static final Logger LOG = LoggerFactory.getLogger(NodeService.class);

  private final NodeRepository nodeRepository;
  private final NodePropertyRepository nodePropertyRepository;
  private final ObjectMapper objectMapper;

  NodeService(
      final @NonNull NodeRepository nodeRepository,
      final @NonNull NodePropertyRepository nodePropertyRepository,
      final @NonNull ObjectMapper objectMapper) {

    this.nodeRepository = nodeRepository;
    this.nodePropertyRepository = nodePropertyRepository;
    this.objectMapper = objectMapper;
  }

  public @NonNull String createNode(final @NonNull NodeForm form, final @Nullable Ulid parentUlid) {

    final String formNodeName = form.getName();
    final String parentId = parentUlid == null ? null : parentUlid.toString();

    if (parentId != null && !this.nodeRepository.existsById(parentId)) {
      LOG.debug("Parent node {} not found", parentId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent node not found");
    }

    if (this.nodeRepository.existsByNameAndParentId(formNodeName, parentId)) {
      LOG.debug("node name {} is in use", formNodeName);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Node name already exists");
    }

    final String nodeId = UlidCreator.getUlid().toString();

    final Node node = new Node();
    node.setId(nodeId);
    node.setName(formNodeName);
    node.setParentId(parentId);
    node.setCreatedAt(Instant.now());

    this.nodeRepository.save(node);
    this.persistProperties(nodeId, form.getProperties());

    LOG.info("node {} created.", formNodeName);

    return nodeId;
  }

  public void deleteNode(final @NonNull Ulid nodeId) {

    final Node node = this.findNode(nodeId);
    this.nodeRepository.delete(node);

    LOG.info("node {} deleted.", nodeId);
  }

  public @NonNull NodeInfo getNode(final @NonNull Ulid nodeId) {

    final Node node = this.findNode(nodeId);

    LOG.info("node {} fetched.", nodeId);

    return this.convertToNodeInfo(node, new NodeInfo());
  }

  public @NonNull Page<NodeListItem> listNodes(final @NonNull Pageable pageable) {

    final Page<NodeListItem> nodes =
        this.nodeRepository
            .findAll(pageable)
            .map(n -> new NodeListItem(n.getId(), n.getParentId(), n.getName(), n.getCreatedAt()));

    LOG.info("node list with {} elements fetched.", nodes.getTotalElements());

    return nodes;
  }

  public @NonNull TraversedNodeInfo traverseNode(final @NonNull String path) {

    String normalizedPath = path;

    if (normalizedPath.startsWith("/")) {
      normalizedPath = normalizedPath.substring(1);
    }

    if (normalizedPath.endsWith("/")) {
      normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
    }

    final String[] segments = normalizedPath.split("/");

    String parentId = null;
    Node parent = null;

    for (final String nodeName : segments) {
      final Optional<Node> parentNodeOptional =
          this.nodeRepository.findByNameAndParentId(nodeName, parentId);

      if (parentNodeOptional.isEmpty()) {
        LOG.debug("Node {} not found", nodeName);
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Node " + segments[0] + " does not found");
      }

      parent = parentNodeOptional.get();
      parentId = parent.getId();
    }

    final TraversedNodeInfo nodeInfo =
        this.convertToNodeInfo(Objects.requireNonNull(parent), new TraversedNodeInfo());

    final List<NodeInfo> children =
        this.nodeRepository.findByParentIdOrderByName(Objects.requireNonNull(parentId)).stream()
            .map(cn -> this.convertToNodeInfo(cn, new NodeInfo()))
            .toList();

    nodeInfo.setChildren(children);

    LOG.info("node {} traversed.", parentId);

    return nodeInfo;
  }

  public void updateNode(final @NonNull NodeForm form, final @NonNull Ulid nodeId) {

    final Node node = this.findNode(nodeId);
    this.nodePropertyRepository.deleteAllByNodeId(nodeId.toString());

    final String currentNodeName = node.getName();
    final String parentId = node.getParentId();
    final String formNodeName = form.getName();

    if (!currentNodeName.equals(formNodeName)
        && this.nodeRepository.existsByNameAndParentId(formNodeName, parentId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Node name already exists");
    }

    node.setName(formNodeName);
    this.nodeRepository.save(node);

    this.persistProperties(nodeId.toString(), form.getProperties());

    LOG.info("node {} updated.", nodeId);
  }

  private @NonNull <T extends NodeInfo> T convertToNodeInfo(
      final @NonNull Node node, final @NonNull T nodeInfo) {

    final Node parentNode = node.getParent();

    nodeInfo.setId(node.getId());
    nodeInfo.setName(node.getName());
    nodeInfo.setParentId(node.getParentId());
    nodeInfo.setParentName(parentNode == null ? null : parentNode.getName());
    nodeInfo.setCreatedAt(node.getCreatedAt());

    final HashMap<String, Object> properties = new HashMap<>();
    node.getProperties()
        .forEach(p -> properties.put(p.getKey(), this.deserializeValue(p.getValue(), p.getType())));

    nodeInfo.setProperties(properties);

    return nodeInfo;
  }

  private @Nullable Object deserializeValue(
      final @Nullable String value, final @NonNull PropertyType type) {

    if (value == null) {
      return null;
    }

    try {
      return this.objectMapper.readValue(value, type.getClazz());
    } catch (final JsonProcessingException e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Form value is in incorrect format");
    }
  }

  private @NonNull Node findNode(final @NonNull Ulid nodeId) {

    final Optional<Node> nodeOptional = this.nodeRepository.findById(nodeId.toString());

    if (nodeOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found");
    }

    return nodeOptional.get();
  }

  private void persistProperties(final String nodeId, final List<NodePropertyForm> formProperties) {

    Optional.ofNullable(formProperties)
        .orElse(List.of())
        .forEach(
            f -> {
              this.persistProperty(nodeId, f);
            });
  }

  private void persistProperty(
      final @NonNull String nodeId, final @NonNull NodePropertyForm propertyForm) {

    final NodeProperty nodeProperty = new NodeProperty();
    nodeProperty.setId(UlidCreator.getUlid().toString());
    nodeProperty.setNodeId(nodeId);
    nodeProperty.setKey(propertyForm.getKey());
    nodeProperty.setType(propertyForm.getType());
    nodeProperty.setValue(this.serializeValue(propertyForm.getValue()));
    nodeProperty.setCreatedAt(Instant.now());
    this.nodePropertyRepository.save(nodeProperty);
  }

  private @NonNull String serializeValue(final @Nullable Object value) {

    try {
      return this.objectMapper.writeValueAsString(value);
    } catch (final JsonProcessingException e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Form value is in incorrect format");
    }
  }
}
