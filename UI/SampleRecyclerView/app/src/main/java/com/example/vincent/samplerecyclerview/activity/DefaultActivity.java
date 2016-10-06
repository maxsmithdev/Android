package com.example.vincent.samplerecyclerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.vincent.samplerecyclerview.R;
import com.example.vincent.samplerecyclerview.adapter.DefaultAdapter;

/**
 * Created by vincent on 4/7/2016.
 */

public class DefaultActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setAdapter(new DefaultAdapter(new String[]{"Australia", "Canada", "France", "Germany", "Hong Kong", "India", "Japan", "Taiwan"}));

    }
}
