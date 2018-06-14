package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yinaf.dragon.Content.Activity.LeisureActivitiesAct;
import com.yinaf.dragon.Content.Activity.WebViewAct;
import com.yinaf.dragon.Content.Bean.HomeFrgModel;
import com.yinaf.dragon.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class HomesAppAdapter extends CommonAdapter<HomeFrgModel.ObjBean.ApplicationListBean>{
    public HomesAppAdapter(Context context, int i, List<HomeFrgModel.ObjBean.ApplicationListBean> applicationList) {
        super(context, R.layout.homes_app_item,applicationList);
    }

    @Override
    protected void convert(ViewHolder holder, HomeFrgModel.ObjBean.ApplicationListBean rowsBean, final int position) {
        ImageView view = holder.getView(R.id.iv_ima);
        Glide.with(mContext).load(rowsBean.getApp_pic()).into(view);

        holder.setText(R.id.tv_title,rowsBean.getApp_title());


    }
}
