package com.example.vincent.sampledrawerlayout;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by vincent on 29/6/2016.
 */
public class DrawerNavActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_menu);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });


        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        mNavigationView = (NavigationView)findViewById(R.id.navigationview);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.settings:
                        Toast.makeText(DrawerNavActivity.this, "Navigation Item Click!", Toast.LENGTH_SHORT).show();
                        break;
                }

                mDrawerLayout.closeDrawers();

                return false;
            }
        });
    }
}
