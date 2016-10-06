package com.example.vincent.samplehttpnetwork.tools;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vincent on 25/6/2016.
 */
public class HttpRunnable implements Runnable {

    private final String TAG = HttpRunnable.class.getSimpleName();
    private HttpImpl mHttpImpl;
    public interface HttpImpl{
        void setMethod(String method);
        void setUri(String uri);
        void setParams(Bundle params);
        void setResponse(String data);
        void handleHttpState(int state);
        void setCurrentThread(Thread thread);

        String getMethod();
        String getUri();
        Bundle getParams();
        String getResponse();
        Thread getCurrentThread();
    }

    public HttpRunnable(HttpImpl httpImpl){
        mHttpImpl = httpImpl;
    }

    @Override
    public void run() {

        mHttpImpl.setCurrentThread(Thread.currentThread());
        // Moves the current Thread into the background
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        mHttpImpl.handleHttpState(HttpTask.HTTP_START);

        try {
            Thread.sleep(5000);
            String response = getResponse(mHttpImpl.getMethod(), mHttpImpl.getUri(), mHttpImpl.getParams());
            mHttpImpl.setResponse(response);
            mHttpImpl.handleHttpState(HttpTask.HTTP_COMPLETE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            mHttpImpl.handleHttpState(HttpTask.HTTP_CANCEL);
        }

    }

    private String getResponse(String method, String uri, Bundle params) throws IOException, InterruptedException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int contentLength = 500;
        String response = null;

        try {
            if(Thread.interrupted()){
                throw new InterruptedException();
            }

            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int state = conn.getResponseCode();
            Log.d(TAG, "The response is: " + state);
            if(state != 200){
                mHttpImpl.handleHttpState(HttpTask.HTTP_ERROR);
            }

            if(Thread.interrupted()){
                throw new InterruptedException();
            }

            is = conn.getInputStream();

            // Convert the InputStream into a string
            response = readContent(is, contentLength);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if(null != is) is.close();
        }

        return response;
    }

    // Reads an InputStream and converts it to a String.
    private String readContent(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }
}
