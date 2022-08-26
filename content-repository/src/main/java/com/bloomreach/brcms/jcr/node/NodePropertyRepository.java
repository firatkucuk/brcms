package com.bloomreach.brcms.jcr.node;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface NodePropertyRepository extends JpaRepository<NodeProperty, String> {

  void deleteAllByNodeId(String nodeId);
}
