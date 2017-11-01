package com.example.jh.drygoods.gank.callback;

/**
 * Created by jinhui on 2017/11/1.
 * Email：1004260403@qq.com
 */

/**
 * <ul type="disc">
 * <li>Author: Sloop</li>
 * <li>Version: v1.0.0</li>
 * <li>Date: 2016/3/8</li>
 * <li>Copyright (c) 2015 GcsSloop</li>
 * <li><a href="http://weibo.com/GcsSloop" target="_blank">WeiBo</a>      </li>
 * <li><a href="https://github.com/GcsSloop" target="_blank">GitHub</a>   </li>
 * </ul>
 */
public interface ICallBack<T> {

    void onSuccess(String flag, String key, T t);

    void onFailure(String flag, String key, String why);
}
