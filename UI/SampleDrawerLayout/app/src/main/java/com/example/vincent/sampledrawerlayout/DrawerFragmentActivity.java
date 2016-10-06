package com.example.vincent.sampledrawerlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;

/**
 * Created by vincent on 28/6/2016.
 */
public class DrawerFragmentActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
    }
}
