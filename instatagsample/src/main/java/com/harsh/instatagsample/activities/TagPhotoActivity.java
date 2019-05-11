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

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.harsh.instatag.InstaTag;
import com.harsh.instatag.TagImageView;
import com.harsh.instatagsample.InstaTagApplication;
import com.harsh.instatagsample.R;
import com.harsh.instatagsample.adapters.UserAdapter;
import com.harsh.instatagsample.interfaces.UserClickListener;
import com.harsh.instatagsample.models.TaggedPhoto;
import com.harsh.instatagsample.models.User;
import com.harsh.instatagsample.utilities.KeyBoardUtil;
import com.harsh.instatagsample.utilities.UsersData;

import java.util.ArrayList;
import java.util.Calendar;

public class TagPhotoActivity extends AppCompatActivity implements UserClickListener,
        View.OnClickListener {

    private InstaTag instaTag;
    private Uri photoToBeTaggedUri;
    private RecyclerView recyclerViewUsersToBeTagged;
    private LinearLayout userToBeTagged, headerSearchUser;
    private TextView tapPhotoToTagUser;
    private int addTagInX, addTagInY;
    private EditText searchForUser;
    private UserAdapter userAdapter;
    private final ArrayList<User> users = new ArrayList<>();
    private RequestOptions requestOptions =
            new RequestOptions()
                    .placeholder(0)
                    .fallback(0)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_photo);

        photoToBeTaggedUri = getIntent().getData();

        instaTag = findViewById(R.id.insta_tag);
        instaTag.setImageToBeTaggedEvent(taggedImageEvent);

        final TextView cancelTextView = findViewById(R.id.cancel);
        final TagImageView doneImageView = findViewById(R.id.done);
        final TagImageView backImageView = findViewById(R.id.get_back);

        recyclerViewUsersToBeTagged = findViewById(R.id.rv_some_one_to_be_tagged);
        tapPhotoToTagUser = findViewById(R.id.tap_photo_to_tag_someone);
        userToBeTagged = findViewById(R.id.header_tag_photo);
        headerSearchUser = findViewById(R.id.header_search_someone);
        searchForUser = findViewById(R.id.search_for_a_person);

        searchForUser.addTextChangedListener(textWatcher);

        cancelTextView.setOnClickListener(this);
        doneImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);

        loadImage();

        users.addAll(UsersData.getDummySomeOneList());
        userAdapter = new UserAdapter(users, this, this);
        recyclerViewUsersToBeTagged.setAdapter(userAdapter);
        recyclerViewUsersToBeTagged.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadImage() {
        Glide
                .with(this)
                .load(photoToBeTaggedUri)
                .apply(requestOptions)
                .into(instaTag.getTagImageView());
    }

    @Override
    public void onUserClick(final User user, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtil.hideKeyboard(TagPhotoActivity.this);
                instaTag.addTag(addTagInX, addTagInY, user.getUserName());
                recyclerViewUsersToBeTagged.setVisibility(View.GONE);
                tapPhotoToTagUser.setVisibility(View.VISIBLE);
                headerSearchUser.setVisibility(View.GONE);
                userToBeTagged.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                KeyBoardUtil.hideKeyboard(this);
                recyclerViewUsersToBeTagged.scrollToPosition(0);
                recyclerViewUsersToBeTagged.setVisibility(View.GONE);
                tapPhotoToTagUser.setVisibility(View.VISIBLE);
                headerSearchUser.setVisibility(View.GONE);
                userToBeTagged.setVisibility(View.VISIBLE);
                break;
            case R.id.done:
                if (instaTag.getListOfTagsToBeTagged().isEmpty()) {
                    Toast.makeText(this,
                            "Please tag at least one user", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<TaggedPhoto> taggedPhotoArrayList = InstaTagApplication
                            .getInstance().getTaggedPhotos();
                    taggedPhotoArrayList.add(
                            new TaggedPhoto(
                                    Calendar.getInstance().getTimeInMillis() + "",
                                    photoToBeTaggedUri.toString(),
                                    instaTag.getListOfTagsToBeTagged()));
                    InstaTagApplication.getInstance().setTaggedPhotos(taggedPhotoArrayList);
                    Toast.makeText(this,
                            "Photo tagged successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.get_back:
                finish();
                break;
        }
    }

    private final InstaTag.TaggedImageEvent taggedImageEvent = new InstaTag.TaggedImageEvent() {
        @Override
        public void singleTapConfirmedAndRootIsInTouch(final int x, final int y) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addTagInX = x;
                    addTagInY = y;
                    recyclerViewUsersToBeTagged.setVisibility(View.VISIBLE);
                    userToBeTagged.setVisibility(View.GONE);
                    tapPhotoToTagUser.setVisibility(View.GONE);
                    headerSearchUser.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }
    };

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (searchForUser.getText().toString().trim().equals("")) {
                users.clear();
                users.addAll(UsersData.getDummySomeOneList());
                userAdapter.notifyDataSetChanged();
            } else {
                users.clear();
                users.addAll(UsersData.
                        getFilteredUser(searchForUser.getText().toString().trim()));
                userAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
