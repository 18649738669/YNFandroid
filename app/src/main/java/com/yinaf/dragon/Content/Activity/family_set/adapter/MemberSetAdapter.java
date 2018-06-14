package com.yinaf.dragon.Content.Activity.family_set.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 成员基本信息配适器
 */
public class MemberSetAdapter extends CommonAdapter<String[]> {

    public MemberSetAdapter(Context context, int layoutId, List<String[]> datas) {
        super(context, R.layout.member_set_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String[] objBean, int position) {
        holder.setText(R.id.tv_title, objBean[1]);
        if (!TextUtils.isEmpty(objBean[2])) {
            holder.setText(R.id.tv_content, objBean[2]);
        }else {
            holder.setText(R.id.tv_content, "");
        }


    }
}
