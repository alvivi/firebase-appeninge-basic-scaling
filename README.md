# firebase & appeninge

This repository contains a simple example of an appengine module using firebase. The module is configured with basic scaling enabled. In theory, after of a period of inactivity, the module should be shut down by appengine. In practice, this never happens if you are using firebase, even after using Firebase's goOffline.

These are the steps to configure, upload and test the issue:

* Edit application ID in `src/main/webapp/WEB-INF/appengine-web.xml`
* Upload the module using `gradlew appengineRun`
* Do an HTTP GET request to the module.
