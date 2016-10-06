package com.example.vincent.retrofitdemo.model;

/**
 * Created by vincent on 18/7/2016.
 */

public class Shots {

    public String id;
    public String title;
    public String description;
    public int width;
    public int height;

    public Images images;
    public int views_count;
    public User user;


    public class Images{
        public String hidpi;
        public String normal;
        public String teaser;
    }

    public class Links{
        public String web;
        public String twitter;
    }
}
