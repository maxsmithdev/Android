package com.example.vincent.samplerecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vincent.samplerecyclerview.R;

/**
 * Created by vincent on 5/7/2016.
 */

public class DefaultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] mTitles;

    public DefaultAdapter(String... titles){
        mTitles = titles;
    }

    public void setTitles(String... titles){
        mTitles = titles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_title, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TitleViewHolder){
            TitleViewHolder titleViewHolder = (TitleViewHolder)holder;
            titleViewHolder.title.setText(mTitles[position]);
        }
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder{

        private TextView title;

        public TitleViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);

        }
    }
}
