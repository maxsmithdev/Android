package com.example.vincent.pagelistview.widget;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.vincent.pagelistview.R;

public class PageListView extends FrameLayout {

    private final String className = getClass().getSimpleName();
    private static enum RefreshState {
        PULL_TO_REFRESH, RELEASE_TO_REFRESH, REFRESHING
    }

    private RefreshState refreshState = RefreshState.PULL_TO_REFRESH;
    private final int TRANSLATE_DURATION_MILLIS = 200;

    private final Interpolator sInterpolator = new AccelerateDecelerateInterpolator();

    private boolean isPullRefreshEnabled;
    private boolean isBeingRefreshDragged;
    private boolean isBeingMoreDragged;

    private boolean tapToBlock = true;
    private boolean tapToLoadMoreEnabled;
    private boolean loading;

    private ProgressBar headerProgress;
    private ProgressBar footerProgress;
    private ProgressBar loadProgress;

    //private CustomBadgeView badgeView;
    //private CustomTabBar optionTabBar;
    private FrameLayout mBottomView;

    private CustomListView listView;
    private BaseAdapter baseAdapter;

    private int pagePosition;
    private int pageScrollY;
    private int currentScrollState;

    private Handler pageTopHandler;
    private Runnable pageTopRunnable = new Runnable(){
        @Override public void run() {
            listView.setSelection(0);
        }
    };

    private Handler pageNewTopHandler;
    private Runnable pageNewTopRunnable = new Runnable(){
        @Override public void run() {
            listView.setSelection(pagePosition);
        }
    };

    private OnPageLoadListener pageLoadListener;

    private int bottomViewHeight;
    public interface OnPageLoadListener {
        void onPageScroll(int position , int scrollY);
        void onPageScrollUp();
        void onPageScrollDown();
        void onPageRefresh();
        void onPageLoadMore();
    }

//	private OnPageClickListener pageClickListener;
//	public interface OnPageClickListener {
//		void onPageViewClick(View view, Bundle bundle);
//		void onPageViewClick(int requestCode, Bundle bundle);
//		void onPageItemClick(String requestTag, AdapterView<?> parent, int position);
//	}

    public PageListView(Context context){
        super(context);
        initialize(context, null);
    }

    public PageListView(Context context, AttributeSet attrs){
        super(context, attrs);
        initialize(context, attrs);
    }

