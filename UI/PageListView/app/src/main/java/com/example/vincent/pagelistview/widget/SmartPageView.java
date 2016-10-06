package com.example.vincent.pagelistview.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vincent on 31/7/2016.
 */

public class SmartPageView extends ViewGroup {

    private View mRefreshView;
    private View mLoadMoreView;
    private SmartListView mSmartListView;

    public SmartPageView(Context context) {
        super(context);
    }

    public SmartPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initialize(Context context){
        mSmartListView = new SmartListView(context);
        mSmartListView.setBackgroundColor(Color.LTGRAY);
        addView(mSmartListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //final int height = MeasureSpec.getSize(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }


}
