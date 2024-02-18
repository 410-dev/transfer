# transfer
Inspired by transfer.sh. Application for Apache Tomcat.


### Build Dependencies
- Maven (Build tool)
- libhy [here](https://github.com/410-dev/libhy)
- libcentralization (not published)
  - This is optional, but to build with out this, you have to remove `me.hysong.dev.apps.transfer.servlets.RegisterOnStart` class and update the return code of `me.hysong.dev.modules.PathFactory` to desired path so that the code is no longer dependent on libcentralization.
 
  

