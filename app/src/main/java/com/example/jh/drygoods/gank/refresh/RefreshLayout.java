package com.example.jh.drygoods.gank.refresh;

/**
 * Created by jinhui on 2017/11/1.
 * Email：1004260403@qq.com
 */

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.jh.drygoods.MainActivity;
import com.example.jh.drygoods.R;

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

    /**
     * ListView的加载中footer
     */
    private View mListViewFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;

    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;

    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;



    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.listview_footer, null, false);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滚动时到了底部也可以加载更多
        if(canLoad()){
            loadData();
        }
    }

    private void loadData() {
    }

    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    private boolean isPullUp() {
        return false;
    }

    private boolean isBottom() {
        return false;
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
