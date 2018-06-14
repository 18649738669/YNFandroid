package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.yinaf.dragon.Content.Bean.DoorLock;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.LockDeviceOperationAPI;
import com.yinaf.dragon.Content.Net.LockGetAlldeviceAPI;
import com.yinaf.dragon.Content.Receiver.RefreshDoorLockReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/18.
 * 功能：智能设备
 */

public class SmartDevicesAct extends BasicAct implements LockGetAlldeviceAPI.LockGetAlldeviceAPIListener,
        LockDeviceOperationAPI.LockDeviceOperationAPIListener,RefreshDoorLockReceiver.RefreshDoorLockListener{
    @BindView(R.id.tool_bar_friends_circle_btn_back)
    RelativeLayout toolBarFriendsCircleBtnBack;
    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tool_bar_friends_circle_right_text)
    RelativeLayout toolBarFriendsCircleRightText;
    @BindView(R.id.smart_devices_ll)
    LinearLayout smartDevicesLl;

    LoadingDialog loadingDialog;

    DoorLock doorLock = new DoorLock();

    RefreshDoorLockReceiver refreshDoorLockReceiver;



    public SmartDevicesAct() {
        super(R.layout.act_smart_devices, R.string.title_activity_smart_devices, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SmartDevicesAct.class);
        context.startActivity(intent);
    }


    @Override
    public void initView() {

        refreshDoorLockReceiver = new RefreshDoorLockReceiver(this);
        RefreshDoorLockReceiver.register(this,refreshDoorLockReceiver);
        loadingDialog = LoadingDialog.showDialog(this);
        toolBarFriendsCircleTitle.setText(R.string.title_activity_smart_devices);
        tvRight.setText("添加设备");
        loadingDialog.show();
        new LockGetAlldeviceAPI(this);

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
                AddSmartDevicesAct.startActivity(this);
                break;
        }
    }

    /**
     * 添加智能门锁
     */
    private View createViewDoorLock(final DoorLock doorLock){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(this).inflate(R.layout.view_smart_door_lock,null);
        view.setLayoutParams(lp);

        TextView name = view.findViewById(R.id.door_lock_tv_name);//名称
        ImageView set = view.findViewById(R.id.door_lock_img_set);//设置
        ImageView the_lock = view.findViewById(R.id.door_lock_img_the_lock_3);//开锁
        ImageView shut = view.findViewById(R.id.door_lock_img_the_lock_1);//关锁

        //设置
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DoorLockSetAct.startActivity(SmartDevicesAct.this,doorLock.getDeviceid(),
                        doorLock.getSubname(),doorLock.getSubid(),doorLock.getSigntime());

            }
        });

        //开锁
        the_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.show();
                new LockDeviceOperationAPI(SmartDevicesAct.this,doorLock.getDeviceid(),doorLock.getSubid(),"06");

            }
        });

        //关锁
        shut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                new LockDeviceOperationAPI(SmartDevicesAct.this,doorLock.getDeviceid(),doorLock.getSubid(),"38");

            }
        });

        return view;
    }

    /**
     * 我的设备列表
     * @param content
     */
    @Override
    public void lockGetAlldeviceSuccess(JSONArray content) {
        smartDevicesLl.removeAllViews();
        try {
            for (int i = 0; i < content.length(); i++){
                JSONObject jsonObject = content.getJSONObject(i);
                doorLock.setDeviceid(JSONUtils.getString(jsonObject,"deviceid"));
                doorLock.setSigntime(JSONUtils.getString(jsonObject,"signtime"));
                doorLock.setSubid(JSONUtils.getString(jsonObject,"subid"));
                doorLock.setSubname(JSONUtils.getString(jsonObject,"subname"));
                smartDevicesLl.addView(createViewDoorLock(doorLock));//添加智能门锁
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        loadingDialog.dismiss();

    }

    @Override
    public void lockGetAlldeviceError(long code, String msg) {

        TipUtils.showTip(msg);
        loadingDialog.dismiss();
    }

    /**
     * 操作门锁
     * @param content
     */
    @Override
    public void lockDeviceOperationSuccess(JSONObject content) {

        loadingDialog.dismiss();

    }

    @Override
    public void lockDeviceOperationError(long code, String msg) {

        TipUtils.showTip(msg);
        loadingDialog.dismiss();
    }

    /**
     * 接收到刷新门锁的广播
     */
    @Override
    public void receiverRefreshDoorLock() {
        loadingDialog.show();
        new LockGetAlldeviceAPI(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefreshDoorLockReceiver.unregister(this,refreshDoorLockReceiver);
    }
}
