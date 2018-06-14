package com.yinaf.dragon.Content.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Receiver.RefreshDoorLockReceiver;
import com.yinaf.dragon.Content.Utils.DoorLock.EspWifiAdminSimple;
import com.yinaf.dragon.Content.Utils.DoorLock.EsptouchTask;
import com.yinaf.dragon.Content.Utils.DoorLock.IEsptouchListener;
import com.yinaf.dragon.Content.Utils.DoorLock.IEsptouchResult;
import com.yinaf.dragon.Content.Utils.DoorLock.IEsptouchTask;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/22.
 * 功能：配置网关WIFI信息
 */

public class DoorLockWifiAct extends BasicAct {


    @BindView(R.id.door_lock_wifi_et_name)
    TextView tvName;
    @BindView(R.id.door_lock_wifi_et_pwd)
    EditText etPwd;
    @BindView(R.id.door_lock_wifi_radio)
    ImageButton radio;
    @BindView(R.id.door_lock_wifi_btn)
    Button btn;

    LoadingDialog loadingDialog;

    private EspWifiAdminSimple mWifiAdmin;

    public DoorLockWifiAct() {
        super(R.layout.act_door_lock_wifi, R.string.door_lock_wifi_title, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DoorLockWifiAct.class);
        context.startActivity(intent);
    }


    @Override
    public void initView() {

        mWifiAdmin = new EspWifiAdminSimple(this);

        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            tvName.setText(apSsid);
        } else {
            tvName.setText("");
        }

        loadingDialog = LoadingDialog.showDialog(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.door_lock_wifi_switch, R.id.door_lock_wifi_ll_radio, R.id.door_lock_wifi_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.door_lock_wifi_switch:
                break;
            case R.id.door_lock_wifi_ll_radio:
                etPwd.setInputType(InputType.TYPE_CLASS_TEXT);
                radio.setImageResource(R.drawable.choose_yes);
                break;
            case R.id.door_lock_wifi_btn:
                String apSsid = tvName.getText().toString();
                String apPassword = etPwd.getText().toString();
                String apBssid = mWifiAdmin.getWifiConnectedBssid();
                new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword, "0");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // display the connected ap's ssid
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            tvName.setText(apSsid);
        } else {
            tvName.setText("");
        }
        // check whether the wifi is connected
        boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
        btn.setEnabled(!isApSsidEmpty);
    }

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the wifi";
//                Toast.makeText(DoorLockWifiAct.this, text,
//                        Toast.LENGTH_LONG).show();
            }

        });
    }

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

        private ProgressDialog mProgressDialog;

        private IEsptouchTask mEsptouchTask;
        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(DoorLockWifiAct.this);
            mProgressDialog
                    .setMessage("正在连接，请稍等...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    synchronized (mLock) {

                        if (mEsptouchTask != null) {
                            mEsptouchTask.interrupt();
                        }
                    }
                }
            });
            mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "加载中...", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            mProgressDialog.show();
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(false);
        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                // !!!NOTICE
                String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
                String apBssid = params[1];
                String apPassword = params[2];
                String taskResultCountStr = params[3];
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, DoorLockWifiAct.this);
                mEsptouchTask.setEsptouchListener(myListener);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setEnabled(true);
            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
                    "确认");
            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("连接成功");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("");
                    }
                    mProgressDialog.setMessage(sb.toString());
                    RefreshDoorLockReceiver.send(DoorLockWifiAct.this);
                } else {
                    mProgressDialog.setMessage("连接失败");
                }
            }
        }
    }

}
