package com.example.vincent.retrofitdemo.model;

import java.util.List;

/**
 * Created by vincent on 18/7/2016.
 */

public class ShotAndUser {

    public List<Shots> shots;
    public User user;

    public ShotAndUser(List<Shots> shots, User user){
        this.shots = shots;
        this.user = user;
    }

}
