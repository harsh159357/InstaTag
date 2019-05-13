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
 *
 */

package com.harsh.instatagsample.utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.harsh.instatagsample.interfaces.AppConstants;
import com.harsh.instatagsample.models.User;

import java.util.ArrayList;

public class UsersData implements AppConstants {
    private static final ArrayList<User> usersList = new ArrayList<>();

    public static ArrayList<User> getUsers() {
        return usersList;
    }

    public static ArrayList<User> getFilteredUsers(String searchString) {
        ArrayList<User> filteredUser = new ArrayList<>();
        for (User user : usersList) {
            if (user.getFullName().contains(searchString) ||
                    user.getUserName().contains(searchString)) {
                filteredUser.add(user);
            }
        }
        if (filteredUser.isEmpty()) {
            filteredUser.add(new User(NO_USER_FOUND, NO_USER_FOUND, NO_USER_FOUND));
        }
        return filteredUser;
    }

    public static ArrayList<User> getUsersFromJson(String json) {
        ArrayList<User> userArrayList;
        if (!json.equals("")) {
            userArrayList = new Gson().fromJson(json, new TypeToken<ArrayList<User>>() {
            }.getType());
        } else {
            userArrayList = new ArrayList<>();
        }
        return userArrayList;
    }
}
