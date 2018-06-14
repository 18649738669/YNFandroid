package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/30.
 * 功能：意见反馈页面
 */

public class FeedbackAct extends BasicAct {


    @BindView(R.id.tool_bar_friends_circle_title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;

    public FeedbackAct() {
        super(R.layout.act_feedback, R.string.title_activity_feedback, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FeedbackAct.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        title.setText(R.string.title_activity_feedback);
        tvRight.setText(R.string.title_activity_my_feedback);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tool_bar_friends_circle_btn_back, R.id.tool_bar_friends_circle_right_text, R.id.feedback_tv_application, R.id.feedback_tv_watches, R.id.feedback_tv_power_consumption, R.id.feedback_tv_watch_appearance, R.id.feedback_tv_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_btn_back:
                finish();
                break;
            case R.id.tool_bar_friends_circle_right_text:
                MyFeedbackAct.startActivity(this);
                break;
            case R.id.feedback_tv_application:
                SubmitFeedbackAct.startActivity(this,R.string.feedback_tv_application,1);
                break;
            case R.id.feedback_tv_watches:
                SubmitFeedbackAct.startActivity(this,R.string.feedback_tv_watches,2);
                break;
            case R.id.feedback_tv_power_consumption:
                SubmitFeedbackAct.startActivity(this,R.string.feedback_tv_power_consumption,3);
                break;
            case R.id.feedback_tv_watch_appearance:
                SubmitFeedbackAct.startActivity(this,R.string.feedback_tv_watch_appearance,4);
                break;
            case R.id.feedback_tv_other:
                SubmitFeedbackAct.startActivity(this,R.string.feedback_tv_other,5);
                break;
        }
    }
}
