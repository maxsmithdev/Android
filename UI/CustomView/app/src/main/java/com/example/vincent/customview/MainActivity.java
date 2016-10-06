package com.example.vincent.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mSampleBtn, mTouchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSampleBtn = (Button)findViewById(R.id.button_sample);
        mTouchBtn = (Button)findViewById(R.id.button_touch);

        mSampleBtn.setOnClickListener(this);
        mTouchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_sample:
                startActivity(new Intent(this, SampleActivity.class));
                break;
            case R.id.button_touch:
                startActivity(new Intent(this, TocuhActivity.class));
                break;
        }
    }
}
