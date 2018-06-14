package com.yinaf.dragon.Content.Activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Adapter.LeisureActivityAdapter;
import com.yinaf.dragon.Content.Adapter.MyActivityAdapter;
import com.yinaf.dragon.Content.Bean.LeisureActivityModel;
import com.yinaf.dragon.Content.Bean.MyActivityModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.LeisureActivityAPI;
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
 * 休闲活动
 */
public class LeisureActivitiesAct extends BasicAct implements LeisureActivityAPI.LeisureActivityListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    private LoadingDialog loadingDialog;
    private LeisureActivityAdapter drugSetAdapter;

    public LeisureActivitiesAct() {
        super(R.layout.leisure_activity_act, R.string.leisure_activity, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())) {
            String string = SPHelper.getString(Builds.SP_USER, "LeisureActivity");
            if (string != null && !TextUtils.isEmpty(string)) {
                LeisureActivityModel model = JSONUtils.parseJson(string, LeisureActivityModel.class);

                initRecyclerView(model);
            } else {
                TipUtils.showTip("请开启网络!");
            }
        } else {
            loadingDialog.show();
            new LeisureActivityAPI(this);
        }
    }

    /**
     * 初始化控件
     */
    private void initRecyclerView(LeisureActivityModel obj) {
        drugSetAdapter = new LeisureActivityAdapter(getApplicationContext()
                , 0, obj.getRows());
        recyclerView.setAdapter(drugSetAdapter);
        drugSetAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getApplicationContext(), LeisureActivitiesDetailsAct.class);
                intent.putExtra("actId",drugSetAdapter.getDatas().get(position).getActId());
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
    public void leisureActivitySuccess(LeisureActivityModel drugSetModel) {
        List<LeisureActivityModel.RowsBean> obj = drugSetModel.getRows();
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
    public void leisureActivityError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }


}
