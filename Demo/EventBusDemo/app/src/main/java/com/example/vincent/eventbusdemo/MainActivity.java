package com.example.vincent.eventbusdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.vincent.eventbusdemo.model.FailureError;
import com.example.vincent.eventbusdemo.model.ShotAndUser;
import com.example.vincent.eventbusdemo.model.User;
import com.example.vincent.eventbusdemo.utils.RestApi;
import com.example.vincent.eventbusdemo.model.Shots;
import com.example.vincent.retrofitandeventbus.R;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Shots> mShots = new ArrayList<>();
    private User mUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RestApi.register(this);

        mShots = Shots.getAll(this);
        mUser = ShotAndUser.getUser(this);
        if(mUser == null){
            RestApi.getShotAndUser();
        }else{
            Log.i(TAG, "User : " + mUser.username);
        }

        if(mShots == null){
            RestApi.getShots();
        }else{
            Log.i(TAG, "Shots : " + mShots.size());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        RestApi.unregister(this);
    }

    /*
     * @Override
     * protected void onHandleIntent(Intent intent) {
     *   EventBus.getDefault().post(new IntentServiceResult(Activity.RESULT_OK, "done!!"));
     * }
     * Because the IntentService is executed on a separate thread,
     * @Subscribe(threadMode = ThreadMode.MAIN)
     * public void doThis(IntentServiceResult intentServiceResult) {
     *   Toast.makeText(this, intentServiceResult.getResultValue(), Toast.LENGTH_SHORT).show();
     * }
     */
    @Subscribe
    public void onEvent(ArrayList<Shots> shots){
        Shots.save(this, shots);
        for(Shots shot : shots){
            Log.i(TAG, "Shots User : " + shot.user.username);
        }
    }

    @Subscribe
    public void onEvent(ShotAndUser shotAndUser){
        ShotAndUser.save(this, shotAndUser.shots, shotAndUser.user);
        for(Shots shot : shotAndUser.shots){
            Log.i(TAG, "Subscribe Shots : " + shot.user.username);
        }
    }

    @Subscribe
    public void onEvent(FailureError error){
        Log.e(TAG, "FailureError " + error.toString());
    }
}
