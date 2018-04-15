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

import java.util.ArrayList;

public class TaggedPhotoActivity extends AppCompatActivity implements TaggedPhotoClickListener {

    private final ArrayList<Object> mObjectArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagged_photo);
        mObjectArrayList.addAll(InstaTagSampleApplication.getInstance().getTaggedPhotos());
        RecyclerView mRecyclerViewTaggedPhotos = findViewById(R.id.rv_tagged_photos);
        TaggedPhotoAdapter taggedPhotoAdapter = new TaggedPhotoAdapter(mObjectArrayList, this, this);
        mRecyclerViewTaggedPhotos.setAdapter(taggedPhotoAdapter);
        mRecyclerViewTaggedPhotos.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onTaggedPhotoClick(TaggedPhoto taggedPhoto, int position) {
        // Do something when clicked on any tagged photo in mRecyclerViewTaggedPhotos
    }
}
