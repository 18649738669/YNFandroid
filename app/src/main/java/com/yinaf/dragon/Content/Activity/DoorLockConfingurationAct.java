package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/22.
 * 功能：配置流程
 */

public class DoorLockConfingurationAct extends BasicAct {


    public DoorLockConfingurationAct() {
        super(R.layout.act_door_lock_configuration, R.string.door_lock_configuration, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DoorLockConfingurationAct.class);
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

    @OnClick(R.id.door_lock_btn_configuration)
    public void onViewClicked() {

        DoorLockWifiAct.startActivity(this);

    }
}
