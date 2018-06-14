package com.yinaf.dragon.Content.Activity.family_set;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yinaf.dragon.Content.Activity.family_set.model.FootstepSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.FrequencySetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.FootstepSetAPI;
import com.yinaf.dragon.Content.Net.FrequencySetAPI;
import com.yinaf.dragon.Content.Net.WatchSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发送频率
 */
public class FrequencySetAct extends BasicAct implements FrequencySetAPI.FrequencySetListener, WatchSetAPI.WatchSetListener {


    @BindView(R.id.tv_heart)
    TextView tvHeart;
    @BindView(R.id.tv_blood)
    TextView tvBlood;
    @BindView(R.id.tv_blood_glucose)
    TextView tvBloodGlucose;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    private int watchId;
    private int memberId;
    private LoadingDialog loadingDialog;
    private FrequencySetModel model;
    private EditText content;

    public FrequencySetAct() {
        super(R.layout.frequency_set_act, R.string.family_frequency_set, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        loadingDialog = LoadingDialog.showDialog(this);
        watchId = intent.getIntExtra("watchId", 0);
        memberId = intent.getIntExtra("memberId", 0);

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())) {
            String string = SPHelper.getString(Builds.SP_USER, memberId + "Frequency");
            if (string != null && !TextUtils.isEmpty(string)) {
                model = JSONUtils.parseJson(string, FrequencySetModel.class);
                FrequencySetModel.ObjBean obj = model.getObj();
                tvHeart.setText(obj.getHeartRate() + "秒/次");
                tvLocation.setText(obj.getPosition() + "秒/次");
                tvBloodGlucose.setText(obj.getBloodSuger() + "秒/次");
                tvBlood.setText(obj.getBloodPressure() + "秒/次");
            } else {
                TipUtils.showTip("请开启网络!");
            }
        } else {
            loadingDialog.show();
            new FrequencySetAPI(this, memberId);
        }

    }

    @OnClick({R.id.tv_heart, R.id.tv_location, R.id.tv_blood_glucose, R.id.tv_blood})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_heart:
                //心率
                showFrequencyDialog(1, tvHeart.getText().toString().split("秒")[0]);
                break;
            case R.id.tv_location:
                //定位
                showFrequencyDialog(2, tvLocation.getText().toString().split("秒")[0]);
                break;
            case R.id.tv_blood_glucose:
                //血糖
                showFrequencyDialog(3, tvBloodGlucose.getText().toString().split("秒")[0]);
                break;
            case R.id.tv_blood:
                //血压
                showFrequencyDialog(4, tvBlood.getText().toString().split("秒")[0]);
                break;
        }
    }

    private int type_number = 0;

    public void showFrequencyDialog(final int type, String c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alert_dialog);


        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View v = inflater.inflate(R.layout.frequency_dialog, null);
        content = (EditText) v.findViewById(R.id.dialog_content);
        content.setText(c);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadingDialog.show();
                switch (type) {
                    case 1:
                        type_number = 1;
                        tvHeart.setText(content.getText() + "秒/次");
                        new WatchSetAPI(FrequencySetAct.this, memberId, watchId
                                , "heartRate", content.getText().toString());
                        break;
                    case 2:
                        type_number = 2;
                        tvLocation.setText(content.getText() + "秒/次");
                        new WatchSetAPI(FrequencySetAct.this, memberId, watchId
                                , "position", content.getText().toString());
                        break;
                    case 3:
                        type_number = 3;
                        tvBloodGlucose.setText(content.getText() + "秒/次");
                        new WatchSetAPI(FrequencySetAct.this, memberId, watchId
                                , "bloodSuger", content.getText().toString());
                        break;
                    case 4:
                        type_number = 4;
                        tvBlood.setText(content.getText() + "秒/次");
                        new WatchSetAPI(FrequencySetAct.this, memberId, watchId
                                , "bloodPressure", content.getText().toString());
                        break;
                }
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

            }
        });
    }


    @Override
    public void frequencySetSuccess(FrequencySetModel drugSetModel) {
        loadingDialog.dismiss();
        if (drugSetModel != null && drugSetModel.getObj() != null) {
            FrequencySetModel.ObjBean obj = drugSetModel.getObj();
            tvHeart.setText(obj.getHeartRate() + "秒/次");
            tvLocation.setText(obj.getPosition() + "秒/次");
            tvBloodGlucose.setText(obj.getBloodSuger() + "秒/次");
            tvBlood.setText(obj.getBloodPressure() + "秒/次");
        }

    }

    @Override
    public void frequencySetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void watchSetSuccess() {
        String string = SPHelper.getString(Builds.SP_USER, memberId + "Frequency");
        if (!TextUtils.isEmpty(string)){
            model = JSONUtils.parseJson(string, FrequencySetModel.class);
            if (model == null) {
                model = new FrequencySetModel();
            }
        }else {
            model = new FrequencySetModel();
        }

        switch (type_number) {
            case 1:
                model.getObj().setHeartRate(content.getText().toString());
                tvHeart.setText(content.getText() + "秒/次");
                break;
            case 2:
                model.getObj().setPosition(content.getText().toString());
                tvLocation.setText(content.getText() + "秒/次");
                break;
            case 3:
                model.getObj().setBloodSuger(content.getText().toString());
                tvBloodGlucose.setText(content.getText() + "秒/次");
                break;
            case 4:
                model.getObj().setBloodPressure(content.getText().toString());
                tvBlood.setText(content.getText() + "秒/次");
                break;
        }
        SPHelper.save(Builds.SP_USER,memberId+"Frequency", new Gson().toJson(model));
        loadingDialog.dismiss();
    }

    @Override
    public void watchSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}

