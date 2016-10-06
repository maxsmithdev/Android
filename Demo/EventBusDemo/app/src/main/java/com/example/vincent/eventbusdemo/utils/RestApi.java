package com.example.vincent.eventbusdemo.utils;

import android.content.Context;
import android.util.Log;

import com.example.vincent.eventbusdemo.model.FailureError;
import com.example.vincent.eventbusdemo.model.ShotAndUser;
import com.example.vincent.eventbusdemo.model.Shots;
import com.example.vincent.eventbusdemo.model.User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by vincent on 27/7/2016.
 */

public class RestApi {

    private static final String TAG = RestApi.class.getSimpleName();
    public static final String API_URL = "https://api.dribbble.com/v1/";
    public static final String TOKEN = "06428b459e81637505938870033865c924800dc8cf711e5e5194ce26c6f00df8";

    public static final EventBus mEventBus = EventBus.getDefault();
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(new OkHttpClient.Builder().build())
            .build();

    public interface RestApiInterface{
        @GET("shots")
        Call<ArrayList<Shots>> getShots(@Query("access_token") String token);

        @GET("user")
        Call<User> getUser(@Query("access_token") String token);

        @GET("shots")
        Observable<ArrayList<Shots>> getObservableShots(@Query("access_token") String token);

        @GET("user")
        Observable<User> getObservableUser(@Query("access_token") String token);
    }

    public static void register(Context context){
        mEventBus.register(context);
    }

    public static void unregister(Context context){
        mEventBus.unregister(context);
    }

    public static void getShots(){
        Call<ArrayList<Shots>> call = retrofit.create(RestApiInterface.class).getShots(TOKEN);
        call.enqueue(new Callback<ArrayList<Shots>>() {
            @Override
            public void onResponse(Call<ArrayList<Shots>> call, Response<ArrayList<Shots>> response) {
                ArrayList<Shots> shots = response.body();
                mEventBus.post(shots);
            }

            @Override
            public void onFailure(Call<ArrayList<Shots>> call, Throwable t) {
                Log.e(TAG, "FailureError : " + t.getMessage());
                mEventBus.post(new FailureError(404, "Not Found."));
            }
        });
    }

    public static void getShotAndUser(){
        Observable<ArrayList<Shots>> shotCall = retrofit.create(RestApiInterface.class).getObservableShots(TOKEN)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
        Observable<User> userCall = retrofit.create(RestApiInterface.class).getObservableUser(TOKEN)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<ShotAndUser> combined = Observable.zip(
                shotCall,
                userCall,
                new Func2<ArrayList<Shots>, User, ShotAndUser>() {

                    @Override
                    public ShotAndUser call(ArrayList<Shots> shotses, User user) {
                        return new ShotAndUser(shotses, user);
                    }
                });

        combined.subscribe(new Subscriber<ShotAndUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mEventBus.post(new FailureError(404, "Not Found."));
            }

            @Override
            public void onNext(ShotAndUser shotAndUser) {
                mEventBus.post(shotAndUser);
            }
        });
    }
}
