package com.example.vincent.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vincent on 31/7/2016.
 */

public class SampleViewGroup extends ViewGroup {

    private static final String TAG = SampleViewGroup.class.getSimpleName();
    private int mCurChildTop = 0;

    public SampleViewGroup(Context context) {
        super(context);
    }

    public SampleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

//        int defWidth = 100;
//        int defHeight = 100;
//
//        int count = getChildCount();
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//
//        switch (widthMode){
//            // can't bigger than parent size
//            case MeasureSpec.AT_MOST:
//                width = Math.min(defWidth, width);
//                break;
//            case MeasureSpec.EXACTLY:
//                width = defWidth;
//                break;
//            // no limit
//            case MeasureSpec.UNSPECIFIED:
//                // any size
//                break;
//        }
//
//        switch (heightMode){
//            case MeasureSpec.AT_MOST:
//                height = Math.min(defHeight, height);
//                break;
//            case MeasureSpec.EXACTLY:
//                height = defHeight;
//                break;
//            case MeasureSpec.UNSPECIFIED:
//                break;
//        }
//
//        for(int i=0;i<count;i++){
//            final View child = getChildAt(i);
//            // Measure the child.
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            child.measure(widthMeasureSpec, heightMeasureSpec);
//        }
//
//        // must be call to notify parent measure size
//        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int viewGroupWidth  = getMeasuredWidth();
        int mPainterPosX = left;
        int mPainterPosY = top;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);

            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();

            if (mPainterPosX + width > viewGroupWidth) {
                mPainterPosX = left;
                mPainterPosY += height;
            }

            childView.layout(mPainterPosX, mPainterPosY, mPainterPosX + width, mPainterPosY + height);
            mPainterPosX += width;
        }

//        final int childTop = getPaddingTop();
//        final int childLeft = getPaddingLeft();
//        final int childBottom = getMeasuredHeight() - getPaddingBottom();
//        final int childRight = getMeasuredWidth() - getPaddingLeft();
//        final int childWidth = childRight - childLeft;
//        final int childHeight = childBottom - childTop;
//
//        int height = getMeasuredHeight();
//        int width = getMeasuredWidth();
//        int curTop = childTop, curLeft = childLeft;
//        int curWidth = 0, curHeight = 0;
//
//        //getChildAt(0).layout(childLeft, childTop, childRight, childBottom);
//        //getChildAt(1).layout(childLeft, childTop, childRight, childBottom);
//
//        for(int i= 0;i<getChildCount();i++){
//            final View child = getChildAt(i);
//            //Get the maximum size of the child
//            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
//                    MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
//            curWidth = child.getMeasuredWidth();
//            curHeight = child.getMeasuredHeight();
//
//            Log.i(TAG, "Child "+i+" [top : "+mCurChildTop+", left : "+childLeft+", bottom : "+childBottom+". right : "+childRight+"]");
//            child.layout(childLeft, mCurChildTop, childRight, childBottom + curHeight);
//            mCurChildTop += (childBottom + curHeight);
//        }
    }
}
