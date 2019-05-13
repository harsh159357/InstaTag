/*
 * Copyright 2017 Harsh Sharma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harsh.instatagsample;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harsh.instatagsample.interfaces.AppConstants;
import com.harsh.instatagsample.models.Photo;
import com.harsh.instatagsample.utilities.JsonUtil;
import com.harsh.instatagsample.utilities.RawJsonToStringUtil;
import com.harsh.instatagsample.utilities.UsersData;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;


public class InstaTagApplication extends Application implements AppConstants {
    private static InstaTagApplication instaTagApplication;
    private static AppPreferences appPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        appPreferences = new AppPreferences(instaTagApplication);
        UsersData.getUsers().addAll(
                UsersData.getUsersFromJson(
                        RawJsonToStringUtil.rawJsonToString(instaTagApplication.getResources(),
                                R.raw.users)));

    }

    public InstaTagApplication() {
        instaTagApplication = this;
    }

    public static InstaTagApplication getInstance() {
        if (instaTagApplication == null) {
            instaTagApplication = new InstaTagApplication();
        }
        return instaTagApplication;
    }

    public ArrayList<Photo> getPhotos() {
        String json = appPreferences.getString
                (AppConstants.PreferenceKeys.TAGGED_PHOTOS, "");
        ArrayList<Photo> photoArrayList;
        if (!json.equals("")) {
            photoArrayList =
                    new Gson().fromJson(json, new TypeToken<ArrayList<Photo>>() {
                    }.getType());
        } else {
            photoArrayList = new ArrayList<>();
        }
        return photoArrayList;
    }

    public void savePhotos(ArrayList<Photo> photoArrayList) {
        appPreferences.put(
                AppConstants.PreferenceKeys.TAGGED_PHOTOS,
                JsonUtil.toJson(photoArrayList
                ));
    }
}
