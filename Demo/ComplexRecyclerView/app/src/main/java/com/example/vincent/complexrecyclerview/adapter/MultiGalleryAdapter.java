package com.example.vincent.complexrecyclerview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vincent.complexrecyclerview.R;
import com.example.vincent.complexrecyclerview.classes.GridItemDecoration;

/**
 * Created by vincent on 28/7/2016.
 */

public class MultiGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_BANNER = 0;
    public static final int TYPE_LIST = 1;
    public static final int TYPE_GRID = 2;

    private Context mContext;
    private PagerAdapter mPageAdapter = new PagerAdapter() {

        private int[] colors = new int[]{Color.YELLOW, Color.GRAY, Color.RED, Color.BLUE, Color.BLACK};

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundColor(colors[position]);
            container.addView(imageView);
            return imageView;
        }

        // allow redraw all view
        //@Override
        //public int getItemPosition(Object object) {
        //    return POSITION_NONE;
        //}

        @Override
        public int getCount() {
            return colors.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
        }
    };

    public MultiGalleryAdapter(Context context){
        mContext = context;
    }

    private Context getContext(){
        return mContext;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_BANNER : position == 1 ? TYPE_LIST : TYPE_GRID;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == TYPE_BANNER ? new BannerViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_banner_item, parent, false))
                : viewType == TYPE_LIST ? new ListViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_recyclerview, parent, false))
                        : new GridViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.row_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            bannerViewHolder.viewPager.setAdapter(mPageAdapter);

            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) bannerViewHolder.itemView.getLayoutParams();
            lp.setFullSpan(true);
            bannerViewHolder.itemView.setLayoutParams(lp);
        }else if(holder instanceof ListViewHolder){
            ListViewHolder listViewHolder = (ListViewHolder)holder;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            listViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
            listViewHolder.recyclerView.setAdapter(new HorizontalAdapter(getContext()));

            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) listViewHolder.itemView.getLayoutParams();
            lp.setFullSpan(true);
            listViewHolder.itemView.setLayoutParams(lp);
        }else if(holder instanceof GridViewHolder){
            GridViewHolder gridViewHolder = (GridViewHolder)holder;
            gridViewHolder.gridImageView.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder{

        private ViewPager viewPager;

        public BannerViewHolder(View itemView) {
            super(itemView);
            viewPager = (ViewPager)itemView.findViewById(R.id.viewpager);
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView recyclerView;

        public ListViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView)itemView.findViewById(R.id.recyclerview);
        }
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{

        private ImageView gridImageView;

        public GridViewHolder(View itemView) {
            super(itemView);
            gridImageView = (ImageView)itemView.findViewById(R.id.grid);
        }
    }
}
