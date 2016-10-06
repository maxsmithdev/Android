package com.example.vincent.complexrecyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vincent.complexrecyclerview.R;

/**
 * Created by vincent on 29/7/2016.
 */

public class HorizontalAdapter extends RecyclerView.Adapter {

    private int[] mColors = new int[]{Color.BLUE, Color.RED, Color.BLACK, Color.YELLOW};
    private Context mContext;

    public HorizontalAdapter(Context context){
        mContext = context;
    }

    private Context getContext(){
        return mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof GridViewHolder){
            GridViewHolder gridViewHolder = (GridViewHolder)holder;
            gridViewHolder.gridImageView.setBackgroundColor(mColors[position]);
        }
    }

    @Override
    public int getItemCount() {
        return mColors.length;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{

        private ImageView gridImageView;

        public GridViewHolder(View itemView) {
            super(itemView);
            gridImageView = (ImageView)itemView.findViewById(R.id.grid);
        }
    }
}
