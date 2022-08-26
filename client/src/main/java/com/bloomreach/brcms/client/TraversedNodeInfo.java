package com.bloomreach.brcms.client;

import java.util.List;

public class TraversedNodeInfo extends NodeInfo {

  private List<NodeInfo> children;

  public List<NodeInfo> getChildren() {
    return this.children;
  }

  public void setChildren(final List<NodeInfo> children) {
    this.children = children;
  }
}
