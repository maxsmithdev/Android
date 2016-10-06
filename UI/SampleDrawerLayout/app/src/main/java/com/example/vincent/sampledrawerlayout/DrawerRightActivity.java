package com.example.vincent.sampledrawerlayout;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by vincent on 28/6/2016.
 */
public class DrawerRightActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener{

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.rightmenu:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
        }
        return false;
    }
}
