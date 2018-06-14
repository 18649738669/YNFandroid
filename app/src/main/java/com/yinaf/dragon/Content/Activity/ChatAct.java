package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/05/10.
 * 功能：微聊的list
 */

public class ChatAct extends BasicAct {

    @BindView(R.id.activity_chat)
    RelativeLayout activityChat;

    public ChatAct() {
        super(R.layout.act_chat, R.string.title_activity_chat);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChatAct.class);
        context.startActivity(intent);
    }


    @Override
    public void initView() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
