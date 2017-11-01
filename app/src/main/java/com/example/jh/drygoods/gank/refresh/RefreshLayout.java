package com.example.jh.drygoods.gank.refresh;

/**
 * Created by jinhui on 2017/11/1.
 * Email：1004260403@qq.com
 */

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.jh.drygoods.MainActivity;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 *
 * @author mrsimple
 */
public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener{

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;

    /**
     * listview实例
     */

    private ListView mListView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */

    private OnLoadListener mOnLoadListener;



    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     *
     * @param mainActivity
     */
    public void setOnLoadListener(MainActivity mainActivity) {
    }

    public void setLoading(boolean b) {
    }

    /**
     * 加载更多的监听器
     */
    public interface OnLoadListener{
        void onLoad();
    }
}
