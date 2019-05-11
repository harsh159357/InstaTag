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

package com.harsh.instatagsample.fragments.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

public class TagPhotoFragment extends Fragment implements UserClickListener, View.OnClickListener {

    private static final int CHOOSE_A_PHOTO_TO_BE_TAGGED = 5000;

    private InstaTag instaTag;
    private Uri photoToBeTaggedURI;
    private RecyclerView recyclerViewUsers;
    private LinearLayout headerUsers, headerSearchUsers;
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

    public TagPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tag_photo, container, false);

        instaTag = rootView.findViewById(R.id.insta_tag);
        instaTag.setImageToBeTaggedEvent(taggedImageEvent);

        final TextView cancelTextView = rootView.findViewById(R.id.cancel);
        final TagImageView doneImageView = rootView.findViewById(R.id.done);
        final TagImageView backImageView = rootView.findViewById(R.id.get_back);

        recyclerViewUsers = rootView.findViewById(R.id.rv_some_one_to_be_tagged);
        tapPhotoToTagUser = rootView.findViewById(R.id.tap_photo_to_tag_someone);
        headerUsers = rootView.findViewById(R.id.header_tag_photo);
        headerSearchUsers = rootView.findViewById(R.id.header_search_someone);
        searchForUser = rootView.findViewById(R.id.search_for_a_person);

        searchForUser.addTextChangedListener(textWatcher);

        tapPhotoToTagUser.setOnClickListener(this);
        cancelTextView.setOnClickListener(this);
        doneImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);

        users.addAll(UsersData.getDummySomeOneList());
        userAdapter = new UserAdapter(users, getActivity(),
                TagPhotoFragment.this);
        recyclerViewUsers.setAdapter(userAdapter);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    private void loadImage() {
        Glide
                .with(this)
                .load(photoToBeTaggedURI)
                .apply(requestOptions)
                .into(instaTag.getTagImageView());
    }

    @Override
    public void onUserClick(final User user, int position) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtil.hideKeyboard(getActivity());
                instaTag.addTag(addTagInX, addTagInY, user.getUserName());
                recyclerViewUsers.setVisibility(View.GONE);
                tapPhotoToTagUser.setVisibility(View.VISIBLE);
                headerSearchUsers.setVisibility(View.GONE);
                headerUsers.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                KeyBoardUtil.hideKeyboard(getActivity());
                recyclerViewUsers.scrollToPosition(0);
                recyclerViewUsers.setVisibility(View.GONE);
                tapPhotoToTagUser.setVisibility(View.VISIBLE);
                headerSearchUsers.setVisibility(View.GONE);
                headerUsers.setVisibility(View.VISIBLE);
                break;
            case R.id.done:
                if (photoToBeTaggedURI != null) {
                    if (instaTag.getListOfTagsToBeTagged().isEmpty()) {
                        Toast.makeText(getActivity(),
                                "Please tag at least one user", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayList<TaggedPhoto> taggedPhotoArrayList = InstaTagApplication
                                .getInstance().getTaggedPhotos();
                        taggedPhotoArrayList.add(
                                new TaggedPhoto(
                                        Calendar.getInstance().getTimeInMillis() + "",
                                        photoToBeTaggedURI.toString(),
                                        instaTag.getListOfTagsToBeTagged()));
                        InstaTagApplication.getInstance().setTaggedPhotos(taggedPhotoArrayList);
                        Toast.makeText(getActivity(),
                                "Photo tagged successfully", Toast.LENGTH_SHORT).show();
                        ((ViewPagerFragmentForDashBoard) getParentFragment()).setHomeAsSelectedTab();
                        reset();
                        LocalBroadcastManager.getInstance(getActivity())
                                .sendBroadcast(new Intent(HomeFragment.NEW_PHOTO_IS_TAGGED));
                    }
                } else {
                    Toast.makeText(getActivity(),
                            "Please choose a photo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.get_back:
                ((ViewPagerFragmentForDashBoard) getParentFragment()).setHomeAsSelectedTab();
                reset();
                break;
            case R.id.tap_photo_to_tag_someone:
                if (photoToBeTaggedURI == null) {
                    Intent photoToBeTagged = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(photoToBeTagged, CHOOSE_A_PHOTO_TO_BE_TAGGED);
                }
                break;
        }
    }

    private final InstaTag.TaggedImageEvent taggedImageEvent = new InstaTag.TaggedImageEvent() {
        @Override
        public void singleTapConfirmedAndRootIsInTouch(final int x, final int y) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addTagInX = x;
                    addTagInY = y;
                    recyclerViewUsers.setVisibility(View.VISIBLE);
                    headerUsers.setVisibility(View.GONE);
                    tapPhotoToTagUser.setVisibility(View.GONE);
                    headerSearchUsers.setVisibility(View.VISIBLE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_A_PHOTO_TO_BE_TAGGED
                && resultCode == android.app.Activity.RESULT_OK
                && data != null) {
            photoToBeTaggedURI = data.getData();
            loadImage();
            tapPhotoToTagUser
                    .setText(getString(R.string.tap_photo_tag_user_drag_to_move_or_tap_to_delete));
        }
    }

    private void reset() {
        photoToBeTaggedURI = null;
        loadImage();
        instaTag.removeTags();
        tapPhotoToTagUser.setText(getString(R.string.tap_here_to_choose_a_photo));
    }
}
