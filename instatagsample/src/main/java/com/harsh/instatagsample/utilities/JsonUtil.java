package com.harsh.instatagsample.utilities;

import android.util.Log;

import com.google.gson.Gson;

public class JsonUtil {
    public static String toJson(Object object) {
        try {
            Gson gson = new com.google.gson.Gson();
            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(JsonUtil.class.getSimpleName(), "Error In Converting ModelToJson", e);
        }
        return "";
    }

}
