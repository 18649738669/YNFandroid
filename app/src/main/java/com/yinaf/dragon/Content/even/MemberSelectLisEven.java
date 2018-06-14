package com.yinaf.dragon.Content.even;

import com.yinaf.dragon.Content.Bean.MemberListModel;

import java.util.List;

public class MemberSelectLisEven {


    public List<MemberListModel.ObjBean> getObj() {
        return obj;
    }

    private final List<MemberListModel.ObjBean> obj;

    public MemberSelectLisEven(List<MemberListModel.ObjBean> datas) {
        this.obj =datas;
    }
}
