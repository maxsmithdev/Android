package com.example.vincent.fragmentmanagerinstance.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.vincent.fragmentmanagerinstance.listener.OnFragmentInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by vincent on 19/7/2016.
 */

public class FragmentHelper {

    private final static String TAG = FragmentHelper.class.getSimpleName();

    private boolean mIsRetain;
    private FragmentObserver mObserver = new FragmentObserver();
    private FragmentManager mFragmentManager;

    public FragmentHelper(FragmentManager fm, FragmentObserver observer){
        mFragmentManager = fm;
        mObserver = observer;
    }

    public FragmentObserver getObserver(){
        synchronized (FragmentObserver.class){
            return mObserver;
        }
    }

    public void setRetain(boolean isKeep){
        mIsRetain = isKeep;
    }

    public void addFragment(int resId, int position, Fragment fragment, String tag){
        ArrayList<String> tags = mObserver.mTags.get(position);
        if (tags == null){
            tags = new ArrayList<>();
        }

        final FragmentTransaction ft = mFragmentManager.beginTransaction();
        final Fragment existFragment = mFragmentManager.findFragmentByTag(tag);
        final Fragment currentFragment = mFragmentManager.findFragmentByTag(mObserver.mCurrentTag);
        if (currentFragment != null){
            Log.i(TAG, "Detach TAG ["+mObserver.mCurrentTag+"]");
            if(mIsRetain){
                if(currentFragment instanceof OnFragmentInterface) {
                    OnFragmentInterface onFragmentInterface = (OnFragmentInterface)currentFragment;
                    onFragmentInterface.onFragmentPause();
                }
            }else{
                ft.detach(currentFragment);
            }
        }

        if(existFragment == null){
            ft.add(resId, fragment, tag);
        }else{
            Log.i(TAG, "exists fragment");
            ft.attach(fragment);
        }

        ft.commit();
        tags.add(tag);

        mObserver.mCurrentPosition = position;
        mObserver.mCurrentTag = tag;
        mObserver.mIndex.add(position);
        mObserver.mTags.put(position, tags);

        Log.i(TAG, "Tags : " + Arrays.toString(mObserver.mTags.get(position).toArray()));
    }

    public boolean pop(){
        ArrayList<String> tags = mObserver.mTags.get(mObserver.mCurrentPosition);

        if (tags != null){
            if(tags.size() >= 1){
                int lastIndex = tags.size() - 1; // get current index
                tags.remove(lastIndex); // remove current tag in observer
                int prevIndex = tags.size() - 1; // get current index

                if(lastIndex > -1) {
                    String prevTag = tags.get(prevIndex);
                    Log.i(TAG, "Before Index " + prevIndex);

                    final FragmentTransaction ft = mFragmentManager.beginTransaction();
                    final Fragment currentFragment = mFragmentManager.findFragmentByTag(mObserver.mCurrentTag);
                    final Fragment prevFragment = mFragmentManager.findFragmentByTag(prevTag);

                    // unchecked : it may be call same tag with current fragment.
                    if (prevFragment != null) {
                        Log.i(TAG, "Attach TAG ["+prevTag+"]");
                        if(mIsRetain){
                            if(currentFragment instanceof OnFragmentInterface){
                                OnFragmentInterface onFragmentInterface = (OnFragmentInterface)currentFragment;
                                onFragmentInterface.onFragmentResume();
                            }
                        }else {
                            ft.attach(prevFragment);
                        }
                    }

                    if(currentFragment != null){
                        Log.i(TAG, "Remove TAG ["+mObserver.mCurrentTag+"]");
                        ft.remove(currentFragment);
                    }

//                    if(next){
//                        mObserver.mCurrentPosition = mObserver.mIndex.iterator().next();
//                        tags = mObserver.mTags.get(mObserver.mCurrentPosition);
//                        lastIndex = tags.size() - 1;
//                        lastTag = tags.get(lastIndex);
//                    }

                    ft.commit();

                    mObserver.mCurrentTag = prevTag;
                    mObserver.mTags.put(mObserver.mCurrentPosition, tags);
                }

                Log.i(TAG, "Tags : " + Arrays.toString(tags.toArray()));
                Log.i(TAG, "Current Position " + mObserver.mCurrentPosition);

                if(lastIndex <= 0){

                }

//                if(lastIndex <= 1){
//                    int lastPoint = mObserver.mIndex.size() - 1;
//                    if(lastPoint <= 1) {
//                        mObserver.mCurrentPosition = mObserver.mIndex.get(lastPoint);
//                    }
//                }

                //return lastIndex <= 0;
            }
        }

        return false;
    }

    public void onResume(){
        ArrayList<String> tags = mObserver.mTags.get(mObserver.mCurrentPosition);
        final FragmentTransaction ft = mFragmentManager.beginTransaction();

        int lastIndex = tags.size() - 1;
        if(lastIndex > -1) {
            String lastTag = tags.get(lastIndex);
            final Fragment currentFragment = mFragmentManager.findFragmentByTag(lastTag);

            if (currentFragment != null) {
                ft.attach(currentFragment);
            }

            ft.commit();
        }
    }

    public static class FragmentObserver implements Serializable {

        public ArrayList<Integer> mIndex = new ArrayList<>();
        public HashMap<Integer, ArrayList<String>> mTags = new HashMap<>();
        public int mCurrentPosition = 0;
        public String mCurrentTag = null;

    }

}
