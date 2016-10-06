package com.example.vincent.samplehttpnetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.vincent.samplehttpnetwork.tools.HttpManager;
import com.example.vincent.samplehttpnetwork.tools.HttpTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mStartBtn, mStopBtn;
    private TextView mResponse;
    private HttpTask mHttpTask;
    private String mUri = "https://api.dribbble.com/v1/shots?access_token=06428b459e81637505938870033865c924800dc8cf711e5e5194ce26c6f00df8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartBtn = (Button)findViewById(R.id.start);
        mStopBtn = (Button)findViewById(R.id.stop);

        mStartBtn.setOnClickListener(this);
        mStopBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                mHttpTask = HttpManager.get(mUri);
                break;
            case R.id.stop:
                //HttpManager.cancel(_httpTask, _uri);
                HttpManager.stop();
                break;
        }
    }
}
