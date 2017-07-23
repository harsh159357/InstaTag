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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class InstaActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CHOOSE_A_PHOTO_TO_BE_TAGGED = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);
        findViewById(R.id.tag_a_photo).setOnClickListener(this);
        findViewById(R.id.see_tagged_photos).setOnClickListener(this);
        findViewById(R.id.see_some_ones).setOnClickListener(this);
        findViewById(R.id.drag_test).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tag_a_photo:
                Intent photoToBeTaggedIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoToBeTaggedIntent, CHOOSE_A_PHOTO_TO_BE_TAGGED);
                break;
            case R.id.see_tagged_photos:
                if (!InstaTagSampleApplication.getInstance().getTaggedPhotos().isEmpty()) {
                    Intent TaggedPhotoIntent = new Intent();
                    TaggedPhotoIntent.setClass(InstaActivity.this, TaggedPhotoActivity.class);
                    startActivity(TaggedPhotoIntent);
                } else {
                    Toast.makeText(this, "You have not tagged any photo yet", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.see_some_ones:
                Intent someOnesIntent = new Intent();
                someOnesIntent.setClass(InstaActivity.this, SomeOnesActivity.class);
                startActivity(someOnesIntent);
                break;
            case R.id.drag_test:
                Intent dragTestIntent = new Intent();
                dragTestIntent.setClass(InstaActivity.this, DragTestActivity.class);
                startActivity(dragTestIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_A_PHOTO_TO_BE_TAGGED && resultCode == RESULT_OK && data != null) {
            Uri photoToBeTagged = data.getData();
            Intent tagPhotoIntent = new Intent();
            tagPhotoIntent.setClass(this, TagPhotoActivity.class);
            tagPhotoIntent.setData(photoToBeTagged);
            startActivity(tagPhotoIntent);
        }
    }
}
