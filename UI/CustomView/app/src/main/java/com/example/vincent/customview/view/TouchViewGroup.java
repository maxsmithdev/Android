package com.example.vincent.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by vincent on 31/7/2016.
 */

public class TouchViewGroup extends ViewGroup {


    public TouchViewGroup(Context context) {
        super(context);
    }

    public TouchViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
