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
import com.yinaf.dragon.Content.Activity.family_set.model.FrequencySetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.PoliceSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.FrequencySetAPI;
import com.yinaf.dragon.Content.Net.PoliceSetAPI;
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
 * 预警范围
 */
public class PoliceSetAct extends BasicAct implements PoliceSetAPI.PoliceSetListener
        , WatchSetAPI.WatchSetListener {


    @BindView(R.id.tv_hrMin)
    TextView tvHrMin;
    @BindView(R.id.tv_hrMax)
    TextView tvHrMax;
    @BindView(R.id.tv_sbpMin)
    TextView tvSbpMin;
    @BindView(R.id.tv_sbpMax)
    TextView tvSbpMax;
    @BindView(R.id.tv_dpbMin)
    TextView tvDpbMin;
    @BindView(R.id.tv_dpbMax)
    TextView tvDpbMax;
    @BindView(R.id.tv_gluMin)
    TextView tvGluMin;
    @BindView(R.id.tv_gluMax)
    TextView tvGluMax;

    private int watchId;
    private int memberId;
    private LoadingDialog loadingDialog;
    private PoliceSetModel model;
    private EditText content;

    public PoliceSetAct() {
        super(R.layout.police_set_act, R.string.family_police_set, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);

        Intent intent = getIntent();
        watchId = intent.getIntExtra("watchId", 0);
        memberId = intent.getIntExtra("memberId", 0);

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())) {
            String string = SPHelper.getString(Builds.SP_USER, memberId + "Police");
            if (string != null && !TextUtils.isEmpty(string)) {
                model = JSONUtils.parseJson(string, PoliceSetModel.class);
                PoliceSetModel.ObjBean obj = model.getObj();

                tvHrMin.setText(obj.getHrMin() + "pbm");
                tvHrMax.setText(obj.getHrMax() + "pbm");
                tvSbpMin.setText(obj.getSbpMin() + "mmHg");
                tvSbpMax.setText(obj.getSbpMax() + "mmHg");
                tvDpbMin.setText(obj.getDbpMin() + "mmHg");
                tvDpbMax.setText(obj.getSbpMax() + "mmHg");
                tvGluMin.setText(obj.getGluMin() + "mmol/L");
                tvGluMax.setText(obj.getGluMax() + "mmol/L");
            } else {
                TipUtils.showTip("请开启网络!");
            }
        } else {
            loadingDialog.show();
            new PoliceSetAPI(this, memberId);
        }


    }

    @OnClick({R.id.tv_hrMin, R.id.tv_hrMax, R.id.tv_sbpMin, R.id.tv_sbpMax
            , R.id.tv_dpbMin, R.id.tv_dpbMax, R.id.tv_gluMin, R.id.tv_gluMax})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hrMin:
                showFrequencyDialog(1, tvHrMin.getText().toString().split("p")[0]);
                break;
            case R.id.tv_hrMax:
                showFrequencyDialog(2, tvHrMax.getText().toString().split("p")[0]);
                break;
            case R.id.tv_sbpMin:
                showFrequencyDialog(3, tvSbpMin.getText().toString().split("m")[0]);
                break;
            case R.id.tv_sbpMax:
                showFrequencyDialog(4, tvSbpMax.getText().toString().split("m")[0]);
                break;
            case R.id.tv_dpbMin:
                showFrequencyDialog(5, tvDpbMin.getText().toString().split("m")[0]);
                break;
            case R.id.tv_dpbMax:
                showFrequencyDialog(6, tvDpbMax.getText().toString().split("m")[0]);
                break;
            case R.id.tv_gluMin:
                showFrequencyDialog(7, tvGluMin.getText().toString().split("m")[0]);
                break;
            case R.id.tv_gluMax:
                showFrequencyDialog(8, tvGluMax.getText().toString().split("m")[0]);
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
                String params_name = "";
                String param = "";
                switch (type) {
                    case 1:
                        type_number=1;
                        params_name = "hrMin";
                        param = content.getText().toString();
                        break;
                    case 2:
                        type_number=2;
                        params_name = "hrMax";
                        param = content.getText().toString();
                        break;
                    case 3:
                        type_number=3;
                        params_name = "sbpMin";
                        param = content.getText().toString();
                        break;
                    case 4:
                        type_number=4;
                        params_name = "sbpMax";
                        param = content.getText().toString();
                        break;
                    case 5:
                        type_number=5;
                        params_name = "dbpMin";
                        param = content.getText().toString();
                        break;
                    case 6:
                        type_number=6;
                        params_name = "dbpMax";
                        param = content.getText().toString();
                        break;
                    case 7:
                        type_number=7;
                        params_name = "gluMin";
                        param = content.getText().toString();
                        break;
                    case 8:
                        type_number=8;
                        params_name = "gluMax";
                        param = content.getText().toString();
                        break;
                }
                new WatchSetAPI(PoliceSetAct.this, memberId, watchId
                        , params_name, param);

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
    public void policeSetSuccess(PoliceSetModel drugSetModel) {
        loadingDialog.dismiss();
        if (drugSetModel != null && drugSetModel.getObj() != null) {
            PoliceSetModel.ObjBean obj = drugSetModel.getObj();

            tvHrMin.setText(obj.getHrMin() + "pbm");
            tvHrMax.setText(obj.getHrMax() + "pbm");
            tvSbpMin.setText(obj.getSbpMin() + "mmHg");
            tvSbpMax.setText(obj.getSbpMax() + "mmHg");
            tvDpbMin.setText(obj.getDbpMin() + "mmHg");
            tvDpbMax.setText(obj.getSbpMax() + "mmHg");
            tvGluMin.setText(obj.getGluMin() + "mmol/L");
            tvGluMax.setText(obj.getGluMax() + "mmol/L");
        }
    }

    @Override
    public void policeSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void watchSetSuccess() {
        String string = SPHelper.getString(Builds.SP_USER, memberId + "Police");
        if (!TextUtils.isEmpty(string)){
            model = JSONUtils.parseJson(string, PoliceSetModel.class);
            if (model == null) {
                model = new PoliceSetModel();
            }
        }else {
            model = new PoliceSetModel();
        }

        switch (type_number) {
            case 1:
                model.getObj().setHrMin(content.getText().toString());
                tvHrMin.setText(content.getText() + "pbm");

                break;
            case 2:
                model.getObj().setHrMax(content.getText().toString());
                tvHrMax.setText(content.getText() + "pbm");
                break;
            case 3:
                model.getObj().setSbpMin(content.getText().toString());
                tvSbpMin.setText(content.getText() + "mmHg");
                break;
            case 4:
                model.getObj().setSbpMax(content.getText().toString());
                tvSbpMax.setText(content.getText() + "mmHg");
                break;
            case 5:
                model.getObj().setDbpMin(content.getText().toString());
                tvDpbMin.setText(content.getText() + "mmHg");
                break;
            case 6:
                model.getObj().setDbpMax(content.getText().toString());
                tvDpbMax.setText(content.getText() + "mmHg");
                break;
            case 7:
                model.getObj().setGluMin(content.getText().toString());
                tvGluMin.setText(content.getText() + "mmol/L");
                break;
            case 8:
                model.getObj().setGluMax(content.getText().toString());
                tvGluMax.setText(content.getText() + "mmol/L");
                break;
        }
        SPHelper.save(Builds.SP_USER,memberId+"Police", new Gson().toJson(model));

        loadingDialog.dismiss();

    }

    @Override
    public void watchSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}


