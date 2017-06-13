package com.harsh.instatagsample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.harsh.instatag.InstaTag;
import com.harsh.instatag.InstaTagImageView;

import java.util.ArrayList;
import java.util.Calendar;

public class TagPhotoActivity extends AppCompatActivity implements SomeOneToBeTaggedAdapterClickListener, View.OnClickListener {

    private InstaTag instaTag;
    private Uri photoToBeTaggedUri;
    private RecyclerView recyclerViewSomeOneToBeTagged;
    private LinearLayout headerSomeOneToBeTagged, headerSearchSomeOne;
    private TextView tapPhotoToTagSomeOneTextView;
    private int addTagInX, addTagInY;
    private EditText editSearchForSomeOne;
    private SomeOneToBeTaggedAdapter someOneToBeTaggedAdapter;
    ArrayList<SomeOne> someOnes = new ArrayList<>();

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
        editSearchForSomeOne = (EditText) findViewById(R.id.search_for_a_person);

        editSearchForSomeOne.addTextChangedListener(textWatcher);

        cancelTextView.setOnClickListener(this);
        doneImageView.setOnClickListener(this);
        backImageView.setOnClickListener(this);

        loadImage();

        someOnes.addAll(SomeOnesData.getDummySomeOneList());
        someOneToBeTaggedAdapter = new SomeOneToBeTaggedAdapter(someOnes, this, this);
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
    public void onSomeOneToBeTaggedClick(final SomeOne someOne, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instaTag.addTagView(addTagInX, addTagInY, someOne.getUserName());
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
                if (instaTag.getListOfUserToBeTagged().isEmpty()) {
                    Toast.makeText(this, "Please tag at least one user", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<TaggedPhoto> taggedPhotoArrayList = InstaTagSampleApplication.getInstance().getTaggedPhotos();
                    taggedPhotoArrayList.add(
                            new TaggedPhoto(
                                    Calendar.getInstance().getTimeInMillis() + "",
                                    photoToBeTaggedUri.toString(),
                                    instaTag.getListOfUserToBeTagged()));
                    InstaTagSampleApplication.getInstance().setTaggedPhotos(taggedPhotoArrayList);
                    Toast.makeText(this, "Photo tagged successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.get_back:
                finish();
                break;
        }
    }

    InstaTag.ImageToBeTaggedEvent imageToBeTaggedEvent = new InstaTag.ImageToBeTaggedEvent() {
        @Override
        public void singleTapConfirmedAndInstaRootIsInTouch(final int x, final int y) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addTagInX = x;
                    addTagInY = y;
                    recyclerViewSomeOneToBeTagged.setVisibility(View.VISIBLE);
                    headerSomeOneToBeTagged.setVisibility(View.GONE);
                    tapPhotoToTagSomeOneTextView.setVisibility(View.GONE);
                    headerSearchSomeOne.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editSearchForSomeOne.getText().toString().trim().equals("")) {
                someOnes.clear();
                someOnes.addAll(SomeOnesData.getDummySomeOneList());
                someOneToBeTaggedAdapter.notifyDataSetChanged();
            } else {
                someOnes.clear();
                someOnes.addAll(SomeOnesData.getFilteredUser(editSearchForSomeOne.getText().toString().trim()));
                someOneToBeTaggedAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
