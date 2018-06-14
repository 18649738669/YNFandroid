package com.yinaf.dragon.Content.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by long on 17/4/11.
 * 存的是聊天消息的手表的最后一条时间
 */

public class ChatLastTimeSpf {
    private SharedPreferences chatLastTimeSpf;

    public ChatLastTimeSpf(Context context) {
        chatLastTimeSpf = context.getSharedPreferences("chatLastTimeSpf", Context.MODE_PRIVATE);
    }

    public void setLastTime(String watchId,String time){
        SharedPreferences.Editor editor = chatLastTimeSpf.edit();
        editor.putString(watchId, time);
        editor.apply();
    }


    public String getLastTime(String watchId) {
        String time=chatLastTimeSpf.getString(watchId,"");
        return time;
    }

    public void clear() {
        chatLastTimeSpf.edit().clear().apply();
    }

}
