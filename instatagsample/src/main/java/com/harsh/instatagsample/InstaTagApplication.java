/*
 * Copyright 2019 Harsh Sharma
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
 *
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

    private int tagShowAnimation,
            tagHideAnimation,
            likeAnimation;

    private int carrotTopColor,
            tagBackgroundColor,
            tagTextColor,
            likeColor;

    @Override
    public void onCreate() {
        super.onCreate();
        appPreferences = new AppPreferences(instaTagApplication);
        UsersData.getUsers().addAll(
                UsersData.getUsersFromJson(
                        RawJsonToStringUtil.rawJsonToString(instaTagApplication.getResources(),
                                R.raw.users)));

        tagShowAnimation = R.anim.zoom_in;
        tagHideAnimation = R.anim.zoom_out;

        carrotTopColor = instaTagApplication.getResources().getColor(R.color.colorPrimaryDark);
        tagBackgroundColor = instaTagApplication.getResources().getColor(R.color.colorPrimaryDark);
        tagTextColor = instaTagApplication.getResources().getColor(android.R.color.white);
        likeColor = instaTagApplication.getResources().getColor(android.R.color.white);
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

    public int getTagShowAnimation() {
        return tagShowAnimation;
    }

    public int getTagHideAnimation() {
        return tagHideAnimation;
    }

    public int getLikeAnimation() {
        return likeAnimation;
    }

    public int getCarrotTopColor() {
        return carrotTopColor;
    }

    public int getTagTextColor() {
        return tagTextColor;
    }

    public int getLikeColor() {
        return likeColor;
    }

    public void setTagShowAnimation(int tagShowAnimation) {
        this.tagShowAnimation = tagShowAnimation;
    }

    public void setTagHideAnimation(int tagHideAnimation) {
        this.tagHideAnimation = tagHideAnimation;
    }

    public void setLikeAnimation(int likeAnimation) {
        this.likeAnimation = likeAnimation;
    }

    public void setCarrotTopColor(int carrotTopColor) {
        this.carrotTopColor = carrotTopColor;
    }

    public void setTagTextColor(int tagTextColor) {
        this.tagTextColor = tagTextColor;
    }

    public void setLikeColor(int likeColor) {
        this.likeColor = likeColor;
    }

    public int getTagBackgroundColor() {
        return tagBackgroundColor;
    }

    public void setTagBackgroundColor(int tagBackgroundColor) {
        this.tagBackgroundColor = tagBackgroundColor;
    }
}
