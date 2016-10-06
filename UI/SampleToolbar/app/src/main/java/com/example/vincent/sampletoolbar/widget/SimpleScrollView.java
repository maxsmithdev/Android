package com.example.vincent.sampletoolbar.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by vincent on 3/7/2016.
 */

public class SimpleScrollView extends ScrollView {

    private final String TAG = SimpleScrollView.class.getSimpleName();

    private OnSimpleScrollListener mListener;
    public interface OnSimpleScrollListener{
        void onScrollChanged(ScrollView scrollView, int left, int top, int oldl, int oldt);
    }

    public SimpleScrollView(Context context) {
        super(context);
    }

    public SimpleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnSimpleScrollListener(OnSimpleScrollListener l){
        mListener = l;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mListener != null) mListener.onScrollChanged(this, l, t, oldl, oldt);
    }



}
