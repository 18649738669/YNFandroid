package com.yinaf.dragon.wxapi;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yinaf.dragon.Content.Dialog.PayDialog;
import com.yinaf.dragon.Content.Dialog.PayStateDialog;
import com.yinaf.dragon.Content.Receiver.RefreshPayReceiver;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.R;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

	private PayStateDialog payStateDialog;//支付结果弹窗


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, "wx7e6fb6e371626009");
//		api.registerApp("wx7e6fb6e371626009");
		api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@SuppressLint("LongLogTag")
	@Override
	public void onReq(BaseReq req) {

	}

	@SuppressLint("LongLogTag")
	@Override
	public void onResp(BaseResp resp) {
		Log.e(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0){
				payStateDialog = new PayStateDialog(this,"支付成功");
				payStateDialog.show();
			}else if (resp.errCode == -1){
				payStateDialog = new PayStateDialog(this,"支付失败");
				payStateDialog.show();
			}else if (resp.errCode == -2){
				payStateDialog = new PayStateDialog(this,"支付取消");
				payStateDialog.show();
			}else{
				payStateDialog = new PayStateDialog(this,"支付失败"+resp.errCode);
				payStateDialog.show();
			}
			RefreshPayReceiver.send(this,resp.errCode);
			payStateDialog.setListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					payStateDialog.dismiss();
					finish();
				}
			});
		}
	}
}