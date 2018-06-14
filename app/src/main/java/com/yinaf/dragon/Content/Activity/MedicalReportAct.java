package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetDeviceReportAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/05/17.
 * 功能：体检报告详情页面
 */

public class MedicalReportAct extends BasicAct implements GetDeviceReportAPI.GetDeviceReportListener {



    @BindView(R.id.medical_report_tv_sex)
    TextView medicalReportTvSex;
    @BindView(R.id.medical_report_tv_name)
    TextView medicalReportTvName;
    @BindView(R.id.medical_report_tv_height)
    TextView medicalReportTvHeight;
    @BindView(R.id.medical_report_tv_weight)
    TextView medicalReportTvWeight;
    @BindView(R.id.medical_report_tv_hwBmi)
    TextView medicalReportTvHwBmi;
    @BindView(R.id.medical_report_tv_templature)
    TextView medicalReportTvTemplature;
    @BindView(R.id.medical_report_tv_leftEyesight)
    TextView medicalReportTvLeftEyesight;
    @BindView(R.id.medical_report_tv_rightEyesight)
    TextView medicalReportTvRightEyesight;
    @BindView(R.id.medical_report_tv_oxygenSaturation)
    TextView medicalReportTvOxygenSaturation;
    @BindView(R.id.medical_report_tv_pulse_pulseRate)
    TextView medicalReportTvPulsePulseRate;
    @BindView(R.id.medical_report_tv_fatRate)
    TextView medicalReportTvFatRate;
    @BindView(R.id.medical_report_tv_fat)
    TextView medicalReportTvFat;
    @BindView(R.id.medical_report_tv_leanBodyWeight)
    TextView medicalReportTvLeanBodyWeight;
    @BindView(R.id.medical_report_tv_waterRate)
    TextView medicalReportTvWaterRate;
    @BindView(R.id.medical_report_tv_water)
    TextView medicalReportTvWater;
    @BindView(R.id.medical_report_tv_boneMass)
    TextView medicalReportTvBoneMass;
    @BindView(R.id.medical_report_tv_muscleMass)
    TextView medicalReportTvMuscleMass;
    @BindView(R.id.medical_report_tv_muscleRate)
    TextView medicalReportTvMuscleRate;
    @BindView(R.id.medical_report_tv_visceralFat)
    TextView medicalReportTvVisceralFat;
    @BindView(R.id.medical_report_tv_basalMetabolism)
    TextView medicalReportTvBasalMetabolism;
    @BindView(R.id.medical_report_tv_build)
    TextView medicalReportTvBuild;
    @BindView(R.id.medical_report_tv_waistCircumference)
    TextView medicalReportTvWaistCircumference;
    @BindView(R.id.medical_report_tv_hipCircumference)
    TextView medicalReportTvHipCircumference;
    @BindView(R.id.medical_report_tv_ytBmi)
    TextView medicalReportTvYtBmi;
    @BindView(R.id.medical_report_tv_heightPressure)
    TextView medicalReportTvHeightPressure;
    @BindView(R.id.medical_report_tv_lowPressure)
    TextView medicalReportTvLowPressure;
    @BindView(R.id.medical_report_tv_pulse)
    TextView medicalReportTvPulse;
    @BindView(R.id.medical_report_tv_bloodSugar)
    TextView medicalReportTvBloodSugar;
    @BindView(R.id.medical_report_tv_uricAcid)
    TextView medicalReportTvUricAcid;
    @BindView(R.id.medical_report_tv_totalCholesterol)
    TextView medicalReportTvTotalCholesterol;
    @BindView(R.id.medical_report_tv_triglycerides)
    TextView medicalReportTvTriglycerides;
    @BindView(R.id.medical_report_tv_heightLipoprotein)
    TextView medicalReportTvHeightLipoprotein;
    @BindView(R.id.medical_report_tv_lowLipoprotein)
    TextView medicalReportTvLowLipoprotein;
    @BindView(R.id.medical_report_tv_tcHdl)
    TextView medicalReportTvTcHdl;

    LoadingDialog loadingDialog;
    int drId;

    public MedicalReportAct() {
        super(R.layout.act_medical_report, R.string.health_records_ll_medical_report, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);

    }

