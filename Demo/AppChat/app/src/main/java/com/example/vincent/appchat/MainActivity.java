package com.example.vincent.appchat;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.vincent.appchat.utils.Constants;
import com.example.vincent.appchat.listener.AppListener;
import com.example.vincent.appchat.utils.Session;
import com.example.vincent.appchat.view.ChannelFragment;
import com.example.vincent.appchat.view.WelcomeFragment;

public class MainActivity extends AppCompatActivity implements AppListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(Constants.FRAGMENT_WELCOME_TAG) == null){
            fm.beginTransaction().replace(R.id.main_content, WelcomeFragment.getInstance(), Constants.FRAGMENT_WELCOME_TAG).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onChangeFragment() {
        Log.i(TAG, "onChangeFragment");
        if(fm.findFragmentByTag(Constants.FRAGMENT_CHANNEL_TAG) == null){
            fm.beginTransaction().replace(R.id.main_content, ChannelFragment.getInstance(Session.getInstance(this).getId()), Constants.FRAGMENT_CHANNEL_TAG).commit();
        }
    }
}
