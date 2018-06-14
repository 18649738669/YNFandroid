package com.yinaf.dragon.Content.Bean;

import com.google.gson.annotations.Expose;

/**
 * Created by zhangli on 17/3/29.
 * 保存聊天的语音
 */

public class ChatMessage {

    public int m_id;//语音ID

    public String voice_url;//语音地址

    public  String img;//头像

    public int send_type;//发送消息来源（0：腕表发送  1：APP发送）

    public int voice_length;//语音长度

    public int read_status;//是否已读 0未读  1已读

    public String create_time;//发送时间

    public double create_time_double;//发送时间  时间戳格式

    public int is_send;//是否发送消息失败（100：发送中  200：成功 300：发送失败）

    public int watchId;//腕表id

    public int memberId;//成员id

    public int getWatchId() {
        return watchId;
    }

    public void setWatchId(int watchId) {
        this.watchId = watchId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public double getCreate_time_double() {
        return create_time_double;
    }

    public void setCreate_time_double(double create_time_double) {
        this.create_time_double = create_time_double;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getVoice_url() {
        return voice_url;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSend_type() {
        return send_type;
    }

    public void setSend_type(int send_type) {
        this.send_type = send_type;
    }

    public int getVoice_length() {
        return voice_length;
    }

    public void setVoice_length(int voice_length) {
        this.voice_length = voice_length;
    }

    public int getRead_status() {
        return read_status;
    }

    public void setRead_status(int read_status) {
        this.read_status = read_status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getIs_send() {
        return is_send;
    }

    public void setIs_send(int is_send) {
        this.is_send = is_send;
    }
}
