package com.yinaf.dragon.Content.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by long on 2018/4/20.
 * 功能：更新智能门锁的广播
 */
public class RefreshDoorLockReceiver extends BroadcastReceiver {

    public static final String REFRESH_DOOR_LOCK = "REFRESH_DOOR_LOCK";

    public RefreshDoorLockListener listener = null;

    public RefreshDoorLockReceiver(RefreshDoorLockListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(REFRESH_DOOR_LOCK.equals(action)){
            listener.receiverRefreshDoorLock();
        }
    }

    public interface RefreshDoorLockListener{
        public void receiverRefreshDoorLock();
    }

    /**
     * 发送广播
     * @param context
     */
    public static void send(Context context){
        Intent intent = new Intent(REFRESH_DOOR_LOCK);
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播接收器
     * @param context
     * @param refreshDoorLockReceiver
     */
    public static void register(Context context, RefreshDoorLockReceiver refreshDoorLockReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshDoorLockReceiver.REFRESH_DOOR_LOCK);
        context.registerReceiver(refreshDoorLockReceiver, intentFilter);
    }

    /**
     * 取消注册广播接收器
     * @param context
     * @param refreshDoorLockReceiver
     */
    public static void unregister(Context context, RefreshDoorLockReceiver refreshDoorLockReceiver){
        if(refreshDoorLockReceiver != null){
            context.unregisterReceiver(refreshDoorLockReceiver);
        }
    }
}
