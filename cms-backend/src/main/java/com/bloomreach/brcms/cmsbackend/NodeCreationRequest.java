package com.bloomreach.brcms.cmsbackend;

import com.bloomreach.brcms.client.NodeForm;
import java.util.List;

public class NodeCreationRequest {

  private NodeForm form;
  private List<NodeCreationRequest> requests;

  public NodeForm getForm() {
    return this.form;
  }

  public void setForm(final NodeForm form) {
    this.form = form;
  }

  public List<NodeCreationRequest> getRequests() {
    return this.requests;
  }

  public void setRequests(final List<NodeCreationRequest> requests) {
    this.requests = requests;
  }
}
