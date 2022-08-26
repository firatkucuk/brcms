package com.bloomreach.brcms.jcr.node;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.f4b6a3.ulid.UlidCreator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NodeControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private NodeService nodeService;

  @Test
  void createChildNode_invalidParentIdLength_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes/{parentNodeId}", "123456789012345678901234567")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node"
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createChildNode_invalidParentUlidFormat_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes/{parentNodeId}", "xxxxxxxxxxxxxxxxxxxxxxxxxx")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node"
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createParentNode_invalidFormInput_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
            {
              "name": ""
            }
            """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createParentNode_invalidFormPropertyInput_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "",
                    "type": "BOOLEAN",
                    "value": true
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createParentNode_invalidFormPropertyTypeArray_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someArray",
                    "type": "ARRAY",
                    "value": 123
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createParentNode_invalidFormPropertyTypeBoolean_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someBoolean",
                    "type": "BOOLEAN",
                    "value": 12
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createParentNode_invalidFormPropertyTypeInput_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someKey",
                    "type": "UNKNOWN",
                    "value": ""
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createParentNode_invalidFormPropertyTypeNumber_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someNumber",
                    "type": "NUMBER",
                    "value": true
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createParentNode_invalidFormPropertyTypeTimestamp_badRequest() throws Exception {

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someTimestamp",
                    "type": "TIMESTAMP",
                    "value": true
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createParentNode_validFormPropertyTypeArray_success() throws Exception {

    Mockito.when(this.nodeService.createNode(Mockito.any(), Mockito.any()))
        .thenReturn(UlidCreator.getUlid());

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someArray",
                    "type": "ARRAY",
                    "value": [1, 2, 3]
                  }
                ]
              }
              """))
        .andExpect(status().isCreated());
  }

  @Test
  void createParentNode_validFormPropertyTypeBoolean_success() throws Exception {

    Mockito.when(this.nodeService.createNode(Mockito.any(), Mockito.any()))
        .thenReturn(UlidCreator.getUlid());

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someBoolean",
                    "type": "BOOLEAN",
                    "value": true
                  }
                ]
              }
              """))
        .andExpect(status().isCreated());
  }

  @Test
  void createParentNode_validFormPropertyTypeNumber_success() throws Exception {

    Mockito.when(this.nodeService.createNode(Mockito.any(), Mockito.any()))
        .thenReturn(UlidCreator.getUlid());

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someNumber",
                    "type": "NUMBER",
                    "value": 123
                  }
                ]
              }
              """))
        .andExpect(status().isCreated());
  }

  @Test
  void createParentNode_validFormPropertyTypeTimestamp_success() throws Exception {

    Mockito.when(this.nodeService.createNode(Mockito.any(), Mockito.any()))
        .thenReturn(UlidCreator.getUlid());

    this.mockMvc
        .perform(
            post("/nodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someTimestamp",
                    "type": "TIMESTAMP",
                    "value": "2022-07-10T20:58:46Z"
                  }
                ]
              }
              """))
        .andExpect(status().isCreated());
  }

  @Test
  void deleteNode_invalidNodeIdLength_badRequest() throws Exception {

    this.mockMvc
        .perform(
            delete("/nodes/{nodeId}", "123456789012345678901234567")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteNode_invalidUlidFormat_badRequest() throws Exception {

    this.mockMvc
        .perform(
            delete("/nodes/{nodeId}", "xxxxxxxxxxxxxxxxxxxxxxxxxx")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteNode_validNodeId_succeeded() throws Exception {

    this.mockMvc
        .perform(
            delete("/nodes/{nodeId}", UlidCreator.getUlid().toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void getNode_invalidNodeIdLength_badRequest() throws Exception {

    this.mockMvc
        .perform(
            get("/nodes/{nodeId}", "123456789012345678901234567")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getNode_invalidUlidFormat_badRequest() throws Exception {

    this.mockMvc
        .perform(
            get("/nodes/{nodeId}", "xxxxxxxxxxxxxxxxxxxxxxxxxx")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getNode_validNodeId_succeeded() throws Exception {

    this.mockMvc
        .perform(
            get("/nodes/{nodeId}", UlidCreator.getUlid().toString())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void listNodes() throws Exception {

    Mockito.when(this.nodeService.listNodes(Mockito.any())).thenReturn(Page.empty());

    this.mockMvc
        .perform(get("/nodes").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void traverseNode_invalidPath_badRequest() throws Exception {

    Mockito.when(this.nodeService.listNodes(Mockito.any())).thenReturn(Page.empty());

    this.mockMvc
        .perform(get("/nodes").param("by-path", "@!#").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void traverseNode_validPath_succeeded() throws Exception {

    Mockito.when(this.nodeService.listNodes(Mockito.any())).thenReturn(Page.empty());

    this.mockMvc
        .perform(
            get("/nodes")
                .param("by-path", "/parent-node/child-node")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void updateNode_invalidNameInput_badRequest() throws Exception {

    this.mockMvc
        .perform(
            put("/nodes/{nodeId}", UlidCreator.getUlid().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "",
                "properties": [
                  {
                    "key": "someTimestamp",
                    "type": "TIMESTAMP",
                    "value": "2022-07-10T20:58:46Z"
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateNode_invalidNodeIdLength_badRequest() throws Exception {

    this.mockMvc
        .perform(
            put("/nodes/{nodeId}", "123456789012345678901234567")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someTimestamp",
                    "type": "TIMESTAMP",
                    "value": "2022-07-10T20:58:46Z"
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateNode_invalidPropertyKey_badRequest() throws Exception {

    this.mockMvc
        .perform(
            put("/nodes/{nodeId}", UlidCreator.getUlid().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "",
                    "type": "BOOLEAN",
                    "value": true
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateNode_invalidPropertyType_badRequest() throws Exception {

    this.mockMvc
        .perform(
            put("/nodes/{nodeId}", UlidCreator.getUlid().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someKey",
                    "type": "UNKNOWN",
                    "value": true
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateNode_invalidPropertyValue_badRequest() throws Exception {

    this.mockMvc
        .perform(
            put("/nodes/{nodeId}", UlidCreator.getUlid().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someBoolean",
                    "type": "BOOLEAN",
                    "value": 123
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateNode_invalidUlidFormat_badRequest() throws Exception {

    this.mockMvc
        .perform(
            put("/nodes/{nodeId}", "xxxxxxxxxxxxxxxxxxxxxxxxxx")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someTimestamp",
                    "type": "TIMESTAMP",
                    "value": "2022-07-10T20:58:46Z"
                  }
                ]
              }
              """))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateNode_validForm_succeeded() throws Exception {

    this.mockMvc
        .perform(
            put("/nodes/{nodeId}", UlidCreator.getUlid().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
              {
                "name": "my-node",
                "properties": [
                  {
                    "key": "someBoolean",
                    "type": "BOOLEAN",
                    "value": true
                  }
                ]
              }
              """))
        .andExpect(status().isOk());
  }
}