    public PageListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public void initialize(Context context, AttributeSet attrs){
        if(isInEditMode()){
            return;
        }

        listView = new CustomListView(context);
        addView(listView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        setHeaderView(context);
        setFooterView(context);
        setLoadingView(context);

        mBottomView = new FrameLayout(context);
        FrameLayout.LayoutParams bottomParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        bottomParams.gravity = Gravity.BOTTOM;
        addView(mBottomView, bottomParams);

        if(attrs != null){
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageOptionTabBar);
//            if(a != null){
//                optionTabBar = new CustomTabBar(context);
//                optionTabBar.setTabFillViewport(a.getBoolean(R.styleable.PageOptionTabBar_optionFillViewport, true));
//
//                int iconsResId = a.getResourceId(R.styleable.PageOptionTabBar_optionIcons, -1);
//                int titlsResId = a.getResourceId(R.styleable.PageOptionTabBar_optionTitles, -1);
//                TypedArray icons = null;
//                String[] titles = null;
//
//                if(titlsResId == -1 && iconsResId != -1){
//                    icons = getResources().obtainTypedArray(iconsResId);
//                    for(int i=0;i<icons.length();i++){
//                        optionTabBar.addTab(i, null, icons.getResourceId(i, -1));
//                    }
//                }else if(titlsResId != -1 && iconsResId != -1){
//                    icons = getResources().obtainTypedArray(iconsResId);
//                    titles = getResources().getStringArray(titlsResId);
//                    for(int i=0;i<titles.length;i++){
//                        optionTabBar.addTab(i, titles[i], icons.getResourceId(i, -1));
//                    }
//                }
//
//                bottomView.addView(optionTabBar, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
//                bottomViewIsVisible = true;
//                hideBottomView(false);
//            }
//            a.recycle();
        }
    }


//    private void bottomViewToggle(final boolean visible, final boolean animate, final boolean force){
//        DeviceUtils.showLog(className, "bottomViewToggle " + (visible ? "visible " : "invisible"));
//        if (bottomViewIsVisible != visible || force) {
//            bottomViewIsVisible = visible;
//            bottomViewHeight = bottomView.getHeight();
//            if (bottomViewHeight == 0 && !force) {
//                ViewTreeObserver vto = bottomView.getViewTreeObserver();
//                if (vto.isAlive()) {
//                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                        @Override
//                        public boolean onPreDraw() {
//                            ViewTreeObserver currentVto = bottomView.getViewTreeObserver();
//                            if (currentVto.isAlive()) {
//                                currentVto.removeOnPreDrawListener(this);
//                            }
//                            bottomViewToggle(visible, animate, true);
//                            return true;
//                        }
//                    });
//                    return;
//                }
//            }
//
//            int translationY = visible ? 0 : bottomView.getMeasuredHeight() + bottomView.getPaddingTop();
//            if (animate) {
//                ViewPropertyAnimator.animate(bottomView).setInterpolator(sInterpolator)
//                        .setDuration(TRANSLATE_DURATION_MILLIS)
//                        .translationY(translationY);
//            } else {
//                ViewHelper.setTranslationY(bottomView, translationY);
//            }
//
//            // On pre-Honeycomb a translated view is still clickable, so we need to disable clicks manually
//            if (!DeviceUtils.hasHoneycomb()) {
//                setClickable(visible);
//            }
//        }
//    }

//    public void showBadgeView(boolean animate){
//        badgeViewtoggle(true, animate, false);
//    }

//    public void hideBadgeView(boolean animate){
//        badgeViewtoggle(false, animate, false);
//    }
//
//    public void setBadgeNumber(int number){
//        if(badgeView != null) badgeView.setBadgeNumber(number);
//    }

//    private void badgeViewtoggle(final boolean visible, final boolean animate, final boolean force){
//        if (badgeIsVisible != visible || force) {
//            badgeIsVisible = visible;
//            int height = badgeView.getHeight();
//            if (height == 0 && !force) {
//                ViewTreeObserver vto = badgeView.getViewTreeObserver();
//                if (vto.isAlive()) {
//                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//                        @Override
//                        public boolean onPreDraw() {
//                            ViewTreeObserver currentVto = badgeView.getViewTreeObserver();
//                            if (currentVto.isAlive()) {
//                                currentVto.removeOnPreDrawListener(this);
//                            }
//                            badgeViewtoggle(visible, animate, true);
//                            return true;
//                        }
//                    });
//                    return;
//                }
//            }
//
//            int translationY = visible ? 0 : -(badgeView.getMeasuredHeight() + badgeView.getPaddingTop());
//            if (animate) {
//                ViewPropertyAnimator.animate(badgeView).setInterpolator(sInterpolator)
//                        .setDuration(TRANSLATE_DURATION_MILLIS)
//                        .translationY(translationY);
//            } else {
//                ViewHelper.setTranslationY(badgeView, translationY);
//            }
//
//            // On pre-Honeycomb a translated view is still clickable, so we need to disable clicks manually
//            if (!DeviceUtils.hasHoneycomb()) {
//                setClickable(visible);
//            }
//        }
//    }

    public int getScrollState(){
        return currentScrollState;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        if(listener != null) listView.setOnItemClickListener(listener);
    }

    public void setOnPageLoadListener(OnPageLoadListener listener){
        if(listener != null) pageLoadListener = listener;
    }

    public void setAdapter(ListAdapter adapter){
        loadProgress.setVisibility(View.GONE);
        if(adapter != null){
            baseAdapter = (BaseAdapter) adapter;
            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            tapToBlock = true;
        }
    }

