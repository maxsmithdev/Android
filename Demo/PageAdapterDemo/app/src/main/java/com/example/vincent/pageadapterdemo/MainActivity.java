package com.example.vincent.pageadapterdemo;

import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.vincent.pageadapterdemo.adapter.GridAdapter;
import com.example.vincent.pageadapterdemo.widget.RecyclerPageView;
import com.example.vincent.pageadapterdemo.widget.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private Handler mHandler = new Handler();
    private ArrayMap<Integer, Runnable> mWorkers = new ArrayMap<>();
    private PagerAdapter mPageAdapter = new PagerAdapter() {

        private String[] mTitles = new String[]{"Tab 1", "Tab 2", "Tab 3", "Tab 4", "Tab 5"};

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i(TAG, "instantiateItem : " + position);

            final RecyclerPageView recyclerPageView = new RecyclerPageView(MainActivity.this);
            //final ArrayList<String> retry = new ArrayList<String>();
            //retry.add("Params 1");
            //retry.add("Params 2");

            //final HashMap<String, String> retry = new HashMap<>();
            //retry.put("param_1", "p1");
            //retry.put("param_2", "p2");

            Bundle retry = new Bundle();
            retry.putString("param_1", "p1");
            retry.putString("param_2", "p2");

            final int pagePosition = position;
            recyclerPageView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerPageView.setOnRetryListener(new RecyclerPageView.OnRetryListener<Bundle>() {

                @Override
                public void onRetry(Bundle retry) {
                    Log.i(TAG, "Retry : " + retry.toString());
                    recyclerPageView.showLoading();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "Set Adapter.");
                            recyclerPageView.setAdapter(new GridAdapter(MainActivity.this));
                        }
                    };

                    mHandler.postDelayed(runnable, 2000);
                    mWorkers.put(pagePosition, runnable);
                }

                //@Override
                //public void onRetry(ArrayList<String> retry) {
                //    Log.i(TAG, "Retry : " + retry.toArray().toString());
                //}
            }, retry);

            Runnable oldRunnable = mWorkers.get(position);
            if(mHandler != null){
                mHandler.removeCallbacks(oldRunnable);
            }

            //Runnable runnable = new Runnable() {
            //    @Override
            //    public void run() {
            //        Log.i(TAG, "Set Adapter.");
            //        recyclerPageView.setAdapter(new GridAdapter(MainActivity.this));
            //    }
            //};

            //mHandler.postDelayed(runnable, 2000);
            //mWorkers.put(position, runnable);
            //recyclerPageView.showEmpty();
            recyclerPageView.showRetry();
            container.addView(recyclerPageView);
            return recyclerPageView;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Runnable oldRunnable = mWorkers.get(position);
            if(mHandler != null){
                mHandler.removeCallbacks(oldRunnable);
            }

            container.removeView((RecyclerPageView)object);
        }
    };

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i(TAG, "onPageScrolled : " + positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            Log.i(TAG, "onPageSelected : " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.i(TAG, "onPageScrollStateChanged : " + (ViewPager.SCROLL_STATE_IDLE == state));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mSlidingTabLayout = (SlidingTabLayout)findViewById(R.id.slidingtablayout);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mSlidingTabLayout.setViewPager(mViewPager);
        //mSlidingTabLayout.setDistributeEvenly(true);
    }
}
