package com.yinaf.dragon.Content.Activity.family_set;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.adapter.AppSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.model.AppSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AppSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import java.util.List;

import butterknife.BindView;

/**
 * App 关联列表
 */
public class AppSetAct extends BasicAct implements AppSetAPI.AppSetListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    private LoadingDialog loadingDialog;
    private AppSetAdapter drugSetAdapter;

    public AppSetAct() {
        super(R.layout.app_set_act, R.string.family_app_set, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        int memberId = getIntent().getIntExtra("memberId", 0);
        //网络请求,获取列表
        if (!NetworkUtils.isNetworkConnect(getApplicationContext())){
            String string = SPHelper.getString(Builds.SP_USER, memberId + "App");
            if (string!=null&&!TextUtils.isEmpty(string)){
                AppSetModel   model = JSONUtils.parseJson(string, AppSetModel.class);
                drugSetAdapter = new AppSetAdapter(getApplicationContext(), 0, model.getObj());
                recyclerView.setAdapter(drugSetAdapter);
            }else {
                TipUtils.showTip("请开启网络!");
            }
        }else {
            loadingDialog.show();
            new AppSetAPI(this,memberId);
        }



    }




    @Override
    public void appSetSuccess(AppSetModel drugSetModel) {
        List<AppSetModel.ObjBean> obj = drugSetModel.getObj();
        loadingDialog.dismiss();
        //显示提醒列表
        if (obj.size() > 0) {
            drugSetAdapter = new AppSetAdapter(getApplicationContext(), 0, obj);
            recyclerView.setAdapter(drugSetAdapter);
            drugSetAdapter.notifyDataSetChanged();
            tv_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void appSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }



}
