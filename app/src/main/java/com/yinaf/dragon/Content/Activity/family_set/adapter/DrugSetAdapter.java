package com.yinaf.dragon.Content.Activity.family_set.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yinaf.dragon.Content.Activity.family_set.DrugSetAddAct;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.DrugSetDeleteAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 药物提醒列表配适器
 */
public class DrugSetAdapter extends CommonAdapter<DrugSetModel.ObjBean> implements DrugSetDeleteAPI.DrugSetAddListener {

    private DrugSetModel.ObjBean obj;

    private Activity mAc;
    private final LoadingDialog loadingDialog;

    public DrugSetAdapter(Context context, Activity activity, List<DrugSetModel.ObjBean> datas) {
        super(context, R.layout.drug_set_item, datas);
        this.mAc =activity;
        loadingDialog = LoadingDialog.showDialog(activity);
    }

    @Override
    protected void convert(ViewHolder holder, final DrugSetModel.ObjBean objBean, final int position) {
        //提醒时间
        holder.setText(R.id.tv_time, objBean.getRemindTime());
        //提醒日期
        holder.setText(R.id.tv_date, objBean.getStartTime() + " 至 " + objBean.getEndTime());
        //备注内容
        holder.setText(R.id.tv_content, objBean.getMedicineTitle());

        holder.getView(R.id.item_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                obj = objBean;
                new DrugSetDeleteAPI(DrugSetAdapter.this, objBean.getMedicineId());
            }
        });
        holder.getView(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("model", (Serializable)getDatas().get(position));
                Intent intent = new Intent(mContext, DrugSetAddAct.class);
                intent.putExtra("type", 1);
                intent.putExtra("pos", position);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void drugSetAddSuccess() {
        loadingDialog.dismiss();
        getDatas().remove(obj);
        notifyDataSetChanged();
    }

    @Override
    public void drugSetAddError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
