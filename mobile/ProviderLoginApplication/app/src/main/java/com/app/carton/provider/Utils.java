package com.app.carton.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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

    public static  String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public static void setDetailsTextField(String preText,
                                           Context context, TextView textView, String postText) {
        if (null == context) {
            return;
        }
        SpannableStringBuilder longDescription = new SpannableStringBuilder();

        longDescription.append(getKeyword(preText,
                context.getResources().getColor(R.color.course_heading_color)));
        longDescription.append(" ");
        longDescription.append(getKeyword(postText,
                context.getResources().getColor(R.color.color_dashboard_papers_text)));
        textView.setText(longDescription);

    }
    private static SpannableString getKeyword(String keyword, int color) {
        SpannableString spanKyeword = new SpannableString(keyword);
        spanKyeword.setSpan(new ForegroundColorSpan(color), 0,
                keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanKyeword;
    }
    public static String toCSV(ArrayList<String> array)
    {
        String result = ""; if (array.size() > 0)
    {
        StringBuilder sb = new StringBuilder();
        for (String s : array) { sb.append(s).append(",");
        }
        result = sb.deleteCharAt(sb.length() - 1).toString();
    }
        return result;
    }
}
