package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.DataEventBus;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddFeedbackAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/30.
 * 功能：提交反馈页面
 */

public class SubmitFeedbackAct extends BasicAct implements AddFeedbackAPI.AddFeedbackListener{


    @BindView(R.id.tool_bar_friends_circle_title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.feedback_et_problem)
    EditText etProblem;
    @BindView(R.id.feedback_et_phone)
    EditText etPhone;
    @BindView(R.id.feedback_et_qq)
    EditText etQq;
    @BindView(R.id.feedback_et_email)
    EditText etEmail;
    LoadingDialog loadingDialog;
    int feedbackType;//反馈的类型

    public SubmitFeedbackAct() {
        super(R.layout.act_submit_feedback, R.string.title_activity_feedback, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);
    }

    public static void startActivity(Context context, int title, int feedbackType) {
        Intent intent = new Intent(context, SubmitFeedbackAct.class);
        intent.putExtra("title",title);
        intent.putExtra("feedbackType",feedbackType);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        feedbackType = getIntent().getIntExtra("feedbackType",0);
        title.setText(getIntent().getIntExtra("title",0));
        tvRight.setText("提交");

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
                if (etProblem.getText().toString().equals("")){
                    TipUtils.showTip("反馈的内容不允许为空！");
                    return;
                }
                loadingDialog.show();
                new AddFeedbackAPI(this,feedbackType,etProblem.getText().toString(),
                        etPhone.getText().toString(),etQq.getText().toString(),etEmail.getText().toString());
                break;
        }
    }

    /**
     * 新增反馈
     */
    @Override
    public void addFeedbackSuccess() {

        TipUtils.showTip("反馈成功！");
        loadingDialog.dismiss();
        finish();

    }

    @Override
    public void addFeedbackError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
