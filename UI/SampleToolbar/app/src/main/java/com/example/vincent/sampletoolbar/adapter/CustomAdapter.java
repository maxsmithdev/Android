package com.example.vincent.sampletoolbar.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vincent.sampletoolbar.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vincent on 3/7/2016.
 */

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<String> mData = new ArrayList<>();

    public void setData(String... data){
        mData = new ArrayList<>(Arrays.asList(data));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_title, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(holder instanceof CustomViewHolder){
            CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            customViewHolder.title.setText(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private static class CustomViewHolder extends ViewHolder{

        private TextView title;

        public CustomViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);

        }
    }

}
