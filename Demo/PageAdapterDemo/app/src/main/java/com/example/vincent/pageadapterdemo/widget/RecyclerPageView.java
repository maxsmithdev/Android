package com.example.vincent.pageadapterdemo.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by vincent on 29/7/2016.
 */

public class RecyclerPageView extends FrameLayout implements View.OnClickListener{

    private static final String TAG = RecyclerPageView.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private Button mRetryBtn;
    private ProgressBar mProgressBar;

    private RecyclerView.Adapter mAdapter;

    private Object mRetryObject;
    private OnRetryListener mOnRetryListner;

    @Override
    public void onClick(View view) {
        if(mOnRetryListner != null) mOnRetryListner.onRetry(mRetryObject);
    }

    public interface OnRetryListener<T> {
        void onRetry(T retry);
    }

    public static class Retry<T> implements Serializable{
        public T params;
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            Log.i(TAG, "AdapterDataObserver " + (mAdapter != null));
            if (mAdapter != null) {
                if (mAdapter.getItemCount() > 0) {
                    mProgressBar.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);
                    mRetryBtn.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    showEmpty();
                }
            }
        }

    };

    public RecyclerPageView(Context context) {
        super(context);
        initialized(context);
    }

    public RecyclerPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialized(context);
    }

    private void initialized(Context context){
        setWillNotDraw(false);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;

        mEmptyView = new TextView(context);
        mEmptyView.setText("No data current available.");
        addView(mEmptyView, lp);

        mProgressBar = new ProgressBar(context);
        addView(mProgressBar, lp);

        mRetryBtn = new Button(context);
        mRetryBtn.setText("Retry");
        mRetryBtn.setOnClickListener(this);
        addView(mRetryBtn, lp);

        mRecyclerView = new RecyclerView(context);
        addView(mRecyclerView, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        showLoading();
    }

    public <T> void setOnRetryListener(OnRetryListener listener, T retry){
        mRetryObject = retry;
        mOnRetryListner = listener;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layouerManager){
        mRecyclerView.setLayoutManager(layouerManager);
    }

    public RecyclerView.Adapter getAdapter(){
        synchronized (RecyclerPageView.class) {
            return mAdapter;
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        final RecyclerView.Adapter oldAdapter = mRecyclerView.getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);

        if(adapter != null){
            mAdapter.registerAdapterDataObserver(observer);
        }

        observer.onChanged();
    }

    public void showEmpty(){
        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mRetryBtn.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    public void showRetry(){
        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mRetryBtn.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mRetryBtn.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }
}
