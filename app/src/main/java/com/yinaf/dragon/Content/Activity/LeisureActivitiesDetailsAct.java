package com.yinaf.dragon.Content.Activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yinaf.dragon.Content.Bean.LeisureActivitiesDetailsModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.LeisureActivitiesAPI;
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
 * 休闲详情
 */
public class LeisureActivitiesDetailsAct extends BasicAct implements LeisureActivitiesAPI.LeisureActivitiesListener {


    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_sponsor_name)
    TextView tvSponsorName;
    @BindView(R.id.tv_sponsor_telephone)
    TextView tvSponsorTelephone;
    @BindView(R.id.tv_act_content)
    TextView tvActContent;
    @BindView(R.id.btn_add)
    Button btnAdd;
    private LoadingDialog loadingDialog;
    private LeisureActivitiesDetailsModel model;
    private EditText content;
    private String actId;

    public LeisureActivitiesDetailsAct() {
        super(R.layout.leisure_activity_details_act, R.string.leisure_activity, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);
    }


    @Override
    public void initView() {
        Intent intent = getIntent();
        loadingDialog = LoadingDialog.showDialog(this);
        actId = intent.getStringExtra("actId");
        toolBarFriendsCircleTitle.setText(intent.getStringExtra("title"));

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())) {
            String string = SPHelper.getString(Builds.SP_USER, actId + "Leisure");
            if (string != null && !TextUtils.isEmpty(string)) {
                model = JSONUtils.parseJson(string, LeisureActivitiesDetailsModel.class);
                LeisureActivitiesDetailsModel.ObjBean obj = model.getObj();
                //图片
                Glide.with(getApplicationContext()).load(obj.getActivityImg()).into(ivImg);
                //活动概述
                tvContent.setText(obj.getActSummary());
                //活动时间
                tvTime.setText("活动时间: "+obj.getStartTime()+"-"+obj.getEndTime());
                //活动地点
                tvAddress.setText("活动地点: "+obj.getAddress());
                //消耗积分
                tvGrade.setText("消耗积分: "+obj.getActIntegral()+"分/次");
                //主办方
                tvSponsorName.setText("活动主办方: "+obj.getSponsorName());
                //主办方联系
                tvSponsorTelephone.setText("主办方联系方式: "+obj.getSponsorTelephone());
                //活动内容
                tvActContent.setText("活动内容: "+obj.getActContent());
            } else {
                TipUtils.showTip("请开启网络!");
            }
        } else {
            loadingDialog.show();
            new LeisureActivitiesAPI(this, actId);
        }

    }

    @OnClick({R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                //报名
                Intent intent = new Intent(getApplicationContext(), ApplyActivityAct.class);
                intent.putExtra("actId",actId);
                intent.putExtra("img",model.getObj().getActivityImg());
                intent.putExtra("content",model.getObj().getActSummary());
                startActivity(intent);
                finish();
                break;

        }
    }






    @Override
    public void leisureActivitiesSuccess(LeisureActivitiesDetailsModel drugSetModel) {
        loadingDialog.dismiss();
        model = drugSetModel;
        LeisureActivitiesDetailsModel.ObjBean obj = model.getObj();
        if (obj!=null){
            //图片
            Glide.with(getApplicationContext()).load(obj.getActivityImg()).into(ivImg);
            //活动概述
            tvContent.setText(obj.getActSummary());
            //活动时间
            tvTime.setText("活动时间: "+obj.getStartTime()+"-"+obj.getEndTime());
            //活动地点
            tvAddress.setText("活动地点: "+obj.getAddress());
            //消耗积分
            tvGrade.setText("消耗积分: "+obj.getActIntegral()+"分/次");
            //主办方
            tvSponsorName.setText("活动主办方: "+obj.getSponsorName());
            //主办方联系
            tvSponsorTelephone.setText("主办方联系方式: "+obj.getSponsorTelephone());
            //活动内容
            tvActContent.setText("活动内容: "+obj.getActContent());
        }
    }

    @Override
    public void leisureActivitiesError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
