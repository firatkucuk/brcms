package com.bloomreach.brcms.cmsbackend.product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bloomreach.brcms.client.ContentRepository;
import com.bloomreach.brcms.client.TraversedNodeInfo;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ContentRepository contentRepository;

  @Test
  void getProductPage_nonExistingPath_notFound() throws Exception {

    Mockito.when(this.contentRepository.traverseNode(Mockito.any()))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    this.mockMvc.perform(get("/products/xxxxxx")).andExpect(status().isNotFound());
  }

  @Test
  void getProductPage_sectionData_succeeded() throws Exception {

    final TraversedNodeInfo traversedNodeInfo = new TraversedNodeInfo();
    traversedNodeInfo.setName("sample-section");

    final Map<String, Object> properties =
        Map.of(
            "nodeType",
            "section",
            SectionInfo.SECTION_PROPERTY_TITLE,
            "Sample section",
            SectionInfo.SECTION_PROPERTY_CONTENT,
            "Sample section page");

    traversedNodeInfo.setProperties(properties);

    Mockito.when(this.contentRepository.traverseNode(Mockito.any()))
        .thenReturn(ResponseEntity.ok(traversedNodeInfo));

    this.mockMvc.perform(get("/products/sample-section")).andExpect(status().isOk());
  }

  @Test
  void getProductPage_productData_succeeded() throws Exception {

    final TraversedNodeInfo traversedNodeInfo = new TraversedNodeInfo();
    traversedNodeInfo.setName("sample-product");

    final Map<String, Object> properties =
        Map.of(
            "nodeType",
            "product",
            ProductInfo.PRODUCT_PROPERTY_TITLE,
            "Sample product",
            ProductInfo.PRODUCT_PROPERTY_MATERIAL,
            "Polyster",
            ProductInfo.PRODUCT_PROPERTY_COLORS,
            List.of("Red", "Yelow"),
            ProductInfo.PRODUCT_PROPERTY_PRICE,
            14.34d);

    traversedNodeInfo.setProperties(properties);

    Mockito.when(this.contentRepository.traverseNode(Mockito.any()))
        .thenReturn(ResponseEntity.ok(traversedNodeInfo));

    this.mockMvc.perform(get("/products/sample-product")).andExpect(status().isOk());
  }
}
