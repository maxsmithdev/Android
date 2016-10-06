package com.example.vincent.pagelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.vincent.pagelistview.widget.PageListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PageListView mPageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> items = new ArrayList<>();
        for(int i=0;i<500;i++){
            items.add("item " + i);
        }

        mPageListView = (PageListView)findViewById(R.id.pagelistview);
        mPageListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
        mPageListView.showPageRefresh();
    }
}
