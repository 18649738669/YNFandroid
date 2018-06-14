package com.yinaf.dragon.Content.Bean;

/**
 * Created by long on 2018/05/08.
 * 功能：健康警报实体类
 */

public class AnAlarm {

    public int image;//图片

    public String title;//标题

    public String name;//成员名字

    public String time;//时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
