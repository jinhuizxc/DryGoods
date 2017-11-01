package com.example.jh.drygoods.gank.net;

import com.example.jh.drygoods.gank.bean.CommonDate;
import com.example.jh.drygoods.gank.callback.ICallBack;
import com.example.jh.drygoods.gank.log.L;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jinhui on 2017/11/1.
 * Email：1004260403@qq.com
 */

public class GankApi {

    // 网络请求获取数据
    public static Call<CommonDate> getCommonData(final String type, int count, int pageIndex, final ICallBack<CommonDate> callBack) {
        Call<CommonDate> commonDate = BuildService.getGankService().getCommonDate(type, count, pageIndex);
        final String key = type + count + pageIndex;
        commonDate.enqueue(new Callback<CommonDate>() {
            @Override
            public void onResponse(Response<CommonDate> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    CommonDate commonDate = response.body();
                    L.i("getCommonData---onResponse：" + commonDate.toString());
                    if(!commonDate.isError()){
                        // 数据正确，把数据返回
                        callBack.onSuccess(type, key, commonDate);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                L.e("getCommonData-----onFailure：" + t.toString());
            }
        });
        return commonDate;
    }
}
