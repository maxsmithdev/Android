package com.example.vincent.samplerecyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vincent.samplerecyclerview.activity.DefaultActivity;
import com.example.vincent.samplerecyclerview.activity.GalleryActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mDefaultBtn, mGalleryListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDefaultBtn = (Button)findViewById(R.id.button_default);
        mGalleryListBtn = (Button)findViewById(R.id.button_gallery);

        mDefaultBtn.setOnClickListener(this);
        mGalleryListBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_default:
                startActivity(new Intent(this, DefaultActivity.class));
                break;
            case R.id.button_gallery:
                startActivity(new Intent(this, GalleryActivity.class));
                break;
        }
    }
}