    public static void startActivity(Context context, int drId) {
        Intent intent = new Intent(context, MedicalReportAct.class);
        intent.putExtra("drId", drId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        drId = getIntent().getIntExtra("drId", 0);
        loadingDialog = LoadingDialog.showDialog(this);
        loadingDialog.show();
        new GetDeviceReportAPI(this, drId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 体检报告详情
     *
     * @param content
     */
    @Override
    public void getDeviceReportSuccess(JSONObject content) {
        if (JSONUtils.getInt(content,"sex") == 1){
            medicalReportTvSex.setText("性别：男");
        }else {
            medicalReportTvSex.setText("性别：女");
        }
        medicalReportTvName.setText("用户名："+JSONUtils.getString(content,"userName"));
        medicalReportTvHeight.setText(JSONUtils.getString(content,"height")+"cm");
        medicalReportTvWeight.setText(JSONUtils.getString(content,"weight")+"kg");
        medicalReportTvHwBmi.setText(JSONUtils.getString(content,"hwBmi"));
        medicalReportTvTemplature.setText(JSONUtils.getString(content,"templature")+"℃");
        medicalReportTvLeftEyesight.setText(JSONUtils.getString(content,"leftEyesight"));
        medicalReportTvRightEyesight.setText(JSONUtils.getString(content,"rightEyesight"));
        medicalReportTvOxygenSaturation.setText(JSONUtils.getString(content,"oxygenSaturation")+"%");
        medicalReportTvPulsePulseRate.setText(JSONUtils.getString(content,"pulseRate"));
        medicalReportTvFatRate.setText(JSONUtils.getString(content,"fatRate")+"%");
        medicalReportTvFat.setText(JSONUtils.getString(content,"fat")+"kg");
        medicalReportTvLeanBodyWeight.setText(JSONUtils.getString(content,"leanBodyWeight"));
        medicalReportTvWaterRate.setText(JSONUtils.getString(content,"waterRate")+"%");
        medicalReportTvWater.setText(JSONUtils.getString(content,"water")+"kg");
        medicalReportTvBoneMass.setText(JSONUtils.getString(content,"boneMass"));
        medicalReportTvMuscleMass.setText(JSONUtils.getString(content,"muscleMass"));
        medicalReportTvMuscleRate.setText(JSONUtils.getString(content,"muscleRate"));
        medicalReportTvVisceralFat.setText(JSONUtils.getString(content,"visceralFat"));
        medicalReportTvBasalMetabolism.setText(JSONUtils.getString(content,"basalMetabolism")+"kcal");
        medicalReportTvBuild.setText(JSONUtils.getString(content,"build"));
        medicalReportTvWaistCircumference.setText(JSONUtils.getString(content,"waistCircumference")+"cm");
        medicalReportTvHipCircumference.setText(JSONUtils.getString(content,"hipCircumference")+"cm");
        medicalReportTvYtBmi.setText(JSONUtils.getString(content,"ytBmi"));
        medicalReportTvHeightPressure.setText(JSONUtils.getString(content,"heightPressure")+"mmhg");
        medicalReportTvLowPressure.setText(JSONUtils.getString(content,"lowPressure")+"mmhg");
        medicalReportTvPulse.setText(JSONUtils.getString(content,"pulse")+"次/分");
        medicalReportTvBloodSugar.setText(JSONUtils.getString(content,"bloodSugar")+"mmol/L");
        medicalReportTvUricAcid.setText(JSONUtils.getString(content,"uricAcid")+"mmol/L");
        medicalReportTvTotalCholesterol.setText(JSONUtils.getString(content,"totalCholesterol")+"mmol/L");
        medicalReportTvTriglycerides.setText(JSONUtils.getString(content,"triglycerides")+"mmol/L");
        medicalReportTvHeightLipoprotein.setText(JSONUtils.getString(content,"heightLipoprotein")+"mmol/L");
        medicalReportTvLowLipoprotein.setText(JSONUtils.getString(content,"lowLipoprotein")+"mmol/L");
        medicalReportTvTcHdl.setText(JSONUtils.getString(content,"tcHdl"));
        loadingDialog.dismiss();

    }

    @Override
    public void getDeviceReportError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
