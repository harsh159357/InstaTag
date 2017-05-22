package com.harsh.instatagsample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.harsh.instatag.SquareImageView;
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
//        Picasso.with(this).load(photoToBeTaggedUri).into(photoToBeTaggedImageView);
    }
}
