package com.example.vincent.sampletoolbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mStatusBtn;
    private Button mScrollBtn;
    private Button mRecyclerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBtn = (Button)findViewById(R.id.button_statusbar_trans);
        mScrollBtn = (Button)findViewById(R.id.button_toolbar_scrollview);
        mRecyclerBtn = (Button)findViewById(R.id.button_toolbar_recyclerview);
        mStatusBtn.setOnClickListener(this);
        mScrollBtn.setOnClickListener(this);
        mRecyclerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_statusbar_trans:
                startActivity(new Intent(this, StatusBarActivity.class));
                break;
            case R.id.button_toolbar_scrollview:
                startActivity(new Intent(this, ScrollViewActivity.class));
                break;
            case R.id.button_toolbar_recyclerview:
                startActivity(new Intent(this, RecyclerViewActivity.class));
                break;
        }
    }
}
