package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Adapter.HealthReportAdapter;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Bean.OneTextData;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetHealthRecordByMonthAPI;
import com.yinaf.dragon.Content.Net.GetHeartRateByMonthAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.App;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/08.
 * 功能：健康报告统计页面
 */

public class HealthReportAct extends BasicAct implements GetHealthRecordByMonthAPI.GetHealthRecordByMonthListener {


    @BindView(R.id.health_report_rounded_img)
    RoundedImageView healthRecordRoundedImg;
    @BindView(R.id.health_report_tv_name)
    TextView healthRecordTvName;
    @BindView(R.id.health_report_recycler)
    RecyclerView healthRecordRecycler;

    HealthReportAdapter healthReportAdapter;
    List<OneTextData>  healthReportList = new ArrayList<OneTextData>();
    private ImageLoader imageLoader;
    DatabaseHelper dbHelper;
    LoadingDialog loadingDialog;



    public HealthReportAct() {
        super(R.layout.act_health_report, R.string.title_activity_health_report, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId) {
        Intent intent = new Intent(context, HealthReportAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        dbHelper = new DatabaseHelper(this, SPHelper.getString(Builds.SP_USER, "userName"));
        imageLoader = App.getImageLoader();
        initRoundedImg();
        healthReportAdapter = new HealthReportAdapter(this,healthReportList);
        healthRecordRecycler.setLayoutManager(new LinearLayoutManager(this));
        healthRecordRecycler.setAdapter(healthReportAdapter);
        healthReportAdapter.setOnClickItemListener(new HealthReportAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(View view, String tag,int position) {
                String year = "";
                String month = "";
                for (int i = 0; i < position; i++){
                    if (healthReportList.get(i).getType() == 0){
                        year = healthReportList.get(i).getText();
                    }
                }
                if (Integer.parseInt(healthReportList.get(position).getText()) >= 10){
                    month = "-" + healthReportList.get(position).getText();
                }else {
                    month = "-0" + healthReportList.get(position).getText();
                }
                NextActivity(year + month);

            }
        });
        loadingDialog.show();
        new GetHealthRecordByMonthAPI(this,getIntent().getStringExtra("memberId"));
    }

    public void NextActivity( String time){
        HealthReportWebViewAct.startActivity(this,getIntent().getStringExtra("memberId"),time);
    }

    /**
     * 初始化圆形头像数据
     */
    private void initRoundedImg() {
        Member member = dbHelper.selectMemberByMemberId(SPHelper.getInt(Builds.SP_USER,"memberId"));
        if (member.getImage() == null ||
                member.getImage().equals("")){
            healthRecordRoundedImg.setImageResource(R.drawable.rounded_img2);
        }else {
            imageLoader.displayImage(member.getImage(), healthRecordRoundedImg);
        }
        healthRecordTvName.setText(member.getRealName());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 健康报告月份统计
     * @param content
     */
    @Override
    public void getHealthRecordByMonthSuccess(JSONArray content) {
        try {
            for (int i = 0; i < content.length(); i++){
                JSONObject jsonObject = content.getJSONObject(i);
                OneTextData oneTextData = new OneTextData();
                oneTextData.setText(JSONUtils.getString(jsonObject,"year"));
                oneTextData.setType(0);
                healthReportList.add(oneTextData);
                JSONArray jsonArray = JSONUtils.getJSONArray(jsonObject,"month");
                for (int j = 0;j < jsonArray.length(); j++){
                    OneTextData oneTextData1 = new OneTextData();
                    oneTextData1.setText(jsonArray.getString(j));
                    oneTextData1.setType(1);
                    healthReportList.add(oneTextData1);
                }
            }
            healthReportAdapter.notifyDataSetChanged();
            loadingDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getHealthRecordByMonthError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
