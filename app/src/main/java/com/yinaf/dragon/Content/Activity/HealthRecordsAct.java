package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.ChooseBindingDialog;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/08.
 * 功能：健康档案页面
 */

public class HealthRecordsAct extends BasicAct {


    @BindView(R.id.health_records_ll_health_report)
    LinearLayout healthRecordsLlHealthReport;

    DatabaseHelper dbHelper;
    Member member;

    public HealthRecordsAct() {
        super(R.layout.act_health_records, R.string.family_tv_health_records, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);

    }

    public static void startActivity(Context context, int memberId) {
        Intent intent = new Intent(context, HealthRecordsAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        dbHelper = new DatabaseHelper(this, SPHelper.getString(Builds.SP_USER,"userName"));
        member = dbHelper.selectMemberByMemberId(getIntent().getIntExtra("memberId",0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.health_records_ll_health_report,R.id.health_records_ll_medical_report})
    public void onViewClicked(View view) {

        switch (view.getId()){
            case R.id.health_records_ll_health_report:
                HealthReportAct.startActivity(this, getIntent().getIntExtra("memberId",0)+"");
                break;
            case R.id.health_records_ll_medical_report:
                MedicalReportListAct.startActivity(this,getIntent().getIntExtra("memberId",0)+"");
                break;
                default:
                    break;
        }

    }
}
