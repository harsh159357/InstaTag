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

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.harsh.instatagsample.InstaTagApplication;
import com.harsh.instatagsample.R;
import com.harsh.instatagsample.adapters.PhotoAdapter;
import com.harsh.instatagsample.interfaces.AppConstants;
import com.harsh.instatagsample.interfaces.PhotoClickListener;
import com.harsh.instatagsample.models.Photo;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PhotoClickListener, AppConstants {

    private IntentFilter newPhotoTaggedIntentFilter = new IntentFilter(Events.NEW_PHOTO_IS_TAGGED);

    private ArrayList<Photo> photos = new ArrayList<>();
    private RecyclerView recyclerViewPhotos;
    private LinearLayout emptyContainer;
    private PhotoAdapter photoAdapter;

    private Handler handler;
    private ProgressDialog progressDialog;

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
        handler = new Handler();

        emptyContainer = rootView.findViewById(R.id.empty_container);
        photos.addAll(InstaTagApplication.getInstance().getPhotos());
        recyclerViewPhotos = rootView.findViewById(R.id.rv_photos);

        photoAdapter = new PhotoAdapter(photos, getContext(), this);
        recyclerViewPhotos.setAdapter(photoAdapter);
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getContext()));

        showEmptyContainer();

        initProgressDialog(ProgressText.PROGRESS_MSG);
        return rootView;
    }

    @Override
    public void onPhotoClick(Photo photo, int position) {

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
        if (InstaTagApplication.getInstance().getPhotos().isEmpty()) {
            recyclerViewPhotos.setVisibility(View.GONE);
            emptyContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerViewPhotos.setVisibility(View.VISIBLE);
            emptyContainer.setVisibility(View.GONE);
        }
    }

    private BroadcastReceiver newPhotoIsTagged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            showProgress(ProgressText.PROGRESS_MSG);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    photos = InstaTagApplication.getInstance().getPhotos();
                    photoAdapter = new PhotoAdapter(photos,
                            getContext(),
                            HomeFragment.this);
                    recyclerViewPhotos.setAdapter(photoAdapter);
                    showEmptyContainer();
                    dismissProgress();
                    recyclerViewPhotos.scrollToPosition(photos.size() - 1);
                    Toast.makeText(getActivity(), ToastText.PHOTO_TAGGED_SUCCESSFULLY
                            , Toast.LENGTH_SHORT).show();
                }
            }, ADD_TAG_DELAY_MILLIS);
        }
    };

    private void initProgressDialog(String msg) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
    }

    public void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgress() {
        progressDialog.dismiss();
    }

}
