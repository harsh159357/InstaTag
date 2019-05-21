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

package com.harsh.instatagsample.fragments.dashboard;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.harsh.instatag.SquareImageView;
import com.harsh.instatagsample.InstaTagApplication;
import com.harsh.instatagsample.R;
import com.harsh.instatagsample.activities.DashBoardActivity;
import com.harsh.instatagsample.adapters.PhotoAdapter;
import com.harsh.instatagsample.interfaces.AppConstants;
import com.harsh.instatagsample.interfaces.PhotoClickListener;
import com.harsh.instatagsample.models.Photo;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements PhotoClickListener, AppConstants, View.OnClickListener {

    private IntentFilter newPhotoTaggedIntentFilter = new IntentFilter(Events.NEW_PHOTO_IS_TAGGED);
    private IntentFilter newConfigurationSavedIntentFilter = new IntentFilter(Events.NEW_CONFIGURATION_SAVED);

    private ArrayList<Photo> photos = new ArrayList<>();
    private RecyclerView recyclerViewPhotos;
    private LinearLayout emptyContainer;
    private PhotoAdapter photoAdapter;

    private Handler handler;
    private ProgressDialog progressDialog;

    SquareImageView configuration;

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

        configuration = rootView.findViewById(R.id.iv_configuration);
        configuration.setOnClickListener(this);
        emptyContainer = rootView.findViewById(R.id.empty_container);
        rootView.findViewById(R.id.iv_delete_all_photos).setOnClickListener(this);
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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newConfigurationSaved,
                newConfigurationSavedIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(newPhotoIsTagged);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(newConfigurationSaved);
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

    private BroadcastReceiver newConfigurationSaved = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showProgress(ProgressText.RELOADING);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    photoAdapter = new PhotoAdapter(photos, getContext(),
                            HomeFragment.this,
                            InstaTagApplication.getInstance().getTagShowAnimation(),
                            InstaTagApplication.getInstance().getTagHideAnimation(),
                            InstaTagApplication.getInstance().getCarrotTopColor(),
                            InstaTagApplication.getInstance().getTagBackgroundColor(),
                            InstaTagApplication.getInstance().getTagTextColor(),
                            InstaTagApplication.getInstance().getLikeColor()
                    );
                    recyclerViewPhotos.setAdapter(photoAdapter);
                    showEmptyContainer();
                    dismissProgress();
                }
            }, CONFIGURATION_DELAY_MILLIS);
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

    @Override
    public void onClick(View view) {
        if (!InstaTagApplication.getInstance().getPhotos().isEmpty()) {
            switch (view.getId()) {
                case R.id.iv_configuration:
                    ((DashBoardActivity) getActivity()).showConfigurationBottomSheet();
                    break;
                case R.id.iv_delete_all_photos:
                    showDeleteAllPhotosAlertDialog();
                    break;
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_posts_yet), Toast.LENGTH_SHORT).show();
        }
    }

    public void showDeleteAllPhotosAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(getString(R.string.remove_all_tagged_posts));
        alert.setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_posts));
        alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InstaTagApplication.getInstance().savePhotos(new ArrayList<Photo>());
                photos.clear();
                photoAdapter.notifyDataSetChanged();
                showEmptyContainer();
            }
        });
        alert.setNegativeButton(getString(R.string.cancel), null);
        alert.show();
    }
}
