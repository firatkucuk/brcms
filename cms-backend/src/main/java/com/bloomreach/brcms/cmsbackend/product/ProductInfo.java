package com.bloomreach.brcms.cmsbackend.product;

import java.util.List;

public class ProductInfo {

  static final String PRODUCT_PROPERTY_TITLE = "title";
  static final String PRODUCT_PROPERTY_MATERIAL = "material";
  static final String PRODUCT_PROPERTY_COLORS = "colors";
  static final String PRODUCT_PROPERTY_PRICE = "price";

  private String name;
  private String title;
  private String material;
  private List<String> colors;
  private Double price;

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getMaterial() {
    return this.material;
  }

  public void setMaterial(final String material) {
    this.material = material;
  }

  public List<String> getColors() {
    return this.colors;
  }

  public void setColors(final List<String> colors) {
    this.colors = colors;
  }

  public Double getPrice() {
    return this.price;
  }

  public void setPrice(final Double price) {
    this.price = price;
  }
}
