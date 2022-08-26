package com.bloomreach.brcms.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

public class ContentRepository {

  private static final HttpHeaders HEADERS;

  static {
    HEADERS = new HttpHeaders();
    HEADERS.setContentType(MediaType.APPLICATION_JSON);
  }

  private final RestTemplate restTemplate;

  public ContentRepository(final @NonNull RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public ResponseEntity<String> createChildNode(final String parentId, final NodeForm form) {

    return this.restTemplate.exchange(
        "/nodes/{nodeId}",
        HttpMethod.POST,
        new HttpEntity<>(form, HEADERS),
        String.class,
        parentId);
  }

  public ResponseEntity<String> createParentNode(final NodeForm form) {

    return this.restTemplate.exchange(
        "/nodes", HttpMethod.POST, new HttpEntity<>(form, HEADERS), String.class);
  }

  public ResponseEntity<Void> deleteNode(final String nodeId) {

    return this.restTemplate.exchange(
        "/nodes/{nodeId}", HttpMethod.DELETE, new HttpEntity<>(HEADERS), Void.class, nodeId);
  }

  public ResponseEntity<NodeInfo> getNode(final String nodeId) {

    return this.restTemplate.exchange(
        "/nodes/{nodeId}", HttpMethod.GET, new HttpEntity<>(HEADERS), NodeInfo.class, nodeId);
  }

  public ResponseEntity<RestResponsePage<NodeListItem>> listAllNodes() {

    return this.restTemplate.exchange(
        "/nodes", HttpMethod.GET, new HttpEntity<>(HEADERS), new ParameterizedTypeReference<>() {});
  }

  public ResponseEntity<TraversedNodeInfo> traverseNode(final String path) {

    return this.traverseNode(path, new ParameterizedTypeReference<>() {});
  }

  public <T> ResponseEntity<T> traverseNode(
      final String path, final ParameterizedTypeReference<T> responseType) {

    return this.restTemplate.exchange(
        "/nodes?by-path={path}&sort=name&name.dir=asc",
        HttpMethod.GET,
        new HttpEntity<>(HEADERS),
        responseType,
        path);
  }

  public ResponseEntity<Void> updateNode(final String nodeId, final NodeForm form) {

    return this.restTemplate.exchange(
        "/nodes/{nodeId}", HttpMethod.PUT, new HttpEntity<>(form, HEADERS), Void.class, nodeId);
  }
}
