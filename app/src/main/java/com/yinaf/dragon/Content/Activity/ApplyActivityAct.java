package com.yinaf.dragon.Content.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yinaf.dragon.Content.Bean.MemberIntegralDetailModel;
import com.yinaf.dragon.Content.Bean.MemberListModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.EnterActivityAPI;
import com.yinaf.dragon.Content.Net.GetMemberAPI;
import com.yinaf.dragon.Content.Net.MemberIntegralDetailAPI;
import com.yinaf.dragon.Content.even.MemberSelectLisEven;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 活动报名
 */
public class ApplyActivityAct extends BasicAct implements GetMemberAPI.GetMemberAPIListener, MemberIntegralDetailAPI.MemberIntegralDetailListener, EnterActivityAPI.EnterActivityListener {


    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_content)
    TextView tvContent;

    Button btnAdd;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_member)
    TextView tvMember;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_grade_number)
    TextView tvGradeNumber;
    @BindView(R.id.tv_grade_count)
    TextView tvGradeCount;
    private LoadingDialog loadingDialog;
    private String actId;
    private List<MemberListModel.ObjBean> beanList;
    private String img;
    private String content1;

    public ApplyActivityAct() {
        super(R.layout.apply_activity_act, R.string.apply_activity, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);
    }


    @Override
    public void initView() {
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        loadingDialog = LoadingDialog.showDialog(this);
        actId = intent.getStringExtra("actId");
        img = intent.getStringExtra("img");
        content1 = intent.getStringExtra("content");

        loadingDialog.show();
        //获取成员列表
        new GetMemberAPI(this);

    }

    @OnClick({R.id.btn_add,R.id.tv_member})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                //确定
                for (MemberListModel.ObjBean objBean : beanList) {
                    if (objBean.isSelect()) {
                        loadingDialog.show();
                        new EnterActivityAPI(this, actId, objBean.getMemberId());
                        return;
                    }
                }
                break;
            case R.id.tv_member:
                //选择成员
                Bundle bundle = new Bundle();
                bundle.putSerializable("model", (Serializable) beanList);
                Intent intent = new Intent(getApplicationContext(), MemberSelectListAct.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }


    @Override
    public void getMemberSuccess(JSONArray content) {
        beanList = new ArrayList<>();
        if (content != null && content.length() > 0) {
            for (int i = 0; i < content.length(); i++) {
                try {
                    JSONObject jsonObject = content.getJSONObject(i);
                    MemberListModel.ObjBean obj = new MemberListModel.ObjBean();
                    obj.setIsLead(JSONUtils.getInt(jsonObject, "isLead"));
                    obj.setImage(JSONUtils.getString(jsonObject, "image"));
                    obj.setRealName(JSONUtils.getString(jsonObject, "realName"));
                    obj.setMemberNum(JSONUtils.getString(jsonObject, "memberNum"));
                    obj.setRela(JSONUtils.getString(jsonObject, "rela"));
                    obj.setMemberId(JSONUtils.getInt(jsonObject, "memberId"));
                    if (i == 0) {
                        obj.setSelect(true);
                    } else {
                        obj.setSelect(false);
                    }

                    beanList.add(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            new MemberIntegralDetailAPI(this, actId, beanList.get(0).getMemberId());
        }


    }

    @Subscribe
    public void onEven(MemberSelectLisEven even) {
        if (even != null && even.getObj().size() > 0) {
            beanList = even.getObj();
            for (MemberListModel.ObjBean objBean : even.getObj()) {
                if (objBean.isSelect()) {
                    loadingDialog.show();
                    new MemberIntegralDetailAPI(this, actId, objBean.getMemberId());
                    return;
                }
            }
        }
    }

    @Override
    public void getMemberError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }


    @Override
    public void memberIntegralDetailSuccess(MemberIntegralDetailModel drugSetModel) {
        loadingDialog.dismiss();
        if (drugSetModel != null) {
            MemberIntegralDetailModel.ObjBean obj = drugSetModel.getObj();
            //图片
            Glide.with(getApplicationContext()).load(img).into(ivImg);
            //活动概述
            tvContent.setText(content1);
            //账号
            tvPhone.setText(obj.getUser_name());
            //成员名称
            tvMember.setText(obj.getReal_name());
            //成员拥有积分
            tvGrade.setText(obj.getIntegral_num() + "");
            //消耗积分
            tvGradeNumber.setText(obj.getAct_integral() + "");
            //合计积分
            tvGradeCount.setText(obj.getAct_integral() + "");
        }
    }

    @Override
    public void memberIntegralDetailError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void enterActivitySuccess(String drugSetModel) {
        TipUtils.showTip(drugSetModel);
        finish();
    }

    @Override
    public void enterActivityError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
