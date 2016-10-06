package com.example.vincent.complexrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vincent.complexrecyclerview.R;

/**
 * Created by vincent on 28/7/2016.
 */

public class MultiTypeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_TITLE = 0;
    private final int TYPE_CONTENT = 1;

    private Context mContext;

    public MultiTypeViewAdapter(Context context){
        mContext = context;
    }

    private Context getContext(){
        return mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2 == 0 ? TYPE_CONTENT : TYPE_TITLE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_TITLE ?
                new TitleViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_title_item, parent, false)) :
                new ContentViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_content_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TitleViewHolder){
            TitleViewHolder titleViewHolder = (TitleViewHolder)holder;
            titleViewHolder.title.setText(getContext().getString(R.string.sample_title));
        }else if(holder instanceof ContentViewHolder){
            ContentViewHolder contentViewHolder = (ContentViewHolder)holder;
            contentViewHolder.content.setText(getContext().getString(R.string.sample_text));
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder{

        private TextView title;

        public TitleViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder{

        private TextView content;

        public ContentViewHolder(View itemView) {
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.content);
        }
    }
}
