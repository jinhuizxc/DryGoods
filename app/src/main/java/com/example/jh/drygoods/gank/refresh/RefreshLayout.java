package com.example.jh.drygoods.gank.refresh;

/**
 * Created by jinhui on 2017/11/1.
 * Email：1004260403@qq.com
 * <p>
 * layout是Android View布局过程的第二个阶段，第一阶段是measure，
 * 即测量，首先测定view的大小。layout的作用是给所有的child安排大小和摆放位置。
 * 有两个主要的相关方法：
 * public void layout(int l, int t, int r, int b);子类不应该重载这个方法，而应该选择重载 protected void onLayout(boolean changed, int left, int top, int right, int bottom)，这个onLayout由layout方法调用，
 * 如果子类是一个ViewGroup（例如LinearLayout或者RelativeLayout），则应该重载onLayout方法，对每个child调用其layout方法，来安排child的大小和位置。
 * <p>
 * 简而言之，如果你自定义的是View而不是ViewGroup，那么不用重载这个，
 * 否则，你应该重载它，并且为所有的child安排，两件事，1，占多大地方，2放在哪儿。
 */

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.jh.drygoods.MainActivity;
import com.example.jh.drygoods.R;
import com.example.jh.drygoods.gank.log.L;

/**
 * 继承自SwipeRefreshLayout,从而实现滑动到底部时上拉加载更多的功能.
 *
 * @author mrsimple
 */
public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

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

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();  // 21

        mListViewFooter = LayoutInflater.from(context).inflate(R.layout.listview_footer, null, false);
    }

    /**
     * ListView是SwipeRefreshView的子控件，所以需要在onLayout()方法中获取子控件ListView。
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
    }


    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
                Log.e(VIEW_LOG_TAG, "### 找到listview");
            }
        }
    }

    /*
    * (non-Javadoc)
    * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
    *  屏幕触摸事件
    */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                // 抬起
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 滚动时到了底部也可以加载更多
        if (canLoad()) {
            loadData();
        }
    }

    private void loadData() {
        if (mOnLoadListener != null) {
            // 设置状态
            setLoading(true);
            mOnLoadListener.onLoad();
        }
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    /**
     * @return 是否是上拉操作
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * 判断是否到了最底部 一次网络加载20项
     */
    private boolean isBottom() {
        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        }
        return false;
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.addFooterView(mListViewFooter);
        } else {
            mListView.removeFooterView(mListViewFooter);
            mYDown = 0;
            mLastY = 0;
        }
    }


    /**
     * 加载更多的监听器
     */
    public interface OnLoadListener {
        void onLoad();
    }
}
