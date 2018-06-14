package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yinaf.dragon.Content.Bean.LeisureActivityModel;
import com.yinaf.dragon.Content.Bean.MyActivityModel;
import com.yinaf.dragon.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class LeisureActivityAdapter extends CommonAdapter<LeisureActivityModel.RowsBean> {

    public LeisureActivityAdapter(Context context, int layoutId, List<LeisureActivityModel.RowsBean> datas) {
        super(context, R.layout.leisure_activity_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, LeisureActivityModel.RowsBean rowsBean, int position) {
         //活动标题
        holder.setText(R.id.tv_title,rowsBean.getActivityTitle());

        //报名时间
        holder.setText(R.id.tv_time,"活动时间: "+rowsBean.getStartTime()+"-"+rowsBean.getEndTime());

        //活动名额
        holder.setText(R.id.tv_grade,"活动名额 :"
                +(!TextUtils.isEmpty(rowsBean.getActPlaces())?rowsBean.getActPlaces():0)+"人");

        //报名人数
        holder.setText(R.id.tv_pep,"报名人数 :"+
                (!TextUtils.isEmpty(rowsBean.getUsePlaces())?rowsBean.getUsePlaces():0)+"人");

        Glide.with(mContext).load(rowsBean.getActivityImg()).into((ImageView) holder.getView(R.id.iv_imag));
    }
}
