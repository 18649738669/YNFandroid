package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.LockDeviceOperationAPI;
import com.yinaf.dragon.Content.Receiver.RefreshDoorLockReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/22.
 * 功能：门锁设置
 */

public class DoorLockSetAct extends BasicAct implements LockDeviceOperationAPI.LockDeviceOperationAPIListener{


    @BindView(R.id.door_lock_set_name)
    TextView name;
    @BindView(R.id.door_lock_set_gateway_number)
    TextView gatewayNumber;
    @BindView(R.id.door_lock_set_lock_number)
    TextView lockNumber;
    @BindView(R.id.door_lock_set_sign_time)
    TextView signTime;

    LoadingDialog loadingDialog;

    public DoorLockSetAct() {
        super(R.layout.act_door_lock_set, R.string.family_tv_set_up_the, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);

    }

    public static void startActivity(Context context, String deviceid, String subname, String subid, String signtime) {
        Intent intent = new Intent(context, DoorLockSetAct.class);
        intent.putExtra("deviceid", deviceid);
        intent.putExtra("subname", subname);
        intent.putExtra("subid", subid);
        intent.putExtra("signtime", signtime);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        name.setText(getIntent().getStringExtra("subname"));
        signTime.setText(getIntent().getStringExtra("signtime"));
        gatewayNumber.setText(getIntent().getStringExtra("deviceid"));
        lockNumber.setText(getIntent().getStringExtra("subid"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.door_lock_set_gateway_information, R.id.door_lock_set_unbinding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.door_lock_set_gateway_information:
                DoorLockConfingurationAct.startActivity(this);
                break;
            case R.id.door_lock_set_unbinding:
                loadingDialog.show();
                new LockDeviceOperationAPI(this,getIntent().getStringExtra("deviceid"),getIntent().getStringExtra("subid"),"08");

                break;
        }
    }

    /**
     * 操作门锁
     * @param content
     */
    @Override
    public void lockDeviceOperationSuccess(JSONObject content) {
        loadingDialog.dismiss();
        TipUtils.showTip("解绑成功");
        RefreshDoorLockReceiver.send(this);
        finish();
    }

    @Override
    public void lockDeviceOperationError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
