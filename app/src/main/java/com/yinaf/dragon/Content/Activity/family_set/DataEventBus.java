package com.yinaf.dragon.Content.Activity.family_set;

import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;

/**
 * 数据更新
 */
public class DataEventBus {

    private int type;//0添加 1 修改

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public DrugSetModel.ObjBean getObjBean() {
        return objBean;
    }

    public void setObjBean(DrugSetModel.ObjBean objBean) {
        this.objBean = objBean;
    }

    private int pos;//修改的列表位置
    private DrugSetModel.ObjBean objBean;

    public DataEventBus(int type, int pos, DrugSetModel.ObjBean objBean) {
        this.type = type;
        this.pos = pos;
        this.objBean = objBean;
    }
}
