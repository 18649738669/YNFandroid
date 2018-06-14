package com.yinaf.dragon.Content.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by long on 18/5/10.
 * 功能：微聊的内容实体类
 */

public class ChatListBean implements Parcelable{
    private double time;  //时间
    private String img;  //成员头像
    private String name;  //成员名称
    private int messageNum;  //消息数
    private int memberId;  //成员ID
    private String watch_id;  //腕表ID
    private String lastSendTime;  //最后聊天时间



       public ChatListBean(Parcel in) {
        time = in.readDouble();
        img = in.readString();
        name = in.readString();
        messageNum = in.readInt();
        watch_id = in.readString();
        lastSendTime=in.readString();
        memberId=in.readInt();
    }

    public ChatListBean() {
    }


    public static final Creator<ChatListBean> CREATOR = new Creator<ChatListBean>() {
        @Override
        public ChatListBean createFromParcel(Parcel in) {
            return new ChatListBean(in);
        }

        @Override
        public ChatListBean[] newArray(int size) {
            return new ChatListBean[size];
        }
    };

    public String getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(String lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }


    public String getWatch_id() {
        return watch_id;
    }

    public void setWatch_id(String watch_id) {
        this.watch_id = watch_id;
    }


    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(time);
        dest.writeString(img);
        dest.writeString(name);
        dest.writeInt(messageNum);
        dest.writeString(watch_id);
        dest.writeString(lastSendTime);
        dest.writeInt(memberId);
    }
}
