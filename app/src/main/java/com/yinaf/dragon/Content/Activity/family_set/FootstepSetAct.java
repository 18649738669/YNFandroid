package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yinaf.dragon.Content.Activity.family_set.model.FootstepSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.FootstepSetAPI;
import com.yinaf.dragon.Content.Net.WatchSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 步长设置
 */
public class FootstepSetAct extends BasicAct implements FootstepSetAPI.FootstepSetListener, WatchSetAPI.WatchSetListener {

    @BindView(R.id.edt_number)
    EditText edtNumber;
    @BindView(R.id.edt_run)
    EditText edtRun;

    private int watchId;
    private int memberId;
    private LoadingDialog loadingDialog;
    private FootstepSetModel model;


    public FootstepSetAct() {
        super(R.layout.footstep_set_act, R.string.family_footstep_set, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        loadingDialog = LoadingDialog.showDialog(this);
        watchId = intent.getIntExtra("watchId", 0);
        memberId = intent.getIntExtra("memberId", 0);

        memberId = getIntent().getIntExtra("memberId", 0);

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())){
            String string = SPHelper.getString(Builds.SP_USER, memberId + "Footstep");
            if (string!=null&&!TextUtils.isEmpty(string)){
                model = JSONUtils.parseJson(string, FootstepSetModel.class);
                edtNumber.setText(model.getObj().getWalkStep()+"");
                edtRun.setText(model.getObj().getRunStep()+"");
            }else {
                TipUtils.showTip("请开启网络!");
            }
        }else {
            loadingDialog.show();
            new FootstepSetAPI(this, memberId);
        }

    }

    @OnClick({R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                if (edtNumber.getText().toString().length() == 0) {
                    TipUtils.showTip("走步散步步长不能为空");
                    return;
                }
                if (edtRun.getText().toString().length() == 0) {
                    TipUtils.showTip("跑步步长不能为空");
                    return;
                }
                loadingDialog.show();
                new WatchSetAPI(this, memberId, watchId
                        , "walkStep", edtNumber.getText().toString()
                        , "runStep", edtRun.getText().toString());
                break;
        }
    }


    @Override
    public void footstepSetSuccess(FootstepSetModel drugSetModel) {
        loadingDialog.dismiss();
        if (drugSetModel != null && drugSetModel.getObj() != null) {
            FootstepSetModel.ObjBean obj = drugSetModel.getObj();
            edtNumber.setText(obj.getWalkStep()+"");
            edtRun.setText(obj.getRunStep()+"");
        }
    }

    @Override
    public void footstepSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }


    @Override
    public void watchSetSuccess() {
        loadingDialog.dismiss();
        finish();
    }

    @Override
    public void watchSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}

