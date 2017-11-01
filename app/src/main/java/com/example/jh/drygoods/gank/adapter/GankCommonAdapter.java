package com.example.jh.drygoods.gank.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jh.drygoods.MainActivity;
import com.example.jh.drygoods.R;
import com.example.jh.drygoods.gank.bean.CommonDate;
import com.example.jh.drygoods.gank.constant.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sloop.adapter.utils.CommonAdapter;
import com.sloop.adapter.utils.ViewHolder;

import java.util.List;

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

public class GankCommonAdapter extends CommonAdapter<CommonDate.ResultsEntity>{

    private Animation push_left_in, push_right_in;
    DisplayImageOptions options;

    // 重写的是这个方法
    public GankCommonAdapter(@NonNull Context context, List<CommonDate.ResultsEntity> datas, @NonNull int layoutId) {
        super(context, datas, layoutId);
    }

    public GankCommonAdapter(@NonNull Context context, List<CommonDate.ResultsEntity> datas) {
        super(context, datas, R.layout.item_common);
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(configuration); // Get singleton instance

        push_left_in = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
        push_right_in = AnimationUtils.loadAnimation(context, R.anim.push_right_in);
        push_left_in.setDuration(1000);
        push_right_in.setDuration(1000);

        //显示图片的配置
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

    }


    @Override
    public void convert(int position, ViewHolder holder, CommonDate.ResultsEntity bean) {
        View convert = holder.getConvertView();
        ImageView img = holder.getView(R.id.common_img);
        TextView desc = holder.getView(R.id.common_desc);
        TextView via = holder.getView(R.id.common_via);
        TextView tag = holder.getView(R.id.common_tag);

        via.setText("via:" + bean.getWho());
        tag.setText(bean.getType());

        if(bean.getType().equals(Constants.FLAG_Meizi)){
            img.setVisibility(View.VISIBLE);
            desc.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(bean.getUrl(), img, options);
        }else {
            desc.setVisibility(View.VISIBLE);
            desc.setText(bean.getDesc());
            img.setVisibility(View.GONE);
        }

        if(position % 2 == 0){
            convert.setAnimation(push_right_in);
        }else {
            convert.setAnimation(push_left_in);
        }
    }
}
