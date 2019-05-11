package com.harsh.instatagsample.utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.harsh.instatagsample.InstaTagApplication;

public class JsonUtil {
    public static String toJson(Object object) {
        try {
            Gson gson = new com.google.gson.Gson();
            return gson.toJson(object);
        } catch (Exception e) {
            Log.e(InstaTagApplication.SHARED_PREF_NAME, "Error In Converting ModelToJson", e);
        }
        return "";
    }

}
