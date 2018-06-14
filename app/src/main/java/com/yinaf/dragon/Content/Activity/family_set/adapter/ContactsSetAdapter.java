package com.yinaf.dragon.Content.Activity.family_set.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yinaf.dragon.Content.Activity.family_set.AddressBookSetAddAct;
import com.yinaf.dragon.Content.Activity.family_set.ContactsSetAddAct;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.ContactsSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddressBookSetDeleteAPI;
import com.yinaf.dragon.Content.Net.ContactsSetDeleteAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;

/**
 * 联系人配适器
 */
public class ContactsSetAdapter extends CommonAdapter<ContactsSetModel.ObjBean> implements ContactsSetDeleteAPI.ContactsSetDeleteListener {

    private ContactsSetModel.ObjBean obj;
    private final LoadingDialog loadingDialog;

    public ContactsSetAdapter(Context context, Activity activity, List<ContactsSetModel.ObjBean> datas) {
        super(context, R.layout.address_book_add_set_item, datas);
        loadingDialog = LoadingDialog.showDialog(activity);
    }

    @Override
    protected void convert(ViewHolder holder, final ContactsSetModel.ObjBean s, final int position) {
        //姓名
        holder.setText(R.id.tv_name, s.getTrueName());
        //关系
        holder.setText(R.id.tv_relation, s.getRela());
        //电话
        holder.setText(R.id.tv_phone, s.getPhone());
        //地址
        if (TextUtils.isEmpty(s.getAddress())){
            holder.setText(R.id.tv_address, "暂无地址");
        }else {
            holder.setText(R.id.tv_address, s.getAddress());
        }
        holder.getView(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                obj=s;
                new ContactsSetDeleteAPI(ContactsSetAdapter.this, s.getContactsId());
            }
        });
        holder.getView(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", (Serializable)getDatas().get(position));
                Intent intent = new Intent(mContext, ContactsSetAddAct.class);
                intent.putExtra("type", 1);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void contactsSetDeleteSuccess() {
        loadingDialog.dismiss();
        getDatas().remove(obj);
        notifyDataSetChanged();
    }

    @Override
    public void contactsSetDeleteError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
