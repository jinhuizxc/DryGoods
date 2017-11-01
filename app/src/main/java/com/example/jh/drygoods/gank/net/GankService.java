package com.example.jh.drygoods.gank.net;

import com.example.jh.drygoods.gank.bean.CommonDate;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by jinhui on 2017/11/1.
 * Email：1004260403@qq.com
 */

public interface GankService {

    // http://gank.io/api/data/Android/10/1
    @GET("data/{type}/{count}/{pageIndex}")
    Call<CommonDate> getCommonDate(
            @Path("type") String type,
            @Path("count") int count,
            @Path("pageIndex") int pageIndex);
}
