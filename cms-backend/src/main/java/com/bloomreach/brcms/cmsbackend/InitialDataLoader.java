package com.bloomreach.brcms.cmsbackend;

import com.bloomreach.brcms.client.ContentRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpStatusCodeException;

public class InitialDataLoader {

  private final ContentRepository contentRepository;

  InitialDataLoader(final @NonNull ContentRepository contentRepository) {
    this.contentRepository = contentRepository;
  }

  void load(final @NonNull List<NodeCreationRequest> requests) {

    if (CollectionUtils.isEmpty(requests)) {
      return;
    }

    try {
      this.contentRepository.traverseNode(requests.get(0).getForm().getName());
      return; // Data is already loaded
    } catch (final HttpStatusCodeException e) {
      // then continue
    }

    for (final NodeCreationRequest request : requests) {
      final ResponseEntity<String> response =
          this.contentRepository.createParentNode(request.getForm());

      final List<NodeCreationRequest> childRequests = request.getRequests();

      if (!CollectionUtils.isEmpty(childRequests)) {
        final String nodeId = response.getBody();
        this.createChildRequests(nodeId, childRequests);
      }
    }
  }

  private void createChildRequests(
      final String parentId, final List<NodeCreationRequest> requests) {

    for (final NodeCreationRequest request : requests) {
      final ResponseEntity<String> response =
          this.contentRepository.createChildNode(parentId, request.getForm());

      final List<NodeCreationRequest> childRequests = request.getRequests();

      if (!CollectionUtils.isEmpty(childRequests)) {
        final String nodeId = response.getBody();
        this.createChildRequests(nodeId, childRequests);
      }
    }
  }
}
