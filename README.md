Android permission Cordova plugin
========

This plugin is designed for supporting Android new permissions checking mechanism.

Since Android 6.0, the Android permissions checking mechanism has been changed. In the old days, the permissions were granted by users when they decide to install the app. Now, the permissions should be granted by a user when he/she are using the app.

For old Android plugins you (developers) are using may not support this new mechanism or already stop updating. So either to find a new plugin to solving this problem, nor trying to add the mechanism in the old plugin. If you don't want to do those, you can try this plugin.

Installation
--------

```bash
cordova plugin add cordova-plugin-android-permissions@0.2.1
```

Usage
--------

```javascript
var permissions = window.plugins.permissions;
permissions.hasPermission(checkPermissionCallback);

function checkPermissionCallback(status) {
  if(!status.hasPermission) {
    var errorCallback = function() {
    console.warn('Camera permission is not turned on');
    }

    permissions.requestPermission(function(status) {
      if( !status.hasPermission ) errorCallback();
    }, errorCallback, permissions.Camera);
  }
}
```
