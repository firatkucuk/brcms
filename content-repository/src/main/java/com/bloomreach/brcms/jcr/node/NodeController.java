package com.bloomreach.brcms.jcr.node;

import com.bloomreach.brcms.client.NodeForm;
import com.bloomreach.brcms.client.NodeInfo;
import com.bloomreach.brcms.client.NodeListItem;
import com.bloomreach.brcms.client.TraversedNodeInfo;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Contains REST API handler for node operations, mostly CRUD operations. */
@Validated
@RestController
public class NodeController {

  private static final Logger LOG = LoggerFactory.getLogger(NodeController.class);

  private final NodeService nodeService;

  public NodeController(final @NonNull NodeService nodeService) {
    this.nodeService = nodeService;
  }

  /**
   * Creates a child node
   *
   * @param form creation form that contains child node name and properties
   * @param parentNodeId nonnull parent id
   * @return id of created child node
   */
  @ApiOperation(value = "Creates a child node")
  @PostMapping(
      value = {"/nodes/{parentNodeId}", "/nodes/{parentNodeId}/"},
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @NonNull
  String createChildNode(
      @Validated @RequestBody final @NonNull NodeForm form,
      @PathVariable @Size(min = 1, max = 26) final @NonNull String parentNodeId) {

    LOG.debug("createChildNode controller method invoked");

    return this.nodeService
        .createNode(
            ValidationUtils.validateAndMapPropertyValues(form),
            ValidationUtils.validateUlid(parentNodeId))
        .toString();
  }

  /**
   * Creates a parent node
   *
   * @param form creation form that contains parent node name and properties
   * @return id of created parent node
   */
  @ApiOperation(value = "Creates a parent node")
  @PostMapping(
      value = {"/nodes", "/nodes/"},
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @NonNull
  String createParentNode(@Validated @RequestBody final @NonNull NodeForm form) {

    LOG.debug("createParentNode controller method invoked");

    return this.nodeService
        .createNode(ValidationUtils.validateAndMapPropertyValues(form), null)
        .toString();
  }

  /**
   * Deletes a node cascaded
   *
   * @param nodeId Requested node id for deletion
   */
  @ApiOperation(value = "Deletes a node")
  @DeleteMapping(
      value = {"/nodes/{nodeId}", "/nodes/{nodeId}/"},
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @NonNull
  void deleteNode(@PathVariable @Size(min = 1, max = 26) final @NonNull String nodeId) {

    LOG.debug("deleteNode controller method invoked");

    this.nodeService.deleteNode(ValidationUtils.validateUlid(nodeId));
  }

  /**
   * Fetches details of specified node
   *
   * @param nodeId Requested node id for fetch operation
   * @return Details of specified node
   */
  @ApiOperation(value = "Fetches a node")
  @GetMapping(
      value = {"/nodes/{nodeId}", "/nodes/{nodeId}/"},
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @NonNull
  NodeInfo getNode(@PathVariable @Size(min = 1, max = 26) final @NonNull String nodeId) {

    LOG.debug("getNode controller method invoked");

    return this.nodeService.getNode(ValidationUtils.validateUlid(nodeId));
  }

  /**
   * List all nodes in a paginated manner, page size and sort order can be adjusted via parameter
   *
   * @param pageable pagination parameters
   * @return Selected page of nodes
   */
  @ApiOperation(value = "List nodes")
  @GetMapping(
      value = {"/nodes", "/nodes/"},
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @NonNull
  Page<NodeListItem> listNodes(
      @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
          final @NonNull Pageable pageable) {

    LOG.debug("listNodes controller method invoked");

    return this.nodeService.listNodes(pageable);
  }

  /**
   * Fetches extended details of a specified path
   *
   * @param path unique path for fetching detailed info
   * @return extended info of specified path
   */
  @ApiOperation(value = "Traverses a node")
  @GetMapping(
      value = {"/nodes", "/nodes/"},
      consumes = MediaType.APPLICATION_JSON_VALUE,
      params = {"by-path"})
  @NonNull
  TraversedNodeInfo traverseNode(
      @RequestParam(name = "by-path") @Pattern(regexp = "/?([a-zA-Z\\d_-]+)(/[a-zA-Z\\d_-]+)*/?")
          final @NonNull String path) {

    LOG.debug("traverseNode controller method invoked");

    return this.nodeService.traverseNode(path);
  }

  /**
   * Updates a node using the form data
   *
   * @param form Update form, overrides the specified data
   * @param nodeId Requested node id for update operation
   */
  @ApiOperation(value = "Updates a node")
  @PutMapping(
      value = {"/nodes/{nodeId}", "/nodes/{nodeId}/"},
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @NonNull
  void updateNode(
      @Validated @RequestBody final @NonNull NodeForm form,
      @PathVariable @Size(min = 1, max = 26) final @NonNull String nodeId) {

    LOG.debug("updateNode controller method invoked");

    this.nodeService.updateNode(
        ValidationUtils.validateAndMapPropertyValues(form), ValidationUtils.validateUlid(nodeId));
  }
}
