package com.dragon4.owo.resight_android.view.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dragon4.owo.resight_android.model.HandMotion;
import com.dragon4.owo.resight_android.R;
import com.dragon4.owo.resight_android.view.adapter.HandMotionAdapter;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

public class HandMotionSelectActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<HandMotion> myDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_motion_select);
        initHandMotionToolBar();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        myDataset = new ArrayList<>();
        mAdapter = new HandMotionAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        myDataset.add(new HandMotion("동작 : 손펴기", R.drawable.icon_hand00_open,"00"));
        myDataset.add(new HandMotion("동작 : 주먹쥐기", R.drawable.icon_hand01_closed,"01"));
        myDataset.add(new HandMotion("동작 : 손가락접기", R.drawable.icon_hand02_finger_closed,"02"));
    }


    private void initHandMotionToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }



}
