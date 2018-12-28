package com.app.carton.orders;

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


}
