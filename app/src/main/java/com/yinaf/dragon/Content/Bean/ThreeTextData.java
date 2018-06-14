package com.yinaf.dragon.Content.Bean;

/**
 * Created by long on 2018/04/26.
 * 功能：三个text数据的实体类
 */

public class ThreeTextData {

    public String time;//时间

    public String content; //内容一

    public String content1; //内容二

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
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }


}
