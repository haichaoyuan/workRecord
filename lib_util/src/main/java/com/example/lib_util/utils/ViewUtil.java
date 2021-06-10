package com.example.lib_util.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author: JiangWeiwei
 * @time: 2018/5/19-10:21
 * @email: jiangweiwei@qccr.com
 * @desc:
 */
public class ViewUtil {
    public static <T extends View> T findChildView(View paramView, Class<T> t) {
        View childView;
        if (paramView instanceof ViewGroup) {
            ViewGroup tempVg = (ViewGroup) paramView;
            int count = tempVg.getChildCount();
            for (int index = 0; index < count; index++) {
                View tempView = tempVg.getChildAt(index);
                if (t.isInstance(tempView)) {
                    childView = tempView;
                    return (T) childView;
                } else if (tempView instanceof ViewGroup) {
                    View view = findChildView(tempView, t);
                    if (view != null) {
                        return (T) view;
                    }
                }
            }
        }
        return null;
    }


    public static <T extends View> List<T> findChildViews(List<T> views, View paramView, Class<T> t) {
        if (paramView instanceof ViewGroup) {
            ViewGroup tempVg = (ViewGroup) paramView;
            int count = tempVg.getChildCount();
            for (int index = 0; index < count; index++) {
                View tempView = tempVg.getChildAt(index);
                if (t.isInstance(tempView)) {
                    views.add((T) tempView);
                } else if (tempView instanceof ViewGroup) {
                    findChildViews(views, tempView, t);
                }
            }
        }
        return views;
    }

    public static boolean isScrolledToTop(RecyclerView rv) {
        return rv.getLayoutManager() instanceof LinearLayoutManager &&
                ((LinearLayoutManager) (rv.getLayoutManager())).findFirstCompletelyVisibleItemPosition() == 0;

    }

    public static boolean isScrolledToTop(ScrollView scrollView) {
        return scrollView.getScrollY() == 0;
    }
}
