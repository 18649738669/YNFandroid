package com.yinaf.dragon.Content.Activity.family_set.model;

import java.io.Serializable;

/**
 * 成员选择器
 */
public class MemberSeleteModel implements Serializable {

    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    private String items;


    public MemberSeleteModel(String item,boolean select){
        this.items=item;
        this.isSelect=select;
    }
}
