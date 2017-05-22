package com.harsh.instatagsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

public class SomeOnesActivity extends AppCompatActivity implements SomeOneToBeTaggedAdapterClickListener {

    RecyclerView recyclerViewSomeOne;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_some_one);
        recyclerViewSomeOne = (RecyclerView) findViewById(R.id.rv_some_one);
        SomeOneToBeTaggedAdapter someOneToBeTaggedAdapter = new SomeOneToBeTaggedAdapter(SomeOnesData.getDummySomeOneList(), this, this);
        recyclerViewSomeOne.setAdapter(someOneToBeTaggedAdapter);
        recyclerViewSomeOne.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onSomeOneToBeTaggedClick(final SomeOne someOne, int position) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SomeOnesActivity.this, someOne.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
