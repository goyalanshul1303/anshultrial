package com.application.onboarding.providersob;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by aggarwal.swati on 12/19/18.
 */

public class SharedPreferences {
    private static final String PREFERENCE_NAME = "preferences";

    public static final String KEY_AUTHTOKEN = "authToken";
    public static void putString(Context context, String key, String value) {
        if (context != null) {
            android.content.SharedPreferences.Editor edit = getSharedPreferences(context).edit();
            if (TextUtils.isEmpty(value)) {
                edit.remove(key);
            } else {
                edit.putString(key, value);
            }
            edit.commit();
        }
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (context != null) {
            getSharedPreferences(context).edit().putBoolean(key, value).commit();
        }
    }

    public static String getString(Context context, String key) {
        if (context != null) {
            return getSharedPreferences(context).getString(key, null);
        } else {
            return getString(context, key, null);
        }
    }
    private SharedPreferences() {

    }
    public static String getString(Context context, String key, String def) {
        if (context != null) {
            return getSharedPreferences(context).getString(key, def);
        } else {
            return null;
        }
    }


    public static android.content.SharedPreferences getSharedPreferences(Context context) {
        android.content.SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        return sharedPreferences;
    }

}
