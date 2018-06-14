package com.yinaf.dragon.Content.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by long on 2018-5-8.
 * 功能：接收下载APP的推送
 */
public class DownloadReceiver extends BroadcastReceiver {

    public static final String DOWNLOAD_ACTION = "DOWNLOAD_ACTION";

    public DownloadListener listener = null;

    public DownloadReceiver(DownloadListener listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int progress = intent.getIntExtra("progress",0);
        if(DOWNLOAD_ACTION.equals(action)){
            listener.receivedDownload(progress);
        }
    }

    public interface DownloadListener{
        public void receivedDownload(int progress);
    }

    /**
     * 发送广播
     * @param context
     */
    public static void send(Context context,int progress){
        Intent intent = new Intent(DOWNLOAD_ACTION);
        intent.putExtra("progress",progress);
        context.sendBroadcast(intent);
    }

    /**
     * 注册广播接收器
     * @param context
     * @param downloadReceiver
     */
    public static void register(Context context,DownloadReceiver downloadReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadReceiver.DOWNLOAD_ACTION);
        context.registerReceiver(downloadReceiver, intentFilter);
    }

    /**
     * 取消注册广播接收器
     * @param context
     * @param downloadReceiver
     */
    public static void unregister(Context context,DownloadReceiver downloadReceiver){
        if(downloadReceiver != null){
            context.unregisterReceiver(downloadReceiver);
        }
    }
}
