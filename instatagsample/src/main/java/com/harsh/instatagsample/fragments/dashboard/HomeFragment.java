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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.harsh.instatagsample.InstaTagSampleApplication;
import com.harsh.instatagsample.R;
import com.harsh.instatagsample.adapters.TaggedPhotoAdapter;
import com.harsh.instatagsample.interfaces.TaggedPhotoClickListener;
import com.harsh.instatagsample.models.TaggedPhoto;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements TaggedPhotoClickListener {

    public static final String NEW_PHOTO_IS_TAGGED = "NEW_PHOTO_IS_TAGGED";
    private final ArrayList<Object> mObjectArrayList = new ArrayList<>();
    private RecyclerView mRecyclerViewTaggedPhotos;
    private TaggedPhotoAdapter taggedPhotoAdapter;
    private LinearLayout mEmptyContainer;
    private IntentFilter newPhotoTaggedIntentFilter = new IntentFilter(NEW_PHOTO_IS_TAGGED);

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mEmptyContainer = rootView.findViewById(R.id.empty_container);
        mObjectArrayList.addAll(InstaTagSampleApplication.getInstance().getTaggedPhotos());
        mRecyclerViewTaggedPhotos = rootView.findViewById(R.id.rv_tagged_photos);
        taggedPhotoAdapter = new TaggedPhotoAdapter(mObjectArrayList,
                getContext(), this);
        mRecyclerViewTaggedPhotos.setAdapter(taggedPhotoAdapter);
        mRecyclerViewTaggedPhotos.setLayoutManager(new LinearLayoutManager(getContext()));
        showEmptyContainer();
        return rootView;
    }

    @Override
    public void onTaggedPhotoClick(TaggedPhoto taggedPhoto, int position) {

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newPhotoIsTagged,
                newPhotoTaggedIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(newPhotoIsTagged);
    }

    private void showEmptyContainer() {
        if (InstaTagSampleApplication.getInstance().getTaggedPhotos().isEmpty()) {
            mRecyclerViewTaggedPhotos.setVisibility(View.GONE);
            mEmptyContainer.setVisibility(View.VISIBLE);
        } else {
            mRecyclerViewTaggedPhotos.setVisibility(View.VISIBLE);
            mEmptyContainer.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver newPhotoIsTagged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mObjectArrayList.clear();
            mObjectArrayList.addAll(InstaTagSampleApplication.getInstance().getTaggedPhotos());
            taggedPhotoAdapter.notifyDataSetChanged();
            showEmptyContainer();
        }
    };

}
