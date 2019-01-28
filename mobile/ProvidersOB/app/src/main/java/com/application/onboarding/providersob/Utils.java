package com.application.onboarding.providersob;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by aggarwal.swati on 12/2/18.
 */

public class Utils {
    //Fragments Tags
    public static final String Login_Fragment = "Login_Fragment";


    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
    public static Object getElementByIndex(LinkedHashMap map, int index) {
        return (map.keySet().toArray())[index];
    }

    public static void hideKeypad(Context context, View view) {
        if (context != null && view != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
