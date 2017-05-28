package com.harsh.instatagsample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.harsh.instatag.InstaTag;

public class TagPhotoActivity extends AppCompatActivity {

    private InstaTag instaTag;
    private Uri photoToBeTaggedUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_photo);
        photoToBeTaggedUri = getIntent().getData();
        instaTag = (InstaTag) findViewById(R.id.insta_tag);
        Glide.with(this).load(photoToBeTaggedUri)
                .centerCrop()
                .placeholder(0)
                .fallback(0)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(instaTag.getInstaTagImageView());
    }
}
