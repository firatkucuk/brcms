package com.bloomreach.brcms.cmsbackend.product;

public class PageLink {

  private String title;
  private String url;

  public PageLink() {
  }

  public PageLink(final String title, final String url) {
    this.title = title;
    this.url = url;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(final String url) {
    this.url = url;
  }
}
