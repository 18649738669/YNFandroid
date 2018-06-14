package com.yinaf.dragon.Content.Activity.family_set.adapter;

import android.content.Context;

import com.yinaf.dragon.Content.Activity.family_set.model.AppSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 *  App 列表配适器
 */
public class AppSetAdapter extends CommonAdapter<AppSetModel.ObjBean> {

    public AppSetAdapter(Context context, int layoutId, List<AppSetModel.ObjBean> datas) {
        super(context, R.layout.app_set_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, AppSetModel.ObjBean objBean, int position) {
        //姓名
        holder.setText(R.id.tv_name, objBean.getRelaName());
        //电弧
        holder.setText(R.id.tv_phone, objBean.getPhone());
        //会员
        if (objBean.getUserType() == 0) {
            holder.setText(R.id.tv_member, "普通会员");
        } else {
            holder.setText(R.id.tv_member, " VIP会员");
        }

        //关系
        holder.setText(R.id.tv_relation, objBean.getRela());
    }
}
