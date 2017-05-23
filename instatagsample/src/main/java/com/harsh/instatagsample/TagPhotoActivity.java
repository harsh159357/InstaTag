package com.harsh.instatagsample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.harsh.instatag.SquareImageView;

import java.io.File;
//import com.squareup.picasso.Picasso;

public class TagPhotoActivity extends AppCompatActivity {

    private SquareImageView photoToBeTaggedImageView;
    private Uri photoToBeTaggedUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_photo);
        photoToBeTaggedUri = getIntent().getData();
        photoToBeTaggedImageView = (SquareImageView) findViewById(R.id.photo_to_be_tagged);
        Glide.with(this).load(photoToBeTaggedUri)
                .centerCrop()
                .placeholder(0)
                .fallback(0)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoToBeTaggedImageView);
//        Picasso.with(this).load(photoToBeTaggedUri).into(photoToBeTaggedImageView);
    }
}
