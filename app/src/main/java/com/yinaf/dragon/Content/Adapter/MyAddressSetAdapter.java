package com.yinaf.dragon.Content.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.MyAddressAddSetAct;
import com.yinaf.dragon.Content.Bean.MyAddressSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.MyAddressSetDeleteAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;

/**
 * 家庭成员 - 地址列表配适器
 */
public class MyAddressSetAdapter extends CommonAdapter<MyAddressSetModel.ObjBean> implements MyAddressSetDeleteAPI.AddressSetDeleteListener {

    private MyAddressSetModel.ObjBean obj;
    private Activity mAc;
    private final LoadingDialog loadingDialog;

    public MyAddressSetAdapter(Context context, Activity activity, List<MyAddressSetModel.ObjBean> datas) {
        super(context, R.layout.address_set_item, datas);
        this.mAc =activity;
        loadingDialog = LoadingDialog.showDialog(activity);
    }

    @Override
    protected void convert(ViewHolder holder, final MyAddressSetModel.ObjBean objBean, final int position) {
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
                new MyAddressSetDeleteAPI(MyAddressSetAdapter.this, objBean.getAuId());
            }
        });
        holder.getView(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", (Serializable)getDatas().get(position));
                Intent intent = new Intent(mContext, MyAddressAddSetAct.class);
                intent.putExtra("type", 1);
                intent.putExtra("memberId", objBean.getAuId());
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
