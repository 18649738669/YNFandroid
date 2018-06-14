package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.adapter.DrugSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.DrugSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 药物提醒
 */
public class DrugSetAct extends BasicAct implements DrugSetAPI.DrugSetListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    private DrugSetAdapter drugSetAdapter;
    private LoadingDialog loadingDialog;


    public DrugSetAct() {
        super(R.layout.drug_set_act, R.string.family_drug_set, R.string.family_add_drug_set
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        loadingDialog = LoadingDialog.showDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //网络请求,获取药物提醒列表
       int memberId = getIntent().getIntExtra("memberId", 0);

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())){
            String string = SPHelper.getString(Builds.SP_USER, memberId + "Drug");
            if (string!=null&&!TextUtils.isEmpty(string)){
                DrugSetModel model = JSONUtils.parseJson(string, DrugSetModel.class);
                drugSetAdapter = new DrugSetAdapter(getApplicationContext(), this, model.getObj());
                recyclerView.setAdapter(drugSetAdapter);
            }else {
                TipUtils.showTip("请开启网络!");
            }
        }else {
            loadingDialog.show();
            new DrugSetAPI(this, memberId);

        }

    }

    @Override
    public void drugSetSuccess(DrugSetModel drugSetModel) {
        List<DrugSetModel.ObjBean> obj = drugSetModel.getObj();
        loadingDialog.dismiss();
        //显示提醒列表
        if (obj.size() > 0) {
            if (drugSetAdapter == null) {
                drugSetAdapter = new DrugSetAdapter(getApplicationContext(), this, obj);
                recyclerView.setAdapter(drugSetAdapter);
            } else {
                drugSetAdapter.getDatas().clear();
                drugSetAdapter.getDatas().addAll(obj);
            }
            drugSetAdapter.notifyDataSetChanged();
            tv_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else if (drugSetAdapter==null){
            tv_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tool_bar_friends_circle_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_right_text:
                //添加提醒
                Intent intent = new Intent(getApplicationContext(), DrugSetAddAct.class);
                intent.putExtra("type", 0);
                intent.putExtra("watchId", getIntent().getIntExtra("watchId", 0));
                intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                startActivity(intent);
                break;

        }
    }

    @Subscribe()
    public void onMessageEvent(DataEventBus event) {
        new DrugSetAPI(DrugSetAct.this,
                getIntent().getIntExtra("memberId", 0));


    }

    @Override
    public void drugSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}


