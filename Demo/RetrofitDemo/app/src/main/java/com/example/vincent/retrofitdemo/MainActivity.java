package com.example.vincent.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.vincent.retrofitdemo.listener.DribbbleInterface;
import com.example.vincent.retrofitdemo.model.ShotAndUser;
import com.example.vincent.retrofitdemo.model.Shots;
import com.example.vincent.retrofitdemo.model.User;
import com.example.vincent.retrofitdemo.tools.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = MainActivity.class.getSimpleName();
    private Button mSingleBtn;
    private Button mMultipleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSingleBtn = (Button)findViewById(R.id.button_single);
        mMultipleBtn = (Button)findViewById(R.id.button_multiple);
        mSingleBtn.setOnClickListener(this);
        mMultipleBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_single:
                DribbbleInterface dribbbleSync = DribbbleInterface.retrofit.create(DribbbleInterface.class);
                Call<List<Shots>> call = dribbbleSync.getShots(Constants.TOKEN);
                call.enqueue(new Callback<List<Shots>>() {
                    @Override
                    public void onResponse(Call<List<Shots>> call, Response<List<Shots>> response) {
                        List<Shots> shots = response.body();
                        for(Shots shot : shots){
                            Log.i(TAG, "User : " + shot.user.username);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Shots>> call, Throwable t) {
                        Log.e(TAG, "Error : " + t.getMessage());
                    }
                });
                break;
            case R.id.button_multiple:

                Observable<List<Shots>> dribbble1 = DribbbleInterface.retrofit
                        .create(DribbbleInterface.class)
                        .getObservableShots(Constants.TOKEN)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());

                Observable<User> dribbble2 = DribbbleInterface.retrofit
                        .create(DribbbleInterface.class)
                        .getObservableUser(Constants.TOKEN)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());

                Observable<ShotAndUser> combined = Observable.zip(
                        dribbble1,
                        dribbble2,
                        new Func2<List<Shots>, User, ShotAndUser>() {

                            @Override
                            public ShotAndUser call(List<Shots> shotses, User user) {
                                return new ShotAndUser(shotses, user);
                            }
                });

                combined.subscribe(new Subscriber<ShotAndUser>() {

                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Error : " + e.getMessage());
                    }

                    @Override
                    public void onNext(ShotAndUser shotAndUser) {
                        List<Shots> shots = shotAndUser.shots;
                        User user = shotAndUser.user;
                        for(Shots shot : shots){
                            Log.i(TAG, "User : " + shot.user.username);
                        }

                        Log.i(TAG, "===========");
                        Log.i(TAG, "User : " + user.username);

                    }
                });

                break;
        }
    }
}
