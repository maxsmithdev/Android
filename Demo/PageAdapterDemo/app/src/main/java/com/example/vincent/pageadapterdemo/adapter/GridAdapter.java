package com.example.vincent.pageadapterdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vincent.pageadapterdemo.R;

/**
 * Created by vincent on 29/7/2016.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> {

    private Context mContext;
    private int mDefaultColor = Color.RED;

    public GridAdapter(Context context){
        mContext = context;
    }

    private Context getContext(){
        return mContext;
    }

    public void setColor(int color){
        mDefaultColor = color;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GridViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        holder.imageView.setBackgroundColor(mDefaultColor);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public GridViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.grid);
        }
    }

}
