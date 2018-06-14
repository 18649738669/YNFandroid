package com.yinaf.dragon.Content.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by long on 2018/5/11.
 * 功能：更新语音消息的广播
 */
public class RefreshVoiceReceiver extends BroadcastReceiver {

    public static final String REFRESH_VOICE = "REFRESH_VOICE";

    public RefreshVoiceListener listener = null;

    public RefreshVoiceReceiver(RefreshVoiceListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String data = intent.getStringExtra("data");
        if(REFRESH_VOICE.equals(action)){
            listener.receiverRefreshVoice(data);
        }
    }

    public interface RefreshVoiceListener{
        public void receiverRefreshVoice(String data);
    }

    /**
     * 发送广播
     * @param context
     */
    public static void send(Context context,String data){
        Intent intent = new Intent(REFRESH_VOICE);
        intent.putExtra("data",data);
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播接收器
     * @param context
     * @param refreshVoiceReceiver
     */
    public static void register(Context context, RefreshVoiceReceiver refreshVoiceReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshVoiceReceiver.REFRESH_VOICE);
        context.registerReceiver(refreshVoiceReceiver, intentFilter);
    }

    /**
     * 取消注册广播接收器
     * @param context
     * @param refreshVoiceReceiver
     */
    public static void unregister(Context context, RefreshVoiceReceiver refreshVoiceReceiver){
        if(refreshVoiceReceiver != null){
            context.unregisterReceiver(refreshVoiceReceiver);
        }
    }
}
