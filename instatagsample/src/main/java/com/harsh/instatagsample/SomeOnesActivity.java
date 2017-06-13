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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class SomeOnesActivity extends AppCompatActivity implements SomeOneToBeTaggedAdapterClickListener {

    RecyclerView recyclerViewSomeOne;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_one);
        recyclerViewSomeOne = (RecyclerView) findViewById(R.id.rv_some_one);
        SomeOneToBeTaggedAdapter someOneToBeTaggedAdapter = new SomeOneToBeTaggedAdapter(SomeOnesData.getDummySomeOneList(), this, this);
        recyclerViewSomeOne.setAdapter(someOneToBeTaggedAdapter);
        recyclerViewSomeOne.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onSomeOneToBeTaggedClick(final SomeOne someOne, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SomeOnesActivity.this, someOne.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
