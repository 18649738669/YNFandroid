package com.yinaf.dragon.Content.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by long on 2018/4/20.
 * 功能：更新成员实时定位的广播
 */
public class RefreshPlaceReceiver extends BroadcastReceiver {

    public static final String REFRESH_PLACE = "REFRESH_PLACE";

    public RefreshPlaceListener listener = null;

    public RefreshPlaceReceiver(RefreshPlaceListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String data = intent.getStringExtra("data");
        if(REFRESH_PLACE.equals(action)){
            listener.receiverRefreshPlace(data);
        }
    }

    public interface RefreshPlaceListener{
        public void receiverRefreshPlace(String data);
    }

    /**
     * 发送广播
     * @param context
     */
    public static void send(Context context,String data){
        Intent intent = new Intent(REFRESH_PLACE);
        intent.putExtra("data",data);
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播接收器
     * @param context
     * @param refreshPlaceReceiver
     */
    public static void register(Context context, RefreshPlaceReceiver refreshPlaceReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshPlaceReceiver.REFRESH_PLACE);
        context.registerReceiver(refreshPlaceReceiver, intentFilter);
    }

    /**
     * 取消注册广播接收器
     * @param context
     * @param refreshPlaceReceiver
     */
    public static void unregister(Context context, RefreshPlaceReceiver refreshPlaceReceiver){
        if(refreshPlaceReceiver != null){
            context.unregisterReceiver(refreshPlaceReceiver);
        }
    }
}
