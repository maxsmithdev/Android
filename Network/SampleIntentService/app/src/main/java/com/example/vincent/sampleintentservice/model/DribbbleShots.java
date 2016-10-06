package com.example.vincent.sampleintentservice.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vincent on 26/6/2016.
 */
public class DribbbleShots implements Serializable {

    public int id;
    public String title;
    public String description;
    public Images images;
    public int views_count;

    public static class Images implements Serializable{
        public String hidpi;
        public String normal;
        public String teaser;
    }
}
