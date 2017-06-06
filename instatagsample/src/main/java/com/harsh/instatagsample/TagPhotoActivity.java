package com.harsh.instatagsample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.harsh.instatag.InstaTag;
import com.harsh.instatag.InstaTagImageView;

public class TagPhotoActivity extends AppCompatActivity implements SomeOneToBeTaggedAdapterClickListener, View.OnClickListener {

    private InstaTag instaTag;
    private Uri photoToBeTaggedUri;
    private RecyclerView recyclerViewSomeOneToBeTagged;
    private LinearLayout headerSomeOneToBeTagged, headerSearchSomeOne;
    private TextView tapPhotoToTagSomeOneTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_photo);
        photoToBeTaggedUri = getIntent().getData();
        instaTag = (InstaTag) findViewById(R.id.insta_tag);
        instaTag.setImageToBeTaggedEvent(imageToBeTaggedEvent);
        final TextView cancelTextView = (TextView) findViewById(R.id.cancel);
        final InstaTagImageView doneImageView = (InstaTagImageView) findViewById(R.id.done);
        final InstaTagImageView backImageView = (InstaTagImageView) findViewById(R.id.get_back);
        recyclerViewSomeOneToBeTagged = (RecyclerView) findViewById(R.id.rv_some_one_to_be_tagged);
        tapPhotoToTagSomeOneTextView = (TextView) findViewById(R.id.tap_photo_to_tag_someone);
        headerSomeOneToBeTagged = (LinearLayout) findViewById(R.id.header_tag_photo);
        headerSearchSomeOne = (LinearLayout) findViewById(R.id.header_search_someone);
        cancelTextView.setOnClickListener(this);
        doneImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        loadImage();
        SomeOneToBeTaggedAdapter someOneToBeTaggedAdapter = new SomeOneToBeTaggedAdapter(SomeOnesData.getDummySomeOneList(), this, this);
        recyclerViewSomeOneToBeTagged.setAdapter(someOneToBeTaggedAdapter);
        recyclerViewSomeOneToBeTagged.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadImage() {
        Glide.with(this).load(photoToBeTaggedUri)
                .centerCrop()
                .placeholder(0)
                .fallback(0)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(instaTag.getInstaTagImageView());
    }

    @Override
    public void onSomeOneToBeTaggedClick(SomeOne someOne, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerViewSomeOneToBeTagged.setVisibility(View.GONE);
                tapPhotoToTagSomeOneTextView.setVisibility(View.VISIBLE);
                headerSearchSomeOne.setVisibility(View.GONE);
                headerSomeOneToBeTagged.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                recyclerViewSomeOneToBeTagged.scrollToPosition(0);
                recyclerViewSomeOneToBeTagged.setVisibility(View.GONE);
                tapPhotoToTagSomeOneTextView.setVisibility(View.VISIBLE);
                headerSearchSomeOne.setVisibility(View.GONE);
                headerSomeOneToBeTagged.setVisibility(View.VISIBLE);
                break;
            case R.id.done:
                break;
            case R.id.get_back:
                break;
        }
    }

    InstaTag.ImageToBeTaggedEvent imageToBeTaggedEvent = new InstaTag.ImageToBeTaggedEvent() {
        @Override
        public void singleTapConfirmedAndInstaRootIsInTouch() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerViewSomeOneToBeTagged.setVisibility(View.VISIBLE);
                    headerSomeOneToBeTagged.setVisibility(View.GONE);
                    tapPhotoToTagSomeOneTextView.setVisibility(View.GONE);
                    headerSearchSomeOne.setVisibility(View.VISIBLE);
                }
            });
        }
    };
}
