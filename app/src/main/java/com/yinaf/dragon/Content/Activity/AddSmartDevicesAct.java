package com.yinaf.dragon.Content.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.LockAddDeviceAPI;
import com.yinaf.dragon.Content.Receiver.RefreshDoorLockReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.yinaf.dragon.externalDemo.utils.CommonUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/21.
 * 功能：添加设备页面
 */

public class AddSmartDevicesAct extends BasicAct implements LockAddDeviceAPI.LockAddDeviceAPIListener{


    @BindView(R.id.tool_bar_friends_circle_btn_back)
    RelativeLayout btnBack;
    @BindView(R.id.tool_bar_friends_circle_title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tool_bar_friends_circle_right_text)
    RelativeLayout rightText;
    @BindView(R.id.add_smart_devices_et_number)
    EditText etNumber;
    @BindView(R.id.add_smart_devices_tv_btn)
    TextView tvBtn;
    @BindView(R.id.add_smart_devices_et_name)
    EditText etName;

    /**
     * 需要的权限
     */
    private static final String[] INITIAL_PERMS={
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private static final int INITIAL_REQUEST=1337;

    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;

    LoadingDialog loadingDialog;


    public AddSmartDevicesAct() {
        super(R.layout.act_add_smart_devices, R.string.title_activity_add_smart_devices, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddSmartDevicesAct.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        tvRight.setText(R.string.add_family_tv_next_step);
        title.setText(R.string.title_activity_add_smart_devices);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tool_bar_friends_circle_btn_back, R.id.tool_bar_friends_circle_right_text, R.id.add_smart_devices_tv_btn})
    public void onViewClicked(View view) {
        String number = etNumber.getText().toString();
        String name = etName.getText().toString();
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_btn_back:
                finish();
                break;
            case R.id.tool_bar_friends_circle_right_text:

                if (number.equals("")){
                    TipUtils.showTip("请填写设备12位网关序列号！");
                    return;
                }
                if (name.equals("")){
                    TipUtils.showTip("请填写设备名称！");
                    return;
                }
                loadingDialog.show();
                new LockAddDeviceAPI(this,number,name);


                break;
            case R.id.add_smart_devices_tv_btn:
                boolean permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED ;

                if (!permission ) {

                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                    }else{
                        TipUtils.showTip("请先在手机的设置中心，设置允许访问相机、相册的权限");
                    }
                }else {
                    //打开二维码扫描界面
                    if (CommonUtil.isCameraCanUse()) {
                        Intent intent = new Intent(this, CaptureAct.class);
                        intent.putExtra("isWatchId",1);
                        intent.putExtra("memberId", SPHelper.getInt(Builds.SP_USER,"memberId"));
                        startActivityForResult(intent, REQUEST_CODE);
                    } else {
                        Toast.makeText(this, "请打开此应用的摄像头权限！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来
            etNumber.setText(scanResult);
        }
    }

    /**
     * 添加设备
     * @param content
     */
    @Override
    public void lockAddDeviceSuccess(JSONObject content) {

        TipUtils.showTip("添加成功！");
        loadingDialog.dismiss();
        RefreshDoorLockReceiver.send(this);
        finish();

    }

    @Override
    public void lockAddDeviceError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);

    }
}
