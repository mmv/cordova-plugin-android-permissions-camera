Android permission Cordova plugin
========

This plugin is designed for supporting Android new permissions checking mechanism.

Since Android 6.0, the Android permissions checking mechanism has been changed. In the old days, the permissions were granted by users when they decide to install the app. Now, the permissions should be granted by a user when he/she are using the app.

For old Android plugins you (developers) are using may not support this new mechanism or already stop updating. So either to find a new plugin to solving this problem, nor trying to add the mechanism in the old plugin. If you don't want to do those, you can try this plugin.

Installation
--------

```bash
cordova plugin add cordova-plugin-android-permissions@0.5.0
```

Usage
--------

### API

```javascript
var permissions = window.plugins.permissions;
permissions.hasPermission(successCallback, errorCallback, permission);
permissions.requestPermission(successCallback, errorCallback, permission);
```

### Permission Name

Following the Android design. See [Manifest.permission](http://developer.android.com/intl/zh-tw/reference/android/Manifest.permission.html).
```javascript
// Example
permissions.ACCESS_COARSE_LOCATION
permissions.CAMERA
permissions.GET_ACCOUNTS
permissions.READ_CONTACTS
permissions.READ_CALENDAR
...
```

Example
--------

```javascript
var permissions = window.plugins.permissions;
permissions.hasPermission(checkPermissionCallback, null, permissions.CAMERA);

function checkPermissionCallback(status) {
  if(!status.hasPermission) {
    var errorCallback = function() {
      console.warn('Camera permission is not turned on');
    }

    permissions.requestPermission(function(status) {
      if( !status.hasPermission ) errorCallback();
    }, errorCallback, permissions.CAMERA);
  }
}
```
