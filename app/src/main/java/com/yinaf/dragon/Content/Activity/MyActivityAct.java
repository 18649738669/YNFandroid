package com.yinaf.dragon.Content.Activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Adapter.MyActivityAdapter;
import com.yinaf.dragon.Content.Bean.MyActivityModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.MyActivityAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 我的活动列表页
 */
public class MyActivityAct extends BasicAct implements MyActivityAPI.MyActivityListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    private LoadingDialog loadingDialog;
    private MyActivityAdapter drugSetAdapter;

    public MyActivityAct() {
        super(R.layout.my_activity_act, R.string.my_activity, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())){
            String string = SPHelper.getString(Builds.SP_USER, "Activity");
            if (string!=null&&!TextUtils.isEmpty(string)){
                MyActivityModel model = JSONUtils.parseJson(string, MyActivityModel.class);
                initRecyclerView(model);

            }else {
                TipUtils.showTip("请开启网络!");
            }
        }else {
            loadingDialog.show();
            new MyActivityAPI(this);
        }
    }

    /**
     * 列表初始化
     */
     private void initRecyclerView(MyActivityModel model){
         drugSetAdapter = new MyActivityAdapter(getApplicationContext()
                 , 0,model.getRows());
         recyclerView.setAdapter(drugSetAdapter);
         drugSetAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                 Intent intent = new Intent(getApplicationContext(), MemberActivitiesDetailsAct.class);
                 intent.putExtra("actId",drugSetAdapter.getDatas().get(position).getMaId());
                 intent.putExtra("title",drugSetAdapter.getDatas().get(position).getActivityTitle());
                 startActivity(intent);
             }

             @Override
             public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                 return false;
             }
         });
     }
    @Override
    public void myActivitySuccess(MyActivityModel drugSetModel) {
        List<MyActivityModel.RowsBean> obj = drugSetModel.getRows();
        //显示提醒列表
        loadingDialog.dismiss();
        if (obj.size() > 0) {
            if (drugSetAdapter == null) {
                 initRecyclerView(drugSetModel);

            } else if (drugSetAdapter.getDatas() != null && drugSetAdapter.getDatas().size() > 0) {
                drugSetAdapter.getDatas().clear();
                drugSetAdapter.getDatas().addAll(obj);
            }
            drugSetAdapter.notifyDataSetChanged();
        } else {
            tv_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void myActivityError(long code, String msg) {
            loadingDialog.dismiss();
            TipUtils.showTip(msg);
    }
}
