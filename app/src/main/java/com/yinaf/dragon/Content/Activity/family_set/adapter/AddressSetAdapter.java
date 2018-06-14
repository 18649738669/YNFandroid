package com.yinaf.dragon.Content.Activity.family_set.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.AddressAddSetAct;
import com.yinaf.dragon.Content.Activity.family_set.DrugSetAddAct;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddressSetDeleteAPI;
import com.yinaf.dragon.Content.Net.DrugSetDeleteAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;

/**
 * 地址列表配适器
 */
public class AddressSetAdapter extends CommonAdapter<AddressSetModel.ObjBean> implements AddressSetDeleteAPI.AddressSetDeleteListener {

    private AddressSetModel.ObjBean obj;
    private Activity mAc;
    private final LoadingDialog loadingDialog;

    public AddressSetAdapter(Context context,Activity activity, List<AddressSetModel.ObjBean> datas) {
        super(context, R.layout.address_set_item, datas);
        this.mAc =activity;
        loadingDialog = LoadingDialog.showDialog(activity);
    }

    @Override
    protected void convert(ViewHolder holder, final AddressSetModel.ObjBean objBean, final int position) {
        //收件地址是否为默认地址
        TextView tv_defcode = holder.getView(R.id.tv_defcode);
        int defcode = objBean.getDefcode();
        if (defcode == 1) {//默认地址  0否  1是(默认地址只有一个)
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.cricle_select);
            //一定要加这行！！！！！！！！！！！
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_defcode.setCompoundDrawables(drawable, null, null, null);

        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.cricle);
            //一定要加这行！！！！！！！！！！！
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_defcode.setCompoundDrawables(drawable, null, null, null);

        }
        //收件人姓名
        holder.setText(R.id.tv_name, objBean.getReceiver());
        //收件人电话
        holder.setText(R.id.tv_phone, objBean.getPhone());
        //收件地址
        holder.setText(R.id.tv_address, objBean.getAddress());

        holder.getView(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj = objBean;
                loadingDialog.show();
                new AddressSetDeleteAPI(AddressSetAdapter.this, objBean.getAddressId());
            }
        });
        holder.getView(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", (Serializable)getDatas().get(position));
                Intent intent = new Intent(mContext, AddressAddSetAct.class);
                intent.putExtra("type", 1);
                intent.putExtra("memberId", objBean.getMemberId());
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mAc.startActivityForResult(intent,100);
            }
        });

    }

    @Override
    public void addressSetDeleteSuccess() {
        loadingDialog.dismiss();
        getDatas().remove(obj);
        notifyDataSetChanged();
    }

    @Override
    public void addressSetDeleteError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
