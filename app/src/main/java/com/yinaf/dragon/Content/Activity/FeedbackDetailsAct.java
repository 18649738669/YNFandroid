package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.yinaf.dragon.Content.Activity.family_set.DataEventBus;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.FeedbackAssessAPI;
import com.yinaf.dragon.Content.Net.GetFeedbackAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/31.
 * 功能：反馈详情页面
 */

public class FeedbackDetailsAct extends BasicAct implements GetFeedbackAPI.GetFeedbackAPIListener,
        FeedbackAssessAPI.FeedbackAssessListener{


    @BindView(R.id.tool_bar_friends_circle_title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.feedback_details_tv_content)
    TextView tvContent;
    @BindView(R.id.feedback_details_tv_reply)
    TextView tvReply;
    @BindView(R.id.feedback_details_tv_time)
    TextView tvTime;
    @BindView(R.id.feedback_details_rb_1)
    RadioButton rb1;
    @BindView(R.id.feedback_details_rb_2)
    RadioButton rb2;
    @BindView(R.id.feedback_details_rb_3)
    RadioButton rb3;
    @BindView(R.id.feedback_details_rg_evaluation)
    RadioGroup rgEvaluation;
    @BindView(R.id.feedback_details_ll_evaluation)
    LinearLayout llEvaluation;
    @BindView(R.id.tool_bar_friends_circle_right_text)
    RelativeLayout rightText;

    int state;//反馈状态
    int assess = 0;


    LoadingDialog loadingDialog;

    public FeedbackDetailsAct() {
        super(R.layout.act_feedback_details, R.string.title_activity_feedback_details, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);

    }

    public static void startActivity(Context context, int feedbackId, int state) {
        Intent intent = new Intent(context, FeedbackDetailsAct.class);
        intent.putExtra("feedbackId", feedbackId);
        intent.putExtra("state", state);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        title.setText(R.string.title_activity_feedback_details);
        state = getIntent().getIntExtra("state",0);
        loadingDialog.show();
        new GetFeedbackAPI(this,getIntent().getIntExtra("feedbackId",0));

        switch (state) {
            case 0:
            case 1:
                rightText.setVisibility(View.GONE);
                llEvaluation.setVisibility(View.GONE);
                break;
            case 3:
                rightText.setVisibility(View.GONE);
                rgEvaluation.setEnabled(false);
                rb1.setEnabled(false);
                rb2.setEnabled(false);
                rb3.setEnabled(false);
                break;
            case 2:
                tvRight.setText("提交");
                rgEvaluation.check(R.id.feedback_details_rb_1);
                break;
        }

        rgEvaluation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == rb1.getId()){
                    assess = 0;
                }
                if (checkedId == rb2.getId()){
                    assess = 1;
                }
                if (checkedId == rb3.getId()){
                    assess = 2;
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tool_bar_friends_circle_btn_back, R.id.tool_bar_friends_circle_right_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_btn_back:
                finish();
                break;
            case R.id.tool_bar_friends_circle_right_text:
                loadingDialog.show();
                new FeedbackAssessAPI(this,getIntent().getIntExtra("feedbackId",0),assess);
                break;
        }
    }

    /**
     * 获取反馈详情
     * @param content
     */
    @Override
    public void getFeedbackSuccess(JSONObject content) {

        tvContent.setText("问题：" + JSONUtils.getString(content,"feedbackContent"));
        tvReply.setText("颐纳福回复：" + JSONUtils.getString(content,"dealContent"));
        tvTime.setText(JSONUtils.getString(content,"dealTime"));
        assess = JSONUtils.getInt(content,"assess");
        switch (assess){
            case 0:rgEvaluation.check(R.id.feedback_details_rb_1);break;
            case 1:rgEvaluation.check(R.id.feedback_details_rb_2);break;
            case 2:rgEvaluation.check(R.id.feedback_details_rb_3);break;
        }
        loadingDialog.dismiss();
    }

    @Override
    public void getFeedbackError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 反馈评价
     * @param content
     */
    @Override
    public void feedbackAssessSuccess(JSONArray content) {
        loadingDialog.dismiss();
        TipUtils.showTip("评价成功");
        EventBus.getDefault().post(new DataEventBus(1, 0, new DrugSetModel.ObjBean()));
        finish();
    }

    @Override
    public void feedbackAssessError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);

    }
}
