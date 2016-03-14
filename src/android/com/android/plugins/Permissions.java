package com.android.plugins;

import android.os.Build;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by JasonYang on 2016/3/11.
 */
public class Permissions extends CordovaPlugin {

    private static final String ACTION_HAS_PERMISSION = "hasPermission";
    private static final String ACTION_REQUEST_PERMISSION = "requestPermission";

    private static final int REQUEST_CODE_ENABLE_PERMISSION = 55433;

    private static final String KEY_ERROR = "error";
    private static final String KEY_MESSAGE = "message";

    private CallbackContext permissionsCallback;

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (ACTION_HAS_PERMISSION.equals(action)) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    hasPermissionAction(callbackContext, args);
                }
            });
            return true;
        } else {
            if (ACTION_REQUEST_PERMISSION.equals(action)) {
                cordova.getThreadPool().execute(new Runnable() {
                    public void run() {
                        requestPermissionAction(callbackContext, args);
                    }
                });
                return true;
            }
        }
        return false;
    }

    private void hasPermissionAction(CallbackContext callbackContext, JSONArray permission) {
        if (permission == null || permission.length() == 0 || permission.length() > 1) {
            JSONObject returnObj = new JSONObject();
            addProperty(returnObj, KEY_ERROR, ACTION_HAS_PERMISSION);
            addProperty(returnObj, KEY_MESSAGE, "One time one permission only.");
            callbackContext.error(returnObj);
        } else {
            try {
                JSONObject returnObj = new JSONObject();
                addProperty(returnObj, ACTION_HAS_PERMISSION, cordova.hasPermission(permission.getString(0)));
                callbackContext.success(returnObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void requestPermissionAction(CallbackContext callbackContext, JSONArray permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            JSONObject returnObj = new JSONObject();
            addProperty(returnObj, KEY_ERROR, ACTION_REQUEST_PERMISSION);
            addProperty(returnObj, KEY_MESSAGE, "Operation unsupported.");
            callbackContext.error(returnObj);
            return;
        } else if (permission == null || permission.length() == 0 || permission.length() > 1) {
            JSONObject returnObj = new JSONObject();
            addProperty(returnObj, KEY_ERROR, ACTION_REQUEST_PERMISSION);
            addProperty(returnObj, KEY_MESSAGE, "One time one permission only.");
            callbackContext.error(returnObj);
            return;
        } else {
            permissionsCallback = callbackContext;
            try {
                cordova.requestPermission(this, REQUEST_CODE_ENABLE_PERMISSION, permission.getString(0));
            } catch (JSONException e) {
                e.printStackTrace();
                permissionsCallback = null;
            }
        }
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) throws JSONException {
        if (permissionsCallback == null) {
            return;
        }

        //Just call hasPermission again to verify
        JSONObject returnObj = new JSONObject();
        if (permissions != null && permissions.length > 0) {
            addProperty(returnObj, ACTION_HAS_PERMISSION, cordova.hasPermission(permissions[0]));
        } else {
            addProperty(returnObj, KEY_ERROR, ACTION_REQUEST_PERMISSION);
            addProperty(returnObj, KEY_MESSAGE, "Unknown error.");
        }
        permissionsCallback.success(returnObj);
    }

    private void addProperty(JSONObject obj, String key, Object value) {
        //Believe exception only occurs when adding duplicate keys, so just ignore it
        try {
            if (value == null) {
                obj.put(key, JSONObject.NULL);
            } else {
                obj.put(key, value);
            }
        } catch (JSONException ignored) {
        }
    }
}
