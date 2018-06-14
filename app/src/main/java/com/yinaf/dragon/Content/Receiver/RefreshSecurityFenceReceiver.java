package com.yinaf.dragon.Content.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by long on 2018/4/20.
 * 功能：更新安全围栏列表的广播
 */
public class RefreshSecurityFenceReceiver extends BroadcastReceiver {

    public static final String REFRESH_SECURITY_FENCE = "REFRESH_SECURITY_FENCE";

    public RefreshSecurityFenceListener listener = null;

    public RefreshSecurityFenceReceiver(RefreshSecurityFenceListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(REFRESH_SECURITY_FENCE.equals(action)){
            listener.receiverRefreshSecurityFence();
        }
    }

    public interface RefreshSecurityFenceListener{
        public void receiverRefreshSecurityFence();
    }

    /**
     * 发送广播
     * @param context
     */
    public static void send(Context context){
        Intent intent = new Intent(REFRESH_SECURITY_FENCE);
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播接收器
     * @param context
     * @param refreshSecurityFenceReceiver
     */
    public static void register(Context context, RefreshSecurityFenceReceiver refreshSecurityFenceReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshSecurityFenceReceiver.REFRESH_SECURITY_FENCE);
        context.registerReceiver(refreshSecurityFenceReceiver, intentFilter);
    }

    /**
     * 取消注册广播接收器
     * @param context
     * @param refreshSecurityFenceReceiver
     */
    public static void unregister(Context context, RefreshSecurityFenceReceiver refreshSecurityFenceReceiver){
        if(refreshSecurityFenceReceiver != null){
            context.unregisterReceiver(refreshSecurityFenceReceiver);
        }
    }
}
