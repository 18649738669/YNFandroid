package com.yinaf.dragon.Content.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yinaf.dragon.Content.Bean.LeisureActivitiesDetailsModel;
import com.yinaf.dragon.Content.Bean.MemberActivitiesDetailsModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.MemberActivitiesAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的活动详情
 */
public class MemberActivitiesDetailsAct extends BasicAct implements MemberActivitiesAPI.MemberActivitiesListener {


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

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_member)
    TextView tvMember;
    private LoadingDialog loadingDialog;
    private MemberActivitiesDetailsModel model;
    private String actId;

    public MemberActivitiesDetailsAct() {
        super(R.layout.member_activity_details_act, R.string.leisure_activity, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);
    }


    @Override
    public void initView() {
        Intent intent = getIntent();
        loadingDialog = LoadingDialog.showDialog(this);
        actId = intent.getStringExtra("actId");
        toolBarFriendsCircleTitle.setText(intent.getStringExtra("title"));


        if (!NetworkUtils.isNetworkConnect(getApplicationContext())) {
            String string = SPHelper.getString(Builds.SP_USER, actId + "MemberActivities");
            if (string != null && !TextUtils.isEmpty(string)) {
                model = JSONUtils.parseJson(string, MemberActivitiesDetailsModel.class);
                MemberActivitiesDetailsModel.ObjBean obj = model.getObj();
                //图片
                Glide.with(getApplicationContext()).load(obj.getActivityImg()).into(ivImg);
                //活动账号
                tvPhone.setText("享受活动账号: " +obj.getUserName());
                //活动成员
                tvMember.setText("享受活动成员: "+obj.getMemberName());
                //活动概述
                tvContent.setText(obj.getActSummary());
                //活动时间
                tvTime.setText("活动时间: " + obj.getStartTime() + "-" + obj.getEndTime());
                //活动地点
                tvAddress.setText("活动地点: " + obj.getActAddress());
                //消耗积分
                tvGrade.setText("消耗积分: " + obj.getActIntegral() + "分/次");
                //主办方
                tvSponsorName.setText("活动主办方: " + obj.getSponsorName());
                //主办方联系
                tvSponsorTelephone.setText("主办方联系方式: " + obj.getSponsorTelephone());
                //活动内容
                tvActContent.setText("活动内容: " + obj.getActContent());
            } else {
                TipUtils.showTip("请开启网络!");
            }
        } else {
            loadingDialog.show();
            new MemberActivitiesAPI(this, actId);
        }

    }


    @Override
    public void memberActivitiesSuccess(MemberActivitiesDetailsModel drugSetModel) {
        loadingDialog.dismiss();
        MemberActivitiesDetailsModel.ObjBean obj = drugSetModel.getObj();
        if (obj != null) {
            //图片
            Glide.with(getApplicationContext()).load(obj.getActivityImg()).into(ivImg);
            //活动概述
            tvContent.setText(obj.getActSummary());
            //活动账号
            tvPhone.setText("享受活动账号: " +obj.getUserName());
            //活动成员
            tvMember.setText("享受活动成员: "+obj.getMemberName());
            //活动时间
            tvTime.setText("活动时间: " + obj.getStartTime() + "-" + obj.getEndTime());
            //活动地点
            tvAddress.setText("活动地点: " + obj.getActAddress());
            //消耗积分
            tvGrade.setText("消耗积分: " + obj.getActIntegral() + "分/次");
            //主办方
            tvSponsorName.setText("活动主办方: " + obj.getSponsorName());
            //主办方联系
            tvSponsorTelephone.setText("主办方联系方式: " + obj.getSponsorTelephone());
            //活动内容
            tvActContent.setText("活动内容: " + obj.getActContent());
        }
    }

    @Override
    public void memberActivitiesError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
