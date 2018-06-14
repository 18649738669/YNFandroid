package com.yinaf.dragon.Content.Activity.family_set.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.yinaf.dragon.Content.Activity.family_set.model.MemberSeleteModel;
import com.yinaf.dragon.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 成员资料信息选择配适器
 */
public class MemberSelectAdapter extends CommonAdapter<MemberSeleteModel> {

    public MemberSelectAdapter(Context context, int layoutId, List<MemberSeleteModel> datas) {
        super(context, R.layout.member_select_item, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MemberSeleteModel s, int position) {
        holder.setText(R.id.tv_name,s.getItems());
        ImageView view = holder.getView(R.id.iv_select);
        if (s.isSelect()){
            view.setImageResource(R.drawable.cricle_select);
        }else {
            view.setImageResource(R.drawable.cricle);
        }

    }
}
