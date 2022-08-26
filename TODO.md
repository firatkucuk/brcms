- common response
- open api
- javadoc
- optimize dependencies
- extra REST methods
- extra unit tests

```
NODE OPERATIONS
================================================================================
POST   /nodes                        : Creates a parent node
POST   /nodes/{parent-node-id}       : Creates a child node under parent node
GET    /nodes                        : Lists all nodes
GET    /nodes?by-path={path}         : Traverse node by path
GET    /nodes/{id}                   : Gets specific node
DELETE /nodes/{id}                   : Deletes specific node
PUT    /nodes/{id}                   : Updates a node
PUT    /nodes/{id}/{parent-node-id}  : Moves a child node under a parent node
                                       / Makes child node a parent node

PROPERTY OPERATIONS
================================================================================
GET    /nodes/{id}/properties        : Gets specific node and properties
DELETE /nodes/{id}/properties/{key}  : Deletes a property
POST   /nodes/{id}/properties        : Creates properties
PUT    /nodes/{id}/properties/{key}  : Update single property
PATCH  /nodes/{id}/properties        : Batch update multiple property
```
