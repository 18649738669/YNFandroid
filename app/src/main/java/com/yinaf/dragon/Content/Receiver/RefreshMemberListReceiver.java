package com.yinaf.dragon.Content.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by long on 2018/4/20.
 * 功能：更新成员列表的广播
 */
public class RefreshMemberListReceiver extends BroadcastReceiver {

    public static final String REFRESH_MEMBER_LIST = "REFRESH_MEMBER_LIST";

    public RefreshMemberListListener listener = null;

    public RefreshMemberListReceiver(RefreshMemberListListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(REFRESH_MEMBER_LIST.equals(action)){
            listener.receiverRefreshMemberList();
        }
    }

    public interface RefreshMemberListListener{
        public void receiverRefreshMemberList();
    }

    /**
     * 发送广播
     * @param context
     */
    public static void send(Context context){
        Intent intent = new Intent(REFRESH_MEMBER_LIST);
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播接收器
     * @param context
     * @param refreshMemberListReceiver
     */
    public static void register(Context context, RefreshMemberListReceiver refreshMemberListReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RefreshMemberListReceiver.REFRESH_MEMBER_LIST);
        context.registerReceiver(refreshMemberListReceiver, intentFilter);
    }

    /**
     * 取消注册广播接收器
     * @param context
     * @param refreshMemberListReceiver
     */
    public static void unregister(Context context, RefreshMemberListReceiver refreshMemberListReceiver){
        if(refreshMemberListReceiver != null){
            context.unregisterReceiver(refreshMemberListReceiver);
        }
    }
}
