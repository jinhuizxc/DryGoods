package com.example.jh.drygoods;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jh.drygoods.cache.ACache;
import com.example.jh.drygoods.gank.adapter.GankCommonAdapter;
import com.example.jh.drygoods.gank.bean.CommonDate;
import com.example.jh.drygoods.gank.callback.ICallBack;
import com.example.jh.drygoods.gank.constant.Constants;
import com.example.jh.drygoods.gank.log.L;
import com.example.jh.drygoods.gank.net.GankApi;
import com.example.jh.drygoods.gank.refresh.RefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.sloop.net.utils.NetUtils;

import java.util.List;

/**
 * 放下姿态去积累学习——
 * 学习下数据缓存、网络请求
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener {

    private Toolbar mToolbar;
    // 定义各类标签
    private String[] flags = {Constants.FLAG_All, Constants.FLAG_Meizi, Constants.FLAG_Android, Constants.FLAG_iOS,
            Constants.FLAG_JS, Constants.FLAG_Recommend, Constants.FLAG_Video, Constants.FLAG_Expand};
    private String currentFlag = flags[0];
    private int currentIndex = 1;

    // 缓存类
    private ACache mCache;
    private ListView mListView;
    // 适配器
    private GankCommonAdapter mAdapter;
    // 适配器刷新控件
    private RefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCache = ACache.get(getApplicationContext());
        initViews();
        startRefresh();
    }


    @Override
    protected void onStop() {
        super.onStop();
        stopAllState();
    }

    private void startRefresh() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
        onRefresh();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        /**
         * 暂时注销掉后续可能有补充
         */
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.setNavigationItemSelectedListener(this);

        headerView.findViewById(R.id.head_img).setOnClickListener(this);
        headerView.findViewById(R.id.head_name).setOnClickListener(this);
        headerView.findViewById(R.id.head_web).setOnClickListener(this);

        mRefreshLayout = findViewById(R.id.id_swipe_ly);
        mRefreshLayout.setOnRefreshListener(this);
        // 上拉监听器, 到了最底部的上拉加载操作
        mRefreshLayout.setOnLoadListener(this);

        mAdapter = new GankCommonAdapter(this, null);
        mListView = findViewById(R.id.id_listview);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, false));
    }


    /**
     * 回调方法
     */
    private ICallBack<CommonDate> mCallBack = new ICallBack<CommonDate>() {
        @Override
        public void onSuccess(String flag, String key, CommonDate commonDate) {
            L.e("onSuccess");
            stopAllState();
            if(flag != currentFlag) return;
            if(commonDate.isError()){
                Toast.makeText(MainActivity.this, "数据加载出错", Toast.LENGTH_SHORT).show();
            }
            // 加入缓存
            mCache.put(key,commonDate,ACache.TIME_DAY*7);
            L.e("cache key =" + key);

            List<CommonDate.ResultsEntity> datas = commonDate.getResults();
            mAdapter.addDatas(datas);
        }

        @Override
        public void onFailure(String flag, String key, String why) {
            L.e("onFailure：" + why);
            stopAllState();
        }
    };

    private void stopAllState() {
        mRefreshLayout.setLoading(false);
        mRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            resetAllByFlag(flags[0]);
        } else if (id == R.id.nav_welfare) {
            resetAllByFlag(flags[1]);
        } else if (id == R.id.nav_android) {
            resetAllByFlag(flags[2]);
        } else if (id == R.id.nav_ios) {
            resetAllByFlag(flags[3]);
        }else if(id == R.id.nav_js){
            resetAllByFlag(flags[4]);
        } else if (id == R.id.nav_recommend) {
            resetAllByFlag(flags[5]);
        } else if (id == R.id.nav_video) {
            resetAllByFlag(flags[6]);
        } else if (id == R.id.nav_expand) {
            resetAllByFlag(flags[7]);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void resetAllByFlag(String flag) {
        // set Title
        mToolbar.setTitle(flag);
        if(flag.equals(Constants.FLAG_All)){
            mToolbar.setTitle(R.string.app_name);
        }
        // clear data
        currentFlag = flag;
        currentIndex = 1;
        mAdapter.clearDatas();

        // reset listview
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            mListView.smoothScrollToPosition(0);
        } else {
            mListView.setSelection(0);
        }

        // get new data
        startRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_img:
                showByUrl(Constants.URL_GANK);
                break;
            case R.id.head_name:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.head_web:
                showByUrl(Constants.URL_Github);
                break;
        }
    }

    @Override
    public void onRefresh() {
        L.e("onRefresh");
        currentIndex = 1;
        mAdapter.clearDatas();
        getData();
    }

    private void getData() {
        try {
            String key = currentFlag + 20 + currentIndex;   // all201
            L.e("key = " + key);
            if (NetUtils.isNetConnection(this)) {
                GankApi.getCommonData(currentFlag, 20, currentIndex, mCallBack);
            } else {
                Toast.makeText(this, "网络连接异常，请检查网络！", Toast.LENGTH_SHORT).show();
               // 获取本地缓存
                getDataFromCache(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataFromCache(String key) {
        L.e("get data key="+key);
        CommonDate data = (CommonDate) mCache.getAsObject(key);
        if(data != null){
            mAdapter.addDatas(data.getResults());
        }
        stopAllState();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonDate.ResultsEntity data = mAdapter.getDataById(position);
        if (data.getType().equals(Constants.FLAG_Meizi)){
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra(Constants.key_imgurl, data.getUrl());
            startActivity(intent);
        }else {
            showByUrl(data.getUrl());
        }
    }
    // 除了福利外都是链接访问。
    private void showByUrl(String url) {
        Intent ie = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(ie);
    }

    @Override
    public void onLoad() {
        L.e("onLoad");
        currentIndex++;
        getData();
    }
}
