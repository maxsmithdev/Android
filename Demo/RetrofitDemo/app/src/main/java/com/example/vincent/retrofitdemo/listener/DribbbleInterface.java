package com.example.vincent.retrofitdemo.listener;

import com.example.vincent.retrofitdemo.model.Shots;
import com.example.vincent.retrofitdemo.model.User;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by vincent on 18/7/2016.
 */

public interface DribbbleInterface {

    @GET("shots")
    Call<List<Shots>> getShots(@Query("access_token") String token);

    @GET("user")
    Call<User> getUser(@Query("access_token") String token);


    @GET("shots")
    Observable<List<Shots>> getObservableShots(@Query("access_token") String token);

    @GET("user")
    Observable<User> getObservableUser(@Query("access_token") String token);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.dribbble.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(new OkHttpClient.Builder().build())
            .build();

}
