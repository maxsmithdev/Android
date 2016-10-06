package com.example.vincent.fragmentmanagerinstance;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vincent.fragmentmanagerinstance.model.FragmentHelper;
import com.example.vincent.fragmentmanagerinstance.model.FragmentHelper.FragmentObserver;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private Button mAddBtn;

    private int mIndex = 0;
    private int mColor = Color.YELLOW;

    private FragmentManager mFragmentManager;
    private FragmentHelper mFragmentHelper;
    private FragmentObserver mFragmentObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            Log.i(TAG, "Found savedInstanceState.");
            mFragmentObserver = (FragmentObserver)savedInstanceState.getSerializable("fragmentObserver");
        }else{
            mFragmentObserver = new FragmentObserver();
        }

        mFragmentManager = getSupportFragmentManager();
        mFragmentHelper = new FragmentHelper(mFragmentManager, mFragmentObserver);

        mAddBtn = (Button)findViewById(R.id.button_add);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        mNavView = (NavigationView)findViewById(R.id.navigationview);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_item_1:
                        mFragmentObserver.mCurrentPosition = 0;
                        mColor = Color.BLUE;
                        mFragmentHelper.addFragment(R.id.main_content, mFragmentObserver.mCurrentPosition, SampleFragment.getInstance(mIndex, mColor), "com.fragment.index." + mIndex);
                        mIndex++;
                        break;
                    case R.id.navigation_item_2:
                        mFragmentObserver.mCurrentPosition = 1;
                        mColor = Color.RED;
                        mFragmentHelper.addFragment(R.id.main_content, mFragmentObserver.mCurrentPosition, SampleFragment.getInstance(mIndex, mColor), "com.fragment.index." + mIndex);
                        mIndex++;
                        break;
                    case R.id.navigation_item_3:
                        mFragmentObserver.mCurrentPosition = 2;
                        mColor = Color.YELLOW;
                        mFragmentHelper.addFragment(R.id.main_content, mFragmentObserver.mCurrentPosition, SampleFragment.getInstance(mIndex, mColor), "com.fragment.index." + mIndex);
                        mIndex++;
                        break;
                }

                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return false;
            }
        });

        if(mFragmentObserver.mTags.size() > 0) {
            mFragmentHelper.onResume();
            Log.i(TAG, "Tags : " + Arrays.toString(mFragmentObserver.mTags.get(mFragmentObserver.mCurrentPosition).toArray()));
        }

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentHelper.addFragment(R.id.main_content, mFragmentObserver.mCurrentPosition, SampleFragment.getInstance(mIndex, mColor), "com.fragment.index." + mIndex);
                mIndex++;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fragmentObserver", mFragmentObserver);
    }

    @Override
    public void onBackPressed() {
        if(mFragmentHelper.pop()){
            super.onBackPressed();
        }
    }

    public static class SampleFragment extends Fragment{

        private final String TAG = SampleFragment.class.getSimpleName();
        private int mIndex = -1;
        private int mColor = Color.YELLOW;

        public static SampleFragment getInstance(int index, int color){
            SampleFragment f = new SampleFragment();
            final Bundle p = new Bundle();
            p.putInt("index", index);
            p.putInt("color", color);
            f.setArguments(p);
            return f;
        }

        @Override
        public void onDetach() {
            super.onDetach();
            Log.i(TAG, "onDetach : " + mIndex);
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.i(TAG, "onPause : " + mIndex);
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.i(TAG, "onStop : " + mIndex);
        }

        @Override
        public void onResume() {
            super.onResume();
            Log.i(TAG, "onResume : " + mIndex);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final Bundle p = getArguments();
            if(p != null){
                mIndex = p.getInt("index", -1);
                mColor = p.getInt("color", -1);
            }

            TextView title = (TextView)rootView.findViewById(R.id.title);
            title.setText("Index " + mIndex);
            rootView.setBackgroundColor(mColor);

            return rootView;
        }
    }
}
