package com.example.vincent.sampletoolbar.tools;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 3/7/2016.
 */

public class UI {

    private static final String TAG = UI.class.getSimpleName();

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }

    public static void actionBarUpsideDown(Activity activity) {
        View root = activity.getWindow().getDecorView();
        View firstChild = ((ViewGroup) root).getChildAt(0);
        if (firstChild instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) firstChild;
            List<View> views = findViewsWithClassName(root, "com.android.internal.widget.ActionBarContainer");
            if (!views.isEmpty()) {
                for (View vv : views) {
                    viewGroup.removeView(vv);
                }
                for (View vv : views) {
                    viewGroup.addView(vv);
                }
            }
        } else {
            Log.e(TAG, "first child is not ViewGroup.");
        }
    }

    private static List<View> findViewsWithClassName(View v, String className) {
        List<View> views = new ArrayList<View>();
        findViewsWithClass(v, className, views);
        return views;
    }

    private static <T extends View> void findViewsWithClass(View v, String clazz, List<T> views) {
        if (v.getClass().getName().equals(clazz)) {
            views.add((T) v);
        }
        if (v instanceof ViewGroup) {
            ViewGroup g = (ViewGroup) v;
            for (int i = 0; i < g.getChildCount(); i++) {
                findViewsWithClass(g.getChildAt(i), clazz, views);
            }
        }
    }

}
