package com.example.vincent.sampletoolbar;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.example.vincent.sampletoolbar.widget.SimpleScrollView;

/**
 * Created by vincent on 3/7/2016.
 */

public class ScrollViewActivity extends AppCompatActivity {

    private final String TAG = ScrollViewActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private SimpleScrollView mScrollview;
    private View mHeaderView;
    private Drawable mToolbarDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mScrollview = (SimpleScrollView)findViewById(R.id.scrollview);
        mHeaderView = (View)findViewById(R.id.header);

        mToolbarDrawable = mToolbar.getBackground();
        mToolbar.getBackground().setAlpha(0);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mScrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {

                private boolean isVisible;

                @Override
                public void onScrollChange(View view, int l, int t, int oldl, int oldt) {
                    Log.i(TAG, "Scroll API M [Left "+l+", Top "+t+", Old Left "+oldl+", Old Top "+oldt+"]");
                }
            });
        }else{
            mScrollview.setOnSimpleScrollListener(new SimpleScrollView.OnSimpleScrollListener() {

                @Override
                public void onScrollChanged(ScrollView scrollView, int left, int top, int oldl, int oldt) {
                    setToolbarAlpha(top);
                    Log.i(TAG, "Scroll API 8 [Left "+left+", Top "+top+", Old Left "+oldl+", Old Top "+oldt+", ScrollY " +scrollView.getScrollY()+ "]");
                }

                private void setToolbarAlpha(int scrollY){
                    int currentHeaderHeight = mHeaderView.getHeight();
                    //if (currentHeaderHeight != mLastHeaderHeight) {
                        //updateHeaderHeight(currentHeaderHeight);
                    //}

                    int headerHeight = currentHeaderHeight - mToolbar.getHeight();
                    float ratio = (float) Math.min(Math.max(scrollY, 0), headerHeight) / headerHeight;
                    int newAlpha = (int) (ratio * 255);
                    mToolbarDrawable.setAlpha(newAlpha);
                }
            });

            //_scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            //    @Override
            //    public void onScrollChanged() {
            //    }
            //});
        }
    }


}
