package com.example.vincent.complexrecyclerview.classes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.vincent.complexrecyclerview.adapter.MultiGalleryAdapter;

/**
 * Created by vincent on 29/7/2016.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration{

    private RecyclerView.Adapter mAdapter;
    private Drawable mDivider;

    public GridItemDecoration(Context context, RecyclerView.Adapter adapter){
        mAdapter = adapter;

        TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        int viewType = mAdapter.getItemViewType(position);
        switch (viewType){
            case MultiGalleryAdapter.TYPE_GRID:
                outRect.left = spanIndex == 1 ? 2 : 10;
                outRect.right = spanIndex == 0 ? 2 : 10;
                outRect.top = 5;
                outRect.bottom = 5;
                break;
        }
    }
}
