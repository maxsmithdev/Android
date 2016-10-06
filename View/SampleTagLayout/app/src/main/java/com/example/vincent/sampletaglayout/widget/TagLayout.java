package com.example.vincent.sampletaglayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vincent on 1/7/2016.
 */
public class TagLayout extends ViewGroup {

    private ArrayList<View> mViews;

    public TagLayout(Context context) {
        super(context);
        init();
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setWillNotDraw(false);
    }

    public void add(View view){
        mViews.add(view);
        invalidate();
    }

    public void create(View... views){
        mViews = new ArrayList<>(Arrays.asList(views));
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int childCount = getChildCount();
        final int childTop = getPaddingTop();
        final int childBottom = getMeasuredHeight() - getPaddingBottom();
        final int childLeft = getPaddingLeft();
        final int childRight = getMeasuredWidth() - getPaddingRight();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        if(childCount > 0) {
            for(int i=0;i<childCount;i++){
                View child = getChildAt(i);

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
