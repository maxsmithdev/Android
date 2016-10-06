package com.example.vincent.sampledrawerlayout;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mLeftBtn, mRightBtn, mFrgMenu, mNavMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLeftBtn = (Button)findViewById(R.id.leftmenu);
        mRightBtn = (Button)findViewById(R.id.rightmenu);
        mFrgMenu = (Button)findViewById(R.id.frgmenu);
        mNavMenu = (Button)findViewById(R.id.navmenu);

        mLeftBtn.setOnClickListener(this);
        mRightBtn.setOnClickListener(this);
        mFrgMenu.setOnClickListener(this);
        mNavMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftmenu:
                startActivity(new Intent(this, DrawerLeftActivity.class));
                break;
            case R.id.rightmenu:
                startActivity(new Intent(this, DrawerRightActivity.class));
                break;
            case R.id.frgmenu:
                startActivity(new Intent(this, DrawerFragmentActivity.class));
                break;
            case R.id.navmenu:
                startActivity(new Intent(this, DrawerNavActivity.class));
                break;
        }
    }
}
