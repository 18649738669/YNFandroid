package com.yinaf.dragon.Content.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.yinaf.dragon.Content.Activity.ChatListAct;
import com.yinaf.dragon.Content.Activity.ChatMainAct;
import com.yinaf.dragon.Content.Activity.HomeAct;
import com.yinaf.dragon.Content.Activity.PlaceAct;
import com.yinaf.dragon.Content.Bean.ChatListBean;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.App;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.LogUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by long on 2018/4/20.
 * 功能：极光推送的广播接收器
 */
public class JPushReceiver extends BroadcastReceiver {

    DatabaseHelper dbHelper;


    @Override
    public void onReceive(Context context, Intent intent) {

        dbHelper = new DatabaseHelper(context,SPHelper.getString(Builds.SP_USER, "userName"));


        Bundle bundle = intent.getExtras();

        LogUtils.d("intent.getAction()-->" + intent.getAction());

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Intent openIntent = new Intent(context, ChatListAct.class);
            openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getContext().startActivity(openIntent);
        }
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            UIUtils.wakeUpAndUnlock();
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);
            JSONObject jsonObject = JSONUtils.toJSONObject(extras);
            String content = JSONUtils.getString(jsonObject, "content");

            String name = SPHelper.getString(Builds.SP_USER, "phone") + "," + StringUtils.getCurrentTimeStamp();

        }
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //接收到自定义消息
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String msg_id = bundle.getString(JPushInterface.EXTRA_MSG_ID);


            JSONObject jsonObject = JSONUtils.toJSONObject(extras);
            String type = JSONUtils.getString(jsonObject, "type");

            if (type.equals("location")){//接收到实时定位的推送消息
                RefreshPlaceReceiver.send(context,extras);
            }
            if (type.equals("voice")){//接收到腕表语音消息的推送
                RefreshVoiceReceiver.send(context,extras);
//                ChatListBean chatBean = new ChatListBean();
//                Member member = dbHelper.selectMemberByMemberId(Integer.parseInt(JSONUtils.getString(jsonObject, "memberId")));
//                chatBean.setMemberId(member.getMemberId());
//                chatBean.setImg(member.getImage());
//                chatBean.setName(member.getRealName());
//                chatBean.setWatch_id(member.getWatchId() + "");
//                showNotification(context,"语音消息","您有一条新的语音消息",0,chatBean);
            }

        }
    }


    /**
     * 显示通知
     *
     * @param title
     * @param message
     * @param notificationId
     */
    public static void showNotification(Context context, String title, String message, int notificationId,ChatListBean chatBean) {
        NotificationManager manager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, ChatMainAct.class);
        intent.putExtra("WATCH_BEAN", chatBean);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setTicker(title)  //设置状态栏的显示的信息
                .setContentTitle(title)   //设置下拉列表里的标题
                .setContentText(message)  //设置上下文内容
                .setWhen(System.currentTimeMillis())  //设置时间发生时间
                .setSmallIcon(R.drawable.logo)//设置状态栏里面的图标（小图标）
                .setLargeIcon(BitmapFactory.decodeResource(App.getContext().getResources(), R
                        .drawable.logo))
                //下拉列表里面的图标（大图标）
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent)
                .getNotification();
        manager.notify(notificationId, notification);
        UIUtils.wakeUpAndUnlock();
    }

}
