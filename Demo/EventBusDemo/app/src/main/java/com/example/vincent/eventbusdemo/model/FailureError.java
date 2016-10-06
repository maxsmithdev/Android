package com.example.vincent.eventbusdemo.model;

/**
 * Created by vincent on 27/7/2016.
 */

public class FailureError {

    public int code = -1;
    public String message;

    public FailureError(int code, String message){
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "[Code : "+code+", Message : "+message+"]";
    }
}
