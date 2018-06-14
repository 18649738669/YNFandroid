package com.yinaf.dragon.Content.Bean;

import java.util.List;

/**
 * Created by long on 2018/04/26.
 * 功能：计步数据的实体类
 */

public class StepGuageData {

    public String date;//日期

    public List<StepGuageDataChild> list;//子list

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StepGuageDataChild> getList() {
        return list;
    }

    public void setList(List<StepGuageDataChild> list) {
        this.list = list;
    }
}
