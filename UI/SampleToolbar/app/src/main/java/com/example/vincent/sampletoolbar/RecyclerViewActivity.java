package com.example.vincent.sampletoolbar;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.vincent.sampletoolbar.adapter.CustomAdapter;
import com.example.vincent.sampletoolbar.tools.UI;

/**
 * Created by vincent on 3/7/2016.
 */

public class RecyclerViewActivity extends AppCompatActivity {

    private final String TAG = RecyclerViewActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private LinearLayout mLayoutToolbar;
    private View mLayoutStatusBar;
    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));

            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mLayoutToolbar = (LinearLayout) findViewById(R.id.layout_toolbar);
        mLayoutStatusBar = (View) findViewById(R.id.layout_statusbar);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mLayoutToolbar.setAlpha(0);

        ViewGroup.LayoutParams lp = mLayoutStatusBar.getLayoutParams();
        lp.height = UI.getStatusBarHeight(this);
        mLayoutStatusBar.setLayoutParams(lp);

        mLinearLayoutManger = new LinearLayoutManager(this);
        mLinearLayoutManger.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManger);

        mAdapter = new CustomAdapter();
        mAdapter.setData(new String[]{"A","B","C","D","E","F","G"});
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            int scrollY = 0;
            int deltaY = 0;
            float alpha = 255f;
            int position = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if(layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
                    int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    final View firstChild = linearLayoutManager.getChildAt(0);
                    final View nextChild = linearLayoutManager.getChildAt(1);
                    scrollY = (firstChild == null) ? 0 : firstChild.getTop();

                    if (scrollY < 0 && nextChild != null) {
                        scrollY = nextChild.getTop();
                        position++;
                    }

                    if (dy > 0) {
                        deltaY += dy;
                    } else {
                        deltaY -= Math.abs(dy);
                    }

                    int height = mLayoutToolbar.getHeight();
                    Log.i(TAG, "[height : "+height+", deltaY : "+deltaY+"]");
                    if(height > 0) {
                        if(deltaY < height) {
                            mLayoutToolbar.setTranslationY(-deltaY);
                        }

                        float ratio = (float)(Math.floor(deltaY) / Math.floor(height));
                        Log.i(TAG, "[ratio : "+ratio+" ]");
                        mLayoutToolbar.setAlpha(ratio);
                    }
                }
            }
        });
    }
}
