package com.yinaf.dragon.Content.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by long on 2018/4/20.
 * 功能：更新成员的广播
 */
public class RefreshMemberReceiver extends BroadcastReceiver {

    public static final String REFRESH_MEMBER = "REFRESH_MEMBER";

    public RefreshMemberListener listener = null;

    public RefreshMemberReceiver(RefreshMemberListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(REFRESH_MEMBER.equals(action)){
            listener.receiverRefreshMember();
        }
    }

    public interface RefreshMemberListener{
        public void receiverRefreshMember();
    }

    /**
     * 发送广播
     * @param context
     */
    public static void send(Context context){
        Intent intent = new Intent(REFRESH_MEMBER);
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播接收器
     * @param context
     * @param refreshMemberReceiver
     */
    public static void register(Context context, RefreshMemberReceiver refreshMemberReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshMemberReceiver.REFRESH_MEMBER);
        context.registerReceiver(refreshMemberReceiver, intentFilter);
    }

    /**
     * 取消注册广播接收器
     * @param context
     * @param refreshMemberReceiver
     */
    public static void unregister(Context context, RefreshMemberReceiver refreshMemberReceiver){
        if(refreshMemberReceiver != null){
            context.unregisterReceiver(refreshMemberReceiver);
        }
    }
}
