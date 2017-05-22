package com.harsh.instatagsample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class InstaActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int CHOOSE_A_PHOTO_TO_BE_TAGGED = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);
        findViewById(R.id.tag_a_photo).setOnClickListener(this);
        findViewById(R.id.see_tagged_photos).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tag_a_photo:
                Intent photoToBeTaggedIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photoToBeTaggedIntent, CHOOSE_A_PHOTO_TO_BE_TAGGED);
                break;
            case R.id.see_tagged_photos:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_A_PHOTO_TO_BE_TAGGED && resultCode == RESULT_OK && data != null) {
            Uri photoToBeTagged = data.getData();
            Intent tagPhotoIntent = new Intent();
            tagPhotoIntent.setClass(this, TagPhotoActivity.class);
            tagPhotoIntent.setData(photoToBeTagged);
            startActivity(tagPhotoIntent);
        }
    }
}
