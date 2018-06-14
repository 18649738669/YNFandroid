package com.yinaf.dragon.Content.Activity.family_set;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.model.WatchesSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.WatchesDeleteSetAPI;
import com.yinaf.dragon.Content.Net.WatchesSetAPI;
import com.yinaf.dragon.Content.Receiver.RefreshMemberReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手表设置设置
 */
public class WatchesSetAct extends BasicAct implements WatchesSetAPI.WatchesSetListener, WatchesDeleteSetAPI.WatchesDeleteSetListener {


    @BindView(R.id.tv_facility)
    TextView tvFacility;
    @BindView(R.id.tv_IMEI)
    TextView tvIMEI;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_time)
    TextView tvTime;
    private int watchId;
    private int memberId;
    private LoadingDialog loadingDialog;
    private WatchesSetModel model;


    public WatchesSetAct() {
        super(R.layout.watches_set_act, R.string.family_watches_Msg_set, R.string.tv_no_binding
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        loadingDialog = LoadingDialog.showDialog(this);

        Intent intent = getIntent();
        watchId = intent.getIntExtra("watchId", 0);
        memberId = intent.getIntExtra("memberId", 0);


        if (!NetworkUtils.isNetworkConnect(getApplicationContext())){
            String string = SPHelper.getString(Builds.SP_USER, memberId + "Watches");
            if (string!=null&&!TextUtils.isEmpty(string)){
                model = JSONUtils.parseJson(string, WatchesSetModel.class);
                WatchesSetModel.ObjBean obj = model.getObj();
                tvFacility.setText(obj.getSeriesName());
                tvIMEI.setText(obj.getWatchImei());
                tvPhone.setText(obj.getPhone());
                tvTime.setText(obj.getOpenTime());
            }else {
                TipUtils.showTip("请开启网络!");
            }
        }else {
            loadingDialog.show();
            new WatchesSetAPI(this, memberId);
        }

    }

    @OnClick({R.id.tv_phone,R.id.tool_bar_friends_circle_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_phone:
                 //更改设备绑定手机号码
                WatchesPhoneSetAct.startActivity(this,tvPhone.getText().toString());

                break;
            case R.id.tool_bar_friends_circle_right_text:
                //手表解绑
            showNormalDialog();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataEventBus eventBus) {
        new WatchesSetAPI(this, memberId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 删除对话
     */

        private void showNormalDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.alert_dialog);


            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View v = inflater.inflate(R.layout.watches_dialog, null);
            Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
            Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
            //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
            final Dialog dialog = builder.create();
            dialog.show();

            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
            dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
            btn_sure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    loadingDialog.show();
                    new WatchesDeleteSetAPI(WatchesSetAct.this,getIntent().getIntExtra("watchId",0));
                    dialog.dismiss();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();

                }
            });
        }


    @Override
    public void footstepSetSuccess(WatchesSetModel drugSetModel) {
        loadingDialog.dismiss();
        if (drugSetModel != null && drugSetModel.getObj() != null) {
            WatchesSetModel.ObjBean obj = drugSetModel.getObj();
            tvFacility.setText(obj.getSeriesName());
            tvIMEI.setText(obj.getWatchImei());
            tvPhone.setText(obj.getPhone());
            tvTime.setText(obj.getOpenTime());
        }
    }

    @Override
    public void footstepSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void watchesDeleteSetSuccess() {
        loadingDialog.dismiss();
        SPHelper.save(Builds.SP_USER,memberId+"Watches", "");
        SPHelper.save(Builds.SP_USER,"watchId",0);
        RefreshMemberReceiver.send(this);
        finish();
    }

    @Override
    public void watchesDeleteSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }


}

