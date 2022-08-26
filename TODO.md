- common response
- javadoc
- optimize dependencies
- extra REST methods
- extra unit tests

```
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
