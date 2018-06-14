package com.yinaf.dragon.Content.Activity.family_set.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yinaf.dragon.Content.Activity.family_set.AddressBookSetAddAct;
import com.yinaf.dragon.Content.Activity.family_set.DrugSetAddAct;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddressBookSetDeleteAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;

/**
 * 通讯录 配适器
 */
public class AddressBookSetAdapter extends CommonAdapter<AddressBookSetModel.ObjBean>
        implements AddressBookSetDeleteAPI.AddressBookSetDeleteListener {

    private AddressBookSetModel.ObjBean obj;
    private final LoadingDialog loadingDialog;

    public AddressBookSetAdapter(Context context, Activity activity, List<AddressBookSetModel.ObjBean> datas) {
        super(context, R.layout.address_book_add_set_item, datas);
        loadingDialog = LoadingDialog.showDialog(activity);
    }

    @Override
    protected void convert(ViewHolder holder, final AddressBookSetModel.ObjBean s, final int position) {

        //姓名
        holder.setText(R.id.tv_name, s.getTrueName());
        //关系
        holder.setText(R.id.tv_relation, s.getRela());
        //电话
        holder.setText(R.id.tv_phone, s.getPhone());
        //地址
        if (TextUtils.isEmpty(s.getAddress())&&TextUtils.isEmpty(s.getProvince())
                &&TextUtils.isEmpty(s.getCity())&&TextUtils.isEmpty(s.getAreas())){
            holder.setText(R.id.tv_address, "暂无地址");
        }else {
            holder.setText(R.id.tv_address, s.getAddress());
        }

        holder.getView(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj = s;
                loadingDialog.show();
                new AddressBookSetDeleteAPI(AddressBookSetAdapter.this, s.getBookId());
            }
        });
        holder.getView(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", (Serializable)getDatas().get(position));
                Intent intent = new Intent(mContext, AddressBookSetAddAct.class);
                intent.putExtra("type", 1);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void addressBookSetDeleteSuccess() {
        loadingDialog.dismiss();
        getDatas().remove(obj);
        notifyDataSetChanged();
    }

    @Override
    public void addressBookSetDeleteError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
