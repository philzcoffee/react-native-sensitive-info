package br.com.classapp.RNSensitiveInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.Map;

/**
 * Fingerprint-protected storage (the `touchID` option) has been removed on Android.
 * It was built on android.hardware.fingerprint.FingerprintManager, which is no longer
 * part of the Android SDK as of API 37. Calls that ask for it behave as they did on
 * devices without a fingerprint sensor: the availability checks resolve false and
 * `touchID` reads and writes reject with "Fingerprint not supported".
 */
public class RNSensitiveInfoModule extends NativeSensitiveInfoSpec {

    public static final String NAME = "RNSensitiveInfo";

    private static final String FINGERPRINT_NOT_SUPPORTED = "Fingerprint not supported";

    public RNSensitiveInfoModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void setInvalidatedByBiometricEnrollment(final boolean invalidatedByBiometricEnrollment, final Promise pm) {
        pm.resolve(null);
    }

    @ReactMethod
    public void isHardwareDetected(final Promise pm) {
        pm.resolve(false);
    }

    @ReactMethod
    public void hasEnrolledFingerprints(final Promise pm) {
        pm.resolve(false);
    }

    @ReactMethod
    public void isSensorAvailable(final Promise promise) {
        promise.resolve(false);
    }

    @ReactMethod
    public void getItem(String key, ReadableMap options, Promise pm) {

        String name = sharedPreferences(options);

        String value = prefs(name).getString(key, null);

        if (value != null && wantsTouchID(options)) {
            pm.reject(FINGERPRINT_NOT_SUPPORTED, FINGERPRINT_NOT_SUPPORTED);
        } else {
            pm.resolve(value);
        }
    }

    @ReactMethod
    public void setItem(String key, String value, ReadableMap options, Promise pm) {

        String name = sharedPreferences(options);

        if (wantsTouchID(options)) {
            pm.reject(FINGERPRINT_NOT_SUPPORTED, FINGERPRINT_NOT_SUPPORTED);
        } else {
            try {
                putExtra(key, value, prefs(name));
                pm.resolve(value);
            } catch (Exception e) {
                Log.d("RNSensitiveInfo", String.valueOf(e.getMessage()));
                pm.reject(e);
            }
        }
    }


    @ReactMethod
    public void deleteItem(String key, ReadableMap options, Promise pm) {

        String name = sharedPreferences(options);

        SharedPreferences.Editor editor = prefs(name).edit();

        editor.remove(key).apply();

        pm.resolve(null);
    }

    @ReactMethod
    public void getAllItems(ReadableMap options, Promise pm) {

        String name = sharedPreferences(options);

        Map<String, ?> allEntries = prefs(name).getAll();
        WritableArray resultData = new WritableNativeArray();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            WritableMap entryMap = new WritableNativeMap();

            entryMap.putString("key", entry.getKey());
            entryMap.putString("value", entry.getValue().toString());
            entryMap.putString("service", name);
            resultData.pushMap(entryMap);
        }

        WritableArray resultWrapper = new WritableNativeArray();
        resultWrapper.pushArray(resultData);
        pm.resolve(resultWrapper);
    }

    @ReactMethod
    public void cancelFingerprintAuth() {
    }

    private static boolean wantsTouchID(ReadableMap options) {
        return options.hasKey("touchID") && options.getBoolean("touchID");
    }

    private SharedPreferences prefs(String name) {
        return getReactApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    @NonNull
    private String sharedPreferences(ReadableMap options) {
        String name = options.hasKey("sharedPreferencesName") ? options.getString("sharedPreferencesName") : "shared_preferences";
        if (name == null) {
            name = "shared_preferences";
        }
        return name;
    }


    private void putExtra(String key, Object value, SharedPreferences mSharedPreferences) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value).apply();
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value).apply();
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value).apply();
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value).apply();
        }
    }
}