    private void needToAddFooter(){
        tapToBlock = true;
        listView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
            @Override public void onGlobalLayout() {
                if(baseAdapter != null && baseAdapter.getCount() > 0){
                    int position = listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();
                    View child = listView.getChildAt(position);
                    //DeviceUtils.showValue(className, "needToAddFooter() OnGlobalLayoutListener Visible last :" + position);
                    listView.removeLoadMoreButton();
                    if(child != null){
                        tapToBlock = child.getBottom() < listView.getMeasuredHeight() ? true : false;
                        //DeviceUtils.showValue(className, "needToAddFooter : " + child.getBottom() +", listView Height :" + listView.getMeasuredHeight());
                        if(tapToBlock || tapToLoadMoreEnabled){
                            //DeviceUtils.showValue(className, "needToAddFooter : add footer");
                            listView.addFooterLoadMoreButton();
                        }
                    }
                }else{
                    tapToBlock = false;
                }

                if(Build.VERSION.SDK_INT < 11)
                    listView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setTapToLoadMore(boolean enabled){
        tapToLoadMoreEnabled = enabled;
    }

    public void needAddBottomMargin(final int rowCount){
        if(listView.isScrollbarFadingEnabled())
            listView.setScrollbarFadingEnabled(false);

        if(rowCount > 1){
            listView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener(){
                @Override
                public boolean onPreDraw() {
                    int[] itemOffsetY = new int[baseAdapter.getCount() + 1];
                    int itemTotalHeight = 0;
                    int listViewHeight = listView.getHeight();

                    for (int i=0; i<= baseAdapter.getCount(); ++i) {
                        View view = baseAdapter.getView(i, null, listView);
                        // View view = listView.getChildAt(i);
                        itemOffsetY[i] = 0;
                        if(view != null){
                            view.measure(
                                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                            itemOffsetY[i] = itemTotalHeight;
                            itemTotalHeight += view.getMeasuredHeight();
                        }
                    }

                    int lastTop = itemOffsetY[rowCount - 1];
                    if(listViewHeight > itemTotalHeight){
                        lastTop += listViewHeight - itemTotalHeight;
                    }else{
                        lastTop -= itemTotalHeight - listViewHeight;
                    }

                    listView.addFooterMargin(lastTop);
                    listView.setSelection(rowCount);

//					View lastChild = listView.getChildAt(size); //headerView == index 0
//					View targetChild = listView.getChildAt(size - 1);
//
//					if(lastChild != null && targetChild != null){
//						marginBottom = targetChild.getBottom() - (lastChild.getBottom() - listView.getMeasuredHeight()) - height - 10;
//						listView.addFooterMargin(marginBottom);
//						listView.setSelection(size);
//					}
                    listView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        }
    }

    public void showPageLoading(){
        setHeaderProgress(2);
        setFooterProgress(2);
        listView.setVisibility(View.GONE);
        loadProgress.setVisibility(View.VISIBLE);
    }

    public void showPageRefresh(){
        setHeaderProgress(0);
        headerProgress.setIndeterminate(true);
    }

    public void showPageLoadMore(){
        setFooterProgress(0);
        footerProgress.setIndeterminate(true);
    }

    public void onPageTop(){
        listView.smoothScrollToPosition(0);
        if(pageTopHandler != null) pageTopHandler.removeCallbacks(pageTopRunnable);
        pageTopHandler = new Handler();
        pageTopHandler.postDelayed(pageTopRunnable, 100);
    }

    public void onTweetDetailLoadComplete(){
        setHeaderProgress(2);
        setFooterProgress(2);
        loadProgress.setVisibility(View.GONE);
        refreshState = RefreshState.PULL_TO_REFRESH;
    }

    public void onLoadComplete(){
        setHeaderProgress(2);
        setFooterProgress(2);
        loadProgress.setVisibility(View.GONE);
        refreshState = RefreshState.PULL_TO_REFRESH;
        needToAddFooter();
        loading = false;
    }

    public void onRefreshComplete(){
        setHeaderProgress(2);
        refreshState = RefreshState.PULL_TO_REFRESH;
        needToAddFooter();
    }

    public void onLoadMoreComplete(){
        setFooterProgress(2);
        needToAddFooter();
        loading = false;
    }

    public void onLoadMoreFailed(){
        setFooterProgress(2);
        tapToBlock = true;
        loading = false;
        listView.addFooterLoadMoreButton();
    }

    public void onRestorePosition(int newItemCount){
        if(listView != null && baseAdapter != null){
            listView.setSelectionFromTop(pagePosition + newItemCount + (listView.getHeaderViewsCount() > 1 ? 1 : 0), pageScrollY);
        }
    }

    public void onRestoreMissingPosition(int position){
        if(listView != null && baseAdapter != null){
            baseAdapter.notifyDataSetChanged();
            listView.setSelection((position > 0 ? position : pagePosition) + (listView.getHeaderViewsCount() > 1 ? 1 : 0));
        }
    }

    public void setPagePosition(int position, int scrollY){
        pagePosition = position;
        pageScrollY = scrollY;
    }

    public void setPullToRefreshEnabled(boolean enabled){
        isPullRefreshEnabled = enabled;
    }

    public boolean isPullToRefreshEnabled(){
        return isPullRefreshEnabled;
    }

    private void setLoadingView(Context context){
        loadProgress = new ProgressBar(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(loadProgress, params);
    }

    private void setHeaderView(Context context){
        LinearLayout header = new LinearLayout(context);
        header.setOrientation(LinearLayout.HORIZONTAL);
        addView(header, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        headerProgress = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        FrameLayout left = new FrameLayout(context);
        FrameLayout right = new FrameLayout(context);

        header.addView(left);
        header.addView(headerProgress);
        header.addView(right);

        FrameLayout.LayoutParams headerParams = (FrameLayout.LayoutParams) header.getLayoutParams();
        headerParams.gravity = Gravity.TOP;
        header.setLayoutParams(headerParams);

        LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) left.getLayoutParams();
        leftParams.weight = 1;
        leftParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        leftParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        left.setLayoutParams(leftParams);

        LinearLayout.LayoutParams progressParams = (LinearLayout.LayoutParams) headerProgress.getLayoutParams();
        progressParams.weight = 2;
        progressParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        progressParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        headerProgress.setLayoutParams(progressParams);

        LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) right.getLayoutParams();
        rightParams.weight = 1;
        rightParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rightParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        right.setLayoutParams(rightParams);
    }

    private void setFooterView(Context context){
        LinearLayout footer = new LinearLayout(context);
        footer.setOrientation(LinearLayout.HORIZONTAL);
        addView(footer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        footerProgress = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        FrameLayout left = new FrameLayout(context);
        FrameLayout right = new FrameLayout(context);

        footer.addView(left);
        footer.addView(footerProgress);
        footer.addView(right);

        FrameLayout.LayoutParams footerParams = (FrameLayout.LayoutParams) footer.getLayoutParams();
        footerParams.gravity = Gravity.BOTTOM;
        footer.setLayoutParams(footerParams);

        LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) left.getLayoutParams();
        leftParams.weight = 1;
        leftParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        leftParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        left.setLayoutParams(leftParams);

        LinearLayout.LayoutParams progressParams = (LinearLayout.LayoutParams) footerProgress.getLayoutParams();
        progressParams.weight = 2;
        progressParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        progressParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        footerProgress.setLayoutParams(progressParams);

        LinearLayout.LayoutParams rightParams = (LinearLayout.LayoutParams) right.getLayoutParams();
        rightParams.weight = 1;
        rightParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rightParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        right.setLayoutParams(rightParams);
    }

    public void setEmptyView(Context context){

    }

    private void setHeaderProgress(float weight){
        if(headerProgress != null){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headerProgress.getLayoutParams();
            params.weight = weight;
            headerProgress.setLayoutParams(params);
            headerProgress.setIndeterminate(false);
            headerProgress.postInvalidate();
        }
    }

    private void setFooterProgress(float weight){
        if(footerProgress != null){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) footerProgress.getLayoutParams();
            params.weight = weight;
            footerProgress.setIndeterminate(false);
            footerProgress.postInvalidate();
            footerProgress.setLayoutParams(params);
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        setPagePosition(ss.getPagePosition(), ss.getPageScrollY());
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState =  super.onSaveInstanceState();
        SavedState ss = new SavedState(superState, pagePosition, pageScrollY);
        return ss;
    }

    class SavedState extends View.BaseSavedState {

        private final int pagePosition;
        private final int pageScrollY;

        public SavedState(Parcelable superState, int pagePosition, int pageScrollY){
            super(superState);
            this.pagePosition = pagePosition;
            this.pageScrollY = pageScrollY;
        }

        public SavedState(Parcel in) {
            super(in);
            this.pagePosition = in.readInt();
            this.pageScrollY = in.readInt();
        }

        public int getPagePosition(){
            return this.pagePosition;
        }

        public int getPageScrollY(){
            return this.pageScrollY;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(pagePosition);
            out.writeInt(pageScrollY);
        }

        public final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private class CustomListView extends ListView implements OnScrollListener {

        private final String className = getClass().getSimpleName();

        private boolean blockLayoutChildren;

        private float lastMotionY = 0;
        private float lastMotionX = 0;
        private float initialMotionY = 0;

        private int SCROLL_DURATION = 150;

        private int prevoiusItemCount = 0;
        private int visibleThreshold = 2;

        private LayoutInflater inflater;
        private CustomHeaderView headerView;
        private RelativeLayout headerContent;
        private LinearLayout footerView;
        private LinearLayout footerMarginView;

        private int headerViewHeight;

        private Button footerLoadMoreButton;

        private int touchSlop;
        private Scroller scroller;

        public CustomListView(Context context){
            super(context);
            initialize();
        }

        public CustomListView(Context context, AttributeSet attrs){
            super(context, attrs);
            initialize();
        }

        public CustomListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            initialize();
        }

        public void initialize(){
            if(isInEditMode()){
                return;
            }

            final Context context = getContext();
            setVerticalFadingEdgeEnabled(false);
            scroller = new Scroller(context, new DecelerateInterpolator());

            headerView = new CustomHeaderView(context);
            headerContent = (RelativeLayout)headerView.findViewById(R.id.refresh_header);
            addHeaderView(headerView, null, false);

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            footerView = (LinearLayout)inflater.inflate(R.layout.layout_listview_more, null);

            footerMarginView = (LinearLayout)footerView.findViewById(R.id.footer_margin);
            footerLoadMoreButton = (Button)footerView.findViewById(R.id.button_load_more);
            footerLoadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(getFooterViewsCount() > 0) removeFooterView(footerView);
                    if(pageLoadListener != null) pageLoadListener.onPageLoadMore();
                }
            });

            headerView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
                @Override
                public void onGlobalLayout() {
                    headerViewHeight = headerContent.getHeight();
                    if(Build.VERSION.SDK_INT < 11)
                        headerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    else
                        headerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

            setOnScrollListener(this);
            isPullRefreshEnabled = true;

            touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

//		private void measureView(View child) {
//			ViewGroup.LayoutParams p = child.getLayoutParams();
//			if (p == null) {
//				p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//						ViewGroup.LayoutParams.WRAP_CONTENT);
//			}
//
//			int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
//			int lpHeight = p.height;
//			int childHeightSpec;
//			if (lpHeight > 0) {
//				childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
//						MeasureSpec.EXACTLY);
//			} else {
//				childHeightSpec = MeasureSpec.makeMeasureSpec(0,
//						MeasureSpec.UNSPECIFIED);
//			}
//			child.measure(childWidthSpec, childHeightSpec);
//		}

//		public void setBlockLayoutChildren(boolean block) {
//			blockLayoutChildren = block;
//		}
//
//		@Override
//		protected void layoutChildren() {
//			if (!blockLayoutChildren) {
//				super.layoutChildren();
//			}
//		}

        public void removeLoadMoreButton(){
            removeFooterView(footerView);
        }

        public void addFooterLoadMoreButton(){
            if(getFooterViewsCount() == 0){
                addFooterView(footerView);
                footerLoadMoreButton.setVisibility(View.VISIBLE);

            }
        }

        public void addFooterMargin(int height){
            if(getFooterViewsCount() == 0){
                addFooterView(footerView, null, false);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)footerMarginView.getLayoutParams();
                params.height = height;
                footerMarginView.setLayoutParams(params);
                footerMarginView.setVisibility(View.VISIBLE);
            }
        }

        public boolean isRrefreshPullAllowed(){
            if(refreshState == RefreshState.REFRESHING){
                return false;
            }

            View child = getChildAt(0);
            // int padding = getPaddingTop();
            if(child == null) return false;
            if(getFirstVisiblePosition() == 0 && child.getTop() == 0
					/*(child.getTop() == 0 ||  Math.abs(child.getTop() - padding) <= 5)*/){
                return true;
            }

            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch(ev.getAction() & MotionEvent.ACTION_MASK){
                case MotionEvent.ACTION_DOWN:
                    if(!isBeingRefreshDragged){
                        lastMotionY = initialMotionY = ev.getY();
                        lastMotionX = ev.getX();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:

                    if (!isVerticalScrollBarEnabled())
                        setVerticalScrollBarEnabled(true);

                    resetHeaderHeight();
                    setHeaderProgress(2);
                    isBeingRefreshDragged = false;

                    if(refreshState == RefreshState.RELEASE_TO_REFRESH){
                        if(pageLoadListener != null){
                            refreshState = RefreshState.REFRESHING;
                            pageLoadListener.onPageRefresh();
                        }
                    }

                    headerView.updateRefreshState(RefreshState.PULL_TO_REFRESH);
                    break;
                case MotionEvent.ACTION_MOVE:

                    final float y = ev.getY(), x = ev.getX();
                    final float diff, oppositeDiff, absDiff;

                    diff = y - lastMotionY;
                    oppositeDiff = x - lastMotionX;

                    absDiff = Math.abs(diff);

                    if(isRrefreshPullAllowed() && !isBeingRefreshDragged && isPullToRefreshEnabled()){
                        if(absDiff > touchSlop && absDiff > Math.abs(oppositeDiff)) {
                            isBeingRefreshDragged = true;
                            lastMotionY = initialMotionY = ev.getY();
                            lastMotionX = ev.getX();
                        }else{
                            isBeingRefreshDragged = false;
                        }
                    }

                    if(isBeingRefreshDragged && headerViewHeight > 0) {

                        if (isVerticalScrollBarEnabled())
                            setVerticalScrollBarEnabled(false);

                        final float yDiff = ev.getY() - initialMotionY;
                        int height = (int) (yDiff / 2.8f) + headerView.getVisiableHeight();
                        float heightRatio = (float) headerView.getVisiableHeight() / (float) (headerViewHeight + 5);
                        float ratio = (1 - (float) heightRatio);
                        float weight = 2 * (float) ratio;

                        //DeviceUtils.showLog(className, "onTouchEvent : headerViewHeight : "+ headerViewHeight+", Visible height : " + headerView.getVisiableHeight() + ", heightRatio :" + heightRatio + ", ratio : "+ ratio +", weight : " + weight);

                        //setHeaderProgress(weight);
                        if(headerView.getVisiableHeight() <= headerViewHeight + 5){
                            headerView.setVisiableHeight(height);
                        }
                        initialMotionY = lastMotionY = ev.getY();
                        lastMotionX = ev.getX();
                    }

                    break;
                case MotionEventCompat.ACTION_POINTER_DOWN:
                case MotionEventCompat.ACTION_POINTER_UP:
                    break;
            }

            return super.onTouchEvent(ev);
        }

        private void resetHeaderHeight(){
            int height = headerView.getVisiableHeight() - getScrollY();
            if(height > 0) {
                // mScrollBack = SCROLLBACK_HEADER;
                scroller.startScroll(0, height, 0, -height, SCROLL_DURATION);
                invalidate();
            }
        }

//		private void applyHeaderTopPadding(MotionEvent ev){
//			if(initialMotionY > -1f){
//				int pointerCount = ev.getHistorySize(); //how many finger pointer
//				for(int p=0; p<pointerCount;p++){
//
//					final int y = (int) ev.getHistoricalY(p);
//					final float yDiff = y - initialMotionY;
//
//					final float ratio = 1 - (yDiff / dragLenght);
//					final float weight = 2 *  ratio;
//					final int topPadding = (int) (-headerOriginalTopPadding * ratio);
//
//					if(isDragDownAllowed()) {
//						if(refreshState != RefreshState.REFRESHING){
//							setHeaderProgress(weight);
//						}
//
//						if (topPadding < 0) {
//							setHeaderPadding((int)yDiff);
//							refreshState = RefreshState.PULL_TO_REFRESH;
//						}else{
//							refreshState = RefreshState.RELEASE_TO_REFRESH;
//						}
//						DeviceUtils.showLog(className, "applyHeaderTopPadding : [padding : " + topPadding + ", bottom : "+ headerView.getBottom() +"]");
//
//					}else{
//						refreshState = RefreshState.PULL_TO_REFRESH;
//						setHeaderProgress(2);
//						//setHeaderPadding(Math.abs(topPadding) - headerOriginalTopPadding);
//						DeviceUtils.showLog(className, "applyHeaderTopPadding : reset padding [" + topPadding + ", bottom : "+ headerView.getBottom() +"]");
//					}
//
//					updateRefrshDownState();
//				}
//			}
//		}

        @Override
        public void computeScroll() {
            if (scroller.computeScrollOffset()) {
                //DeviceUtils.showLog(className, "PageListView Test computeScroll");
                headerView.setVisiableHeight(scroller.getCurrY());
//				if (mScrollBack == SCROLLBACK_HEADER) {
//					mHeaderView.setVisiableHeight(mScroller.getCurrY());
//				} else {
//					mFooterView.setBottomMargin(mScroller.getCurrY());
//				}
                postInvalidate();
            }
            super.computeScroll();
        }

        private boolean isSameFirstRow(int firstVisibleItem) {
            return firstVisibleItem == previousFirstVisibleItem;
        }

        private int getTopItemScrollY(AbsListView view) {
            if (view == null || view.getChildAt(0) == null) return 0;
            View topChild = view.getChildAt(0);
            return topChild.getTop();
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            currentScrollState = scrollState;
        }

        private int previousFirstVisibleItem;
        boolean checkRow = true;
        private int lastScrollY = 0;
        private int scrollThreshold = 100;

        public void setScrollThreshold(int threshold){
            scrollThreshold = threshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(getAdapter() == null){
                return;
            }

            if(getAdapter().getCount() == 0){
                return;
            }

            if(currentScrollState == SCROLL_STATE_TOUCH_SCROLL
                    && refreshState != RefreshState.REFRESHING){
                if(firstVisibleItem == 0){
                    if ((headerView.getBottom() < headerViewHeight + 5)
                            && refreshState != RefreshState.PULL_TO_REFRESH) {
                        refreshState = RefreshState.PULL_TO_REFRESH;
                    }else if (headerView.getBottom() > headerViewHeight + 5
                            && refreshState != RefreshState.RELEASE_TO_REFRESH){
                        refreshState = RefreshState.RELEASE_TO_REFRESH;
                    }
                    headerView.updateRefreshState(refreshState);
                }
            }


            pagePosition = view.getFirstVisiblePosition();
            View child = view.getChildAt(0);
            pageScrollY = (child == null) ? 0 : child.getTop();

            if(pageScrollY < 0 && view.getChildAt(1) != null){
                child = view.getChildAt(1);
                pageScrollY = child.getTop();
                pagePosition++;
            }

//            if(pageLoadListener != null){
//                pageLoadListener.onPageScroll(pagePosition, pageScrollY);
//                if(isSameFirstRow(firstVisibleItem)){
//                    int newScrollY = getTopItemScrollY(view);
//                    boolean isSignificantDelta = Math.abs(lastScrollY - newScrollY) > scrollThreshold;
//                    if (isSignificantDelta) {
//                        if (lastScrollY > newScrollY) {
//                            pageLoadListener.onPageScrollUp();
//                        } else {
//                            pageLoadListener.onPageScrollDown();
//                        }
//                    }
//
//                    lastScrollY = newScrollY;
//                }else{
//                    if (firstVisibleItem > previousFirstVisibleItem) {
//                        pageLoadListener.onPageScrollUp();
//                    } else {
//                        pageLoadListener.onPageScrollDown();
//                    }
//
//                    lastScrollY = getTopItemScrollY(view);
//                    previousFirstVisibleItem = firstVisibleItem;
//                }
//            }

//            if(!tapToLoadMoreEnabled && !tapToBlock){
//                if(totalItemCount < prevoiusItemCount){
//                    prevoiusItemCount = totalItemCount;
//                    if(totalItemCount == 0) checkRow = true;
//                }
//
//                if(checkRow && (totalItemCount > prevoiusItemCount)){
//                    checkRow = false;
//                    prevoiusItemCount = totalItemCount;
//                }
//
//                if(!checkRow){
//                    if(!loading && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))){
//                        if(pageLoadListener != null) pageLoadListener.onPageLoadMore();
//                        loading = checkRow = true;
//                    }
//                }
//            }
        }

        private class CustomHeaderView extends LinearLayout {

            private LinearLayout mContainer;
            private TextView mRefreshStateText;

            public CustomHeaderView(Context context) {
                super(context);
                initialize(context);
            }

            public CustomHeaderView(Context context, AttributeSet attrs) {
                super(context, attrs);
                initialize(context);
            }

            public CustomHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initialize(context);
            }

            private void initialize(Context context){
                mContainer = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.layout_listview_refresh, null);
                mRefreshStateText = (TextView)mContainer.findViewById(R.id.text_refresh_state);
                addView(mContainer, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
            }

            private void updateRefreshState(RefreshState state){
                switch(state){
                    case PULL_TO_REFRESH:
                        mRefreshStateText.setText("Pull to refresh..");
                        break;
                    case RELEASE_TO_REFRESH:
                        mRefreshStateText.setText("Release to refresh..");
                        break;
                    default: break;
                }
            }

            public void setVisiableHeight(int height) {
                if (height < 0){
                    height = 0;
                }

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
                params.height = height;
                mContainer.setLayoutParams(params);
            }

            public int getVisiableHeight() {
                return mContainer.getHeight();
            }

        }

//		private class CustomHeaderOnPreDrawListener implements OnPreDrawListener {
//			@Override
//			public boolean onPreDraw() {
//				headerViewHeight = headerView.getMeasuredHeight();
//				DeviceUtils.showLog(className, "CustomHeaderOnPreDrawListener headerViewHeight " + headerViewHeight);
//				if (headerViewHeight > 0) {
////					headerOriginalTopPadding = headerViewHeight;
////					setHeaderPadding(-headerOriginalTopPadding);
////					setHeaderHeight(0);
////					requestLayout();
//				}
//
//				headerView.getViewTreeObserver().removeOnPreDrawListener(this);
//				return false;
//			}
//
//		}
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////
    private class CustomBadgeView extends FrameLayout {

        private LayoutInflater inflater;
        private FrameLayout container;
        private TextView badgeNumber;

        public CustomBadgeView(Context context) {
            super(context);
            initialize();
        }

        public CustomBadgeView(Context context, AttributeSet attrs) {
            super(context, attrs);
            initialize();
        }

        public CustomBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initialize();
        }

        private void initialize(){
//            final Context context = getContext();
//            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            container = (FrameLayout)inflater.inflate(R.layout.badge_number, null);
//            badgeNumber = (TextView)container.findViewById(R.id.badge_number);
//            addView(container);
        }

        public void setBadgeNumber(int number){
            if(badgeNumber != null) badgeNumber.setText("" + number);
        }
    }
}
