package com.yinaf.dragon.Content.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yinaf.dragon.Content.Adapter.MemberSelectListAdapter;
import com.yinaf.dragon.Content.Bean.MemberListModel;
import com.yinaf.dragon.Content.even.MemberSelectLisEven;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成员选择
 */
public class MemberSelectListAct extends BasicAct  {



    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    //成员列表信息
    private List<MemberListModel.ObjBean> obj;


    public MemberSelectListAct() {
        super(R.layout.member_select_list_act, R.string.member_select_list,0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //成员列表信息
        Bundle extras = getIntent().getExtras();
       obj = (List<MemberListModel.ObjBean>) extras.getSerializable("model");

        final MemberSelectListAdapter memberSelectAdapter =
                new MemberSelectListAdapter(getApplicationContext(), 0, obj);

        recyclerView.setAdapter(memberSelectAdapter);
        memberSelectAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<MemberListModel.ObjBean> datas = memberSelectAdapter.getDatas();
                for (int i = 0 ;i<datas.size();i++){
                    if (i==position){
                        datas.get(position).setSelect(true);
                    }else {
                        datas.get(i).setSelect(false);
                    }
                }
                EventBus.getDefault().post(new MemberSelectLisEven(datas));
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @OnClick(R.id.btn_add)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                 finish();
                break;

        }
    }


}
