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

package com.harsh.instatagsample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.harsh.instatagsample.R;
import com.harsh.instatagsample.adapters.UserAdapter;
import com.harsh.instatagsample.interfaces.UserClickListener;
import com.harsh.instatagsample.models.User;
import com.harsh.instatagsample.utilities.UsersData;

public class UserActivity extends AppCompatActivity implements UserClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        RecyclerView mRecyclerViewSomeOne = findViewById(R.id.rv_users);
        UserAdapter userAdapter =
                new UserAdapter(UsersData.getDummySomeOneList(),
                        this, this);
        mRecyclerViewSomeOne.setAdapter(userAdapter);
        mRecyclerViewSomeOne.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onUserClick(final User user, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UserActivity.this,
                        user.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
