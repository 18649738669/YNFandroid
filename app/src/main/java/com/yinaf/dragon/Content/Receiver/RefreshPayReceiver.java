package com.yinaf.dragon.Content.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by long on 2018/5/11.
 * 功能：更新语音消息的广播
 */
public class RefreshPayReceiver extends BroadcastReceiver {

    public static final String REFRESH_PAY = "REFRESH_PAY";

    public RefreshPayListener listener = null;

    public RefreshPayReceiver(RefreshPayListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int code = intent.getIntExtra("code",0);
        if(REFRESH_PAY.equals(action)){
            listener.receiverRefreshPay(code);
        }
    }

    public interface RefreshPayListener{
        public void receiverRefreshPay(int code);
    }

    /**
     * 发送广播
     * @param context
     */
    public static void send(Context context,int code){
        Intent intent = new Intent(REFRESH_PAY);
        intent.putExtra("code",code);
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播接收器
     * @param context
     * @param refreshPayReceiver
     */
    public static void register(Context context, RefreshPayReceiver refreshPayReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshPayReceiver.REFRESH_PAY);
        context.registerReceiver(refreshPayReceiver, intentFilter);
    }

    /**
     * 取消注册广播接收器
     * @param context
     * @param refreshPayReceiver
     */
    public static void unregister(Context context, RefreshPayReceiver refreshPayReceiver){
        if(refreshPayReceiver != null){
            context.unregisterReceiver(refreshPayReceiver);
        }
    }
}
