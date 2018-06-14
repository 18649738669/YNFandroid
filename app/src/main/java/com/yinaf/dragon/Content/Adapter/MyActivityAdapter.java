package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yinaf.dragon.Content.Bean.MyActivityModel;
import com.yinaf.dragon.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 我的活动配适器
 */
public class MyActivityAdapter extends CommonAdapter<MyActivityModel.RowsBean> {

    public MyActivityAdapter(Context context, int layoutId, List<MyActivityModel.RowsBean> datas) {
        super(context, R.layout.my_activity_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MyActivityModel.RowsBean rowsBean, int position) {
            //活动编号
            holder.setText(R.id.tv_number,"活动编号:  "+rowsBean.getActivityNum());
            //活动标题
            holder.setText(R.id.tv_title,rowsBean.getActivityTitle());

            //报名时间
            holder.setText(R.id.tv_time,"报名时间: "+rowsBean.getCreateTime());

            //积分
            holder.setText(R.id.tv_grade,"消耗积分: "+rowsBean.getActIntegral()+" 分/次");

        Glide.with(mContext).load(rowsBean.getActivityImg()).into((ImageView) holder.getView(R.id.iv_imag));
    }
}
