package com.bloomreach.brcms.cmsbackend.product;

import com.bloomreach.brcms.client.ContentRepository;
import com.bloomreach.brcms.client.TraversedNodeInfo;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ProductController {

  private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

  private final ContentRepository contentRepository;

  public ProductController(final @NonNull ContentRepository contentRepository) {
    this.contentRepository = contentRepository;
  }

  @GetMapping("/products/**")
  public String getProductPage(
    final @NonNull HttpServletRequest request,
    final @NonNull Model model
  ) {

    try {
      LOG.debug("Fetching node from remote content repository");

      final ResponseEntity<TraversedNodeInfo> response =
        this.contentRepository.traverseNode(request.getServletPath());
      final TraversedNodeInfo   nodeInfo   = Objects.requireNonNull(response.getBody());
      final Map<String, Object> properties = nodeInfo.getProperties();

      LOG.info("Node {} fetched", nodeInfo.getName());

      if ("section".equals(properties.get("nodeType"))) {
        model.addAttribute("section", this.convertToSection(nodeInfo));
        return "section";
      } else {
        model.addAttribute("product", this.convertToProduct(nodeInfo));
        return "product";
      }

    } catch (final HttpStatusCodeException e) {
      throw new ResponseStatusException(e.getStatusCode());
    }
  }

  private @NonNull ProductInfo convertToProduct(final @NonNull TraversedNodeInfo nodeInfo) {

    final Map<String, Object> properties = nodeInfo.getProperties();

    final ProductInfo productInfo = new ProductInfo();
    productInfo.setName(nodeInfo.getName());
    productInfo.setTitle((String) properties.get("title"));
    productInfo.setMaterial((String) properties.get("material"));
    productInfo.setColors((List<String>) properties.get("colors"));
    productInfo.setPrice((Double) properties.get("price"));

    return productInfo;
  }

  private @NonNull SectionInfo convertToSection(final @NonNull TraversedNodeInfo nodeInfo) {

    final Map<String, Object> properties = nodeInfo.getProperties();

    final SectionInfo sectionInfo = new SectionInfo();
    sectionInfo.setName(nodeInfo.getName());
    sectionInfo.setTitle((String) properties.get("title"));
    sectionInfo.setContent((String) properties.get("content"));
    sectionInfo.setParentName(nodeInfo.getParentName());

    final List<PageLink> links = nodeInfo.getChildren()
      .stream().map(c -> new PageLink((String) c.getProperties().get("title"), c.getName()))
      .toList();

    sectionInfo.setSubPages(links);

    return sectionInfo;
  }
}