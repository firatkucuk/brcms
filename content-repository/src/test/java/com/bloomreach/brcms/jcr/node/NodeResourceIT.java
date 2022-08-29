package com.bloomreach.brcms.jcr.node;

import com.bloomreach.brcms.client.ContentRepository;
import com.bloomreach.brcms.client.NodeForm;
import com.bloomreach.brcms.client.NodeInfo;
import com.bloomreach.brcms.client.NodeListItem;
import com.bloomreach.brcms.client.NodePropertyForm;
import com.bloomreach.brcms.client.PropertyType;
import com.bloomreach.brcms.client.RestResponsePage;
import com.bloomreach.brcms.client.TraversedNodeInfo;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NodeResourceIT {

  @Autowired private TestRestTemplate testRestTemplate;
  private ContentRepository contentRepository;

  @BeforeEach
  void beforeEach() {
    this.contentRepository = new ContentRepository(this.testRestTemplate.getRestTemplate());
  }

  @Test
  void createChildNode_notExistsNode_notFound() {

    final NodeForm nodeForm = new NodeForm();
    nodeForm.setName("my-node");

    final ResponseEntity<String> response =
        this.contentRepository.createChildNode(UlidCreator.getUlid().toString(), nodeForm);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void createChildNode_sameNameDifferentParent_succeeded() {

    // Create parent node

    final NodeForm parentNodeForm = new NodeForm();
    parentNodeForm.setName("same-node-name");

    ResponseEntity<String> response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String parentNodeId = response.getBody();
    Assertions.assertNotNull(parentNodeId);

    // Create child node

    final NodeForm childNodeForm = new NodeForm();
    childNodeForm.setName("same-node-name");

    response = this.contentRepository.createChildNode(parentNodeId, childNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String childNodeId = response.getBody();
    Assertions.assertNotNull(childNodeId);

    // Delete parent node and child node cascaded

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(parentNodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void createChildNode_sameName_failed() {

    // Create parent node

    final NodeForm parentNodeForm = new NodeForm();
    parentNodeForm.setName("parent-node");

    ResponseEntity<String> response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String parentNodeId = response.getBody();
    Assertions.assertNotNull(parentNodeId);

    // Create child node

    final NodeForm childNodeForm = new NodeForm();
    childNodeForm.setName("child-node");

    response = this.contentRepository.createChildNode(parentNodeId, childNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    // Create child node with same name

    response = this.contentRepository.createChildNode(parentNodeId, childNodeForm);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    final String childNodeId = response.getBody();
    Assertions.assertNotNull(childNodeId);

    // Delete parent node and child node cascaded

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(parentNodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void createChildNode_validInput_succeeded() {

    // Create parent node

    final NodeForm parentNodeForm = new NodeForm();
    parentNodeForm.setName("parent-node");

    ResponseEntity<String> response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String parentNodeId = response.getBody();
    Assertions.assertNotNull(parentNodeId);

    // Create child node

    final NodePropertyForm propertyForm1 = new NodePropertyForm();
    propertyForm1.setKey("myBoolean");
    propertyForm1.setType(PropertyType.BOOLEAN);
    propertyForm1.setValue(true);

    final NodePropertyForm propertyForm2 = new NodePropertyForm();
    propertyForm2.setKey("myNumber");
    propertyForm2.setType(PropertyType.NUMBER);
    propertyForm2.setValue(123);

    final NodeForm childNodeForm = new NodeForm();
    childNodeForm.setName("child-node");
    childNodeForm.setProperties(List.of(propertyForm1, propertyForm2));

    response = this.contentRepository.createChildNode(parentNodeId, childNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String childNodeId = response.getBody();
    Assertions.assertNotNull(childNodeId);

    // Delete parent node and child node cascaded

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(parentNodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void createParentNode_sameName_failed() {

    // Create parent node

    final NodeForm parentNodeForm = new NodeForm();
    parentNodeForm.setName("my-node");

    ResponseEntity<String> response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String parentNodeId = response.getBody();
    Assertions.assertNotNull(parentNodeId);

    // Create parent node with same name

    response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    // Delete parent node and child node cascaded

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(parentNodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void createParentNode_validInput_succeeded() {

    // Create parent node

    final NodePropertyForm propertyForm1 = new NodePropertyForm();
    propertyForm1.setKey("myBoolean");
    propertyForm1.setType(PropertyType.BOOLEAN);
    propertyForm1.setValue(true);

    final NodePropertyForm propertyForm2 = new NodePropertyForm();
    propertyForm2.setKey("myNumber");
    propertyForm2.setType(PropertyType.NUMBER);
    propertyForm2.setValue(123);

    final NodeForm parentNodeForm = new NodeForm();
    parentNodeForm.setName("parent-node");
    parentNodeForm.setProperties(List.of(propertyForm1, propertyForm2));

    final ResponseEntity<String> response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String parentNodeId = response.getBody();
    Assertions.assertNotNull(parentNodeId);

    // Delete parent node

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(parentNodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void deleteNode_notExistsNode_notFound() {

    final ResponseEntity<Void> response =
        this.contentRepository.deleteNode(UlidCreator.getUlid().toString());
    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void deleteNode_succeeded() {

    // Create parent node

    final NodeForm parentNodeForm = new NodeForm();
    parentNodeForm.setName("parent-node");

    final ResponseEntity<String> response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String parentNodeId = response.getBody();
    Assertions.assertNotNull(parentNodeId);

    // Delete parent node

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(parentNodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void getNode_notExistsNode_notFound() {

    final ResponseEntity<NodeInfo> response =
        this.contentRepository.getNode(UlidCreator.getUlid().toString());
    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void getNode_succeeded() {

    // Create parent node

    final NodeForm parentNodeForm = new NodeForm();
    parentNodeForm.setName("parent-node");

    ResponseEntity<String> response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String parentNodeId = response.getBody();
    Assertions.assertNotNull(parentNodeId);

    // Create child node

    final NodePropertyForm propertyForm1 = new NodePropertyForm();
    propertyForm1.setKey("myBoolean");
    propertyForm1.setType(PropertyType.BOOLEAN);
    propertyForm1.setValue(true);

    final NodePropertyForm propertyForm2 = new NodePropertyForm();
    propertyForm2.setKey("myNumber");
    propertyForm2.setType(PropertyType.NUMBER);
    propertyForm2.setValue(123);

    final NodePropertyForm propertyForm3 = new NodePropertyForm();
    propertyForm3.setKey("myText");
    propertyForm3.setType(PropertyType.TEXT);
    propertyForm3.setValue("Hello");

    final NodePropertyForm propertyForm4 = new NodePropertyForm();
    propertyForm4.setKey("myArray");
    propertyForm4.setType(PropertyType.ARRAY);
    propertyForm4.setValue(List.of("Apple", "Banana"));

    final NodePropertyForm propertyForm5 = new NodePropertyForm();
    propertyForm5.setKey("myTimestamp");
    propertyForm5.setType(PropertyType.TIMESTAMP);
    propertyForm5.setValue(Instant.now());

    final NodePropertyForm propertyForm6 = new NodePropertyForm();
    propertyForm6.setKey("myNull");
    propertyForm6.setType(PropertyType.TEXT);
    propertyForm6.setValue(null);

    final NodePropertyForm propertyForm7 = new NodePropertyForm();
    propertyForm7.setKey("myNodeLink");
    propertyForm7.setType(PropertyType.NODE_LINK);
    propertyForm7.setValue("/other-node");

    final NodeForm childNodeForm = new NodeForm();
    childNodeForm.setName("child-node");
    childNodeForm.setProperties(
        List.of(
            propertyForm1,
            propertyForm2,
            propertyForm3,
            propertyForm4,
            propertyForm5,
            propertyForm6,
            propertyForm7));

    response = this.contentRepository.createChildNode(parentNodeId, childNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String childNodeId = response.getBody();
    Assertions.assertNotNull(childNodeId);

    // Get child node

    final ResponseEntity<NodeInfo> getNodeResponse = this.contentRepository.getNode(childNodeId);
    Assertions.assertEquals(HttpStatus.OK, getNodeResponse.getStatusCode());

    final NodeInfo childNodeInfo = getNodeResponse.getBody();
    Assertions.assertNotNull(childNodeInfo);
    Assertions.assertEquals(childNodeId, childNodeInfo.getId());
    Assertions.assertNotNull(childNodeInfo.getCreatedAt());
    Assertions.assertEquals(childNodeForm.getName(), childNodeInfo.getName());
    Assertions.assertEquals(parentNodeId, childNodeInfo.getParentId());
    Assertions.assertEquals(parentNodeForm.getName(), childNodeInfo.getParentName());

    final Map<String, Object> childNodeProperties = childNodeInfo.getProperties();

    Assertions.assertNotNull(childNodeProperties);
    Assertions.assertEquals(7, childNodeProperties.size());
    Assertions.assertEquals(true, childNodeProperties.get("myBoolean"));
    Assertions.assertEquals(123, childNodeProperties.get("myNumber"));
    Assertions.assertEquals("Hello", childNodeProperties.get("myText"));
    Assertions.assertEquals(List.of("Apple", "Banana"), childNodeProperties.get("myArray"));
    Assertions.assertEquals(
        propertyForm5.getValue().toString(), childNodeProperties.get("myTimestamp").toString());
    Assertions.assertNull(childNodeProperties.get("myNull"));

    // Delete parent node

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(parentNodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void listNodes_succeeded() {

    // Create parent node 1

    final NodeForm node1Form = new NodeForm();
    node1Form.setName("node1");

    ResponseEntity<String> response = this.contentRepository.createParentNode(node1Form);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String node1Id = response.getBody();
    Assertions.assertTrue(Ulid.isValid(node1Id));

    // Create parent node 2

    final NodeForm node2Form = new NodeForm();
    node2Form.setName("node2");

    response = this.contentRepository.createParentNode(node2Form);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String node2Id = response.getBody();
    Assertions.assertTrue(Ulid.isValid(node2Id));

    // Create parent node 3

    final NodeForm node3Form = new NodeForm();
    node3Form.setName("node3");

    response = this.contentRepository.createParentNode(node3Form);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String node3Id = response.getBody();
    Assertions.assertTrue(Ulid.isValid(node3Id));

    // List all nodes

    ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(3, page.getTotalElements());
    final List<NodeListItem> content = page.getContent();
    Assertions.assertEquals("node3", content.get(0).name());
    Assertions.assertEquals("node2", content.get(1).name());
    Assertions.assertEquals("node1", content.get(2).name());

    // Delete parent node 1

    ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(node1Id);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // Delete parent node 2

    deleteResponse = this.contentRepository.deleteNode(node2Id);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // Delete parent node 3

    deleteResponse = this.contentRepository.deleteNode(node3Id);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    listResponse = this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void traverseNode_notFound() {

    final ResponseEntity<Void> listResponse =
        this.contentRepository.traverseNode("random-name", new ParameterizedTypeReference<>() {});
    Assertions.assertEquals(HttpStatus.NOT_FOUND, listResponse.getStatusCode());
  }

  @Test
  void traverseNode_succeeded() {

    // Create parent node

    final NodeForm parentNodeForm = new NodeForm();
    parentNodeForm.setName("parent");

    ResponseEntity<String> response = this.contentRepository.createParentNode(parentNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String parentNodeId = response.getBody();
    Assertions.assertTrue(Ulid.isValid(parentNodeId));

    // Create child node

    final NodeForm childNodeForm = new NodeForm();
    childNodeForm.setName("child");

    response = this.contentRepository.createChildNode(parentNodeId, childNodeForm);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String childNodeId = response.getBody();
    Assertions.assertTrue(Ulid.isValid(childNodeId));

    // Create a grand child node 1

    final NodeForm grandChildNode1Form = new NodeForm();
    grandChildNode1Form.setName("child1");

    response = this.contentRepository.createChildNode(childNodeId, grandChildNode1Form);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String childNode1Id = response.getBody();
    Assertions.assertTrue(Ulid.isValid(childNode1Id));

    // Create a grand child node 2

    final NodeForm grandChildNode2Form = new NodeForm();
    grandChildNode2Form.setName("child2");

    response = this.contentRepository.createChildNode(childNodeId, grandChildNode2Form);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String childNode2Id = response.getBody();
    Assertions.assertTrue(Ulid.isValid(childNode2Id));

    // Filter child nodes

    ResponseEntity<TraversedNodeInfo> traverseResponse =
        this.contentRepository.traverseNode("parent/child");
    Assertions.assertEquals(HttpStatus.OK, traverseResponse.getStatusCode());

    final TraversedNodeInfo nodeInfo = traverseResponse.getBody();
    Assertions.assertNotNull(nodeInfo);
    Assertions.assertEquals(nodeInfo.getId(), childNodeId);
    Assertions.assertEquals(nodeInfo.getName(), childNodeForm.getName());
    Assertions.assertEquals(nodeInfo.getParentId(), parentNodeId);
    Assertions.assertEquals(nodeInfo.getParentName(), parentNodeForm.getName());
    Assertions.assertNotNull(nodeInfo.getCreatedAt());

    final List<NodeInfo> children = nodeInfo.getChildren();
    Assertions.assertNotNull(children);
    Assertions.assertEquals(2, children.size());
    Assertions.assertEquals("child1", children.get(0).getName());
    Assertions.assertEquals("child2", children.get(1).getName());

    // Normalized responses

    traverseResponse = this.contentRepository.traverseNode("/parent/child");
    Assertions.assertEquals(HttpStatus.OK, traverseResponse.getStatusCode());

    traverseResponse = this.contentRepository.traverseNode("/parent/child/");
    Assertions.assertEquals(HttpStatus.OK, traverseResponse.getStatusCode());

    traverseResponse = this.contentRepository.traverseNode("parent/child/");
    Assertions.assertEquals(HttpStatus.OK, traverseResponse.getStatusCode());

    // Delete parent node

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(parentNodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void updateNode_notExistsNode_notFound() {

    final NodeForm nodeForm = new NodeForm();
    nodeForm.setName("my-node");

    final ResponseEntity<Void> response =
        this.contentRepository.updateNode(UlidCreator.getUlid().toString(), nodeForm);
    Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void updateNode_sameNameWithExistingOne_succeeded() {

    // Create parent node 1

    final NodeForm form1 = new NodeForm();
    form1.setName("same-name");

    ResponseEntity<String> response = this.contentRepository.createParentNode(form1);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String node1Id = response.getBody();
    Assertions.assertNotNull(node1Id);

    // Create parent node 2

    final NodeForm form2 = new NodeForm();
    form2.setName("other-name");

    response = this.contentRepository.createParentNode(form2);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String node2Id = response.getBody();
    Assertions.assertNotNull(node2Id);

    // Update the node

    final NodeForm nodeForm = new NodeForm();
    nodeForm.setName("same-name");

    final ResponseEntity<Void> updateResponse =
        this.contentRepository.updateNode(node2Id, nodeForm);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());

    // Delete parent node 1

    ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(node1Id);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // Delete parent node 2

    deleteResponse = this.contentRepository.deleteNode(node2Id);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }

  @Test
  void updateNode_validInput_succeeded() {

    // Create parent node

    final NodePropertyForm propertyForm1 = new NodePropertyForm();
    propertyForm1.setKey("myBoolean");
    propertyForm1.setType(PropertyType.BOOLEAN);
    propertyForm1.setValue(true);

    final NodePropertyForm propertyForm2 = new NodePropertyForm();
    propertyForm2.setKey("myNumber");
    propertyForm2.setType(PropertyType.NUMBER);
    propertyForm2.setValue(123);

    final NodeForm form = new NodeForm();
    form.setName("parent-node");
    form.setProperties(List.of(propertyForm1, propertyForm2));

    final ResponseEntity<String> response = this.contentRepository.createParentNode(form);
    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

    final String nodeId = response.getBody();
    Assertions.assertNotNull(nodeId);

    // Update the node

    final NodePropertyForm propertyForm1u = new NodePropertyForm();
    propertyForm1u.setKey("myBooleanUpdated");
    propertyForm1u.setType(PropertyType.BOOLEAN);
    propertyForm1u.setValue(false);

    final NodePropertyForm propertyForm2u = new NodePropertyForm();
    propertyForm2u.setKey("myNumberUpdated");
    propertyForm2u.setType(PropertyType.NUMBER);
    propertyForm2u.setValue(321);

    final NodeForm nodeForm = new NodeForm();
    nodeForm.setName("my-node");
    nodeForm.setProperties(List.of(propertyForm1u, propertyForm2u));

    final ResponseEntity<Void> updateResponse = this.contentRepository.updateNode(nodeId, nodeForm);
    Assertions.assertEquals(HttpStatus.OK, updateResponse.getStatusCode());

    // Get updated node

    final ResponseEntity<NodeInfo> getNodeResponse = this.contentRepository.getNode(nodeId);
    Assertions.assertEquals(HttpStatus.OK, getNodeResponse.getStatusCode());

    final NodeInfo childNodeInfo = getNodeResponse.getBody();
    Assertions.assertNotNull(childNodeInfo);
    Assertions.assertEquals(nodeId, childNodeInfo.getId());
    Assertions.assertNotNull(childNodeInfo.getCreatedAt());
    Assertions.assertEquals(nodeForm.getName(), childNodeInfo.getName());
    Assertions.assertNull(childNodeInfo.getParentId());
    Assertions.assertNull(childNodeInfo.getParentName());

    final Map<String, Object> childNodeProperties = childNodeInfo.getProperties();

    Assertions.assertNotNull(childNodeProperties);
    Assertions.assertEquals(2, childNodeProperties.size());
    Assertions.assertEquals(false, childNodeProperties.get("myBooleanUpdated"));
    Assertions.assertEquals(321, childNodeProperties.get("myNumberUpdated"));

    // Delete parent node

    final ResponseEntity<Void> deleteResponse = this.contentRepository.deleteNode(nodeId);
    Assertions.assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

    // List all nodes, should be 0

    final ResponseEntity<RestResponsePage<NodeListItem>> listResponse =
        this.contentRepository.listAllNodes();
    Assertions.assertEquals(HttpStatus.OK, listResponse.getStatusCode());

    final RestResponsePage<NodeListItem> page = listResponse.getBody();

    Assertions.assertNotNull(page);
    Assertions.assertEquals(0, page.getTotalElements());
  }
}
