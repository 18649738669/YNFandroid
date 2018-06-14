package com.yinaf.dragon.Content.Bean;


/**
 * Created by long on 2018/04/26.
 * 功能：两个text数据的实体类
 */

public class TwoTextData {

    public String time;//时间

    public String text;//内容

    public int type;//类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
