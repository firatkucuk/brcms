package com.bloomreach.brcms.cmsbackend.product;

import java.util.List;

public class SectionInfo {

  static final String SECTION_PROPERTY_TITLE = "title";
  static final String SECTION_PROPERTY_CONTENT = "content";

  private String name;
  private String title;
  private String content;
  private String parentName;
  private List<PageLink> subPages;

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

  public String getContent() {
    return this.content;
  }

  public void setContent(final String content) {
    this.content = content;
  }

  public String getParentName() {
    return this.parentName;
  }

  public void setParentName(final String parentName) {
    this.parentName = parentName;
  }

  public List<PageLink> getSubPages() {
    return this.subPages;
  }

  public void setSubPages(final List<PageLink> subPages) {
    this.subPages = subPages;
  }
}
