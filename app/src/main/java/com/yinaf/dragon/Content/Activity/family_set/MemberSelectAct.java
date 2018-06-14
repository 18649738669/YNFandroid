package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yinaf.dragon.Content.Activity.family_set.adapter.MemberSelectAdapter;
import com.yinaf.dragon.Content.Activity.family_set.model.MemberSeleteModel;
import com.yinaf.dragon.Content.Activity.family_set.widget.CustomDatePicker;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.MemberUpdateSetAPI;
import com.yinaf.dragon.Content.Net.MySettingUpdateSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options1Items;
import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options2Items;
import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options3Items;

/**
 * 成员基本信息 填写选择
 */
public class MemberSelectAct extends BasicAct implements MemberUpdateSetAPI.MemberUpdateSetListener, MySettingUpdateSetAPI.MySettingUpdateSetListener {

    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    /**
     * 信息设置参数 key
     */
    private String params;
    /**
     *信息设置 位置
     */
    private int position;
    private int memberId;
    private String content;
    private List<MemberSeleteModel> seleteList;
    private LoadingDialog loadingDialog;


    public MemberSelectAct() {
        super(R.layout.member_list_act, R.string.family_member_msg_set, R.string.txt_save
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        loadingDialog = LoadingDialog.showDialog(this);
        //获取信息
        Intent intent = getIntent();
        toolBarFriendsCircleTitle.setText(intent.getStringExtra("title"));
        content = intent.getStringExtra("content");
        params = intent.getStringExtra("params");
        position = intent.getIntExtra("position", 0);
        memberId = intent.getIntExtra("memberId", 0);

        //5 性别 6 血型 民族 婚姻 工作情况 职业 文化 居住 子女距离
        if (memberId==-1){
            seleteList = getSeleteList(params);
        }else {
            seleteList = getSeleteList(params);
        }

        if (!TextUtils.isEmpty(content)){
            for (MemberSeleteModel model: seleteList){
                if (model.getItems().equals(content)){
                    model.setSelect(true);
                    break;
                }
            }
        }
        final MemberSelectAdapter memberSelectAdapter = new MemberSelectAdapter(getApplicationContext(), 0, seleteList);
        recyclerView.setAdapter(memberSelectAdapter);
        memberSelectAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<MemberSeleteModel> datas = memberSelectAdapter.getDatas();
                for (int i = 0 ;i<datas.size();i++){
                    if (i==position){
                        datas.get(position).setSelect(true);
                    }else {
                        datas.get(i).setSelect(false);
                    }
                }
                memberSelectAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    @OnClick(R.id.tool_bar_friends_circle_right_text)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_right_text:
                if (seleteList!=null&&seleteList.size()>0){
                    for (MemberSeleteModel model: seleteList){
                        if (model.isSelect()){
                            content = model.getItems();
                            if (content.equals("男")){
                                content ="1";
                            }else if(content.equals("女")) {
                                content ="2";
                            }
                            break;
                        }
                    }
                }
                loadingDialog.show();
                if (memberId==-1){
                    new MySettingUpdateSetAPI(this, params, content);
                }else {

                    new MemberUpdateSetAPI(this, memberId, params, content);
                }
                break;

        }
    }


    @Override
    public void memberUpdateSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void memberUpdateSetSuccess() {
        loadingDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("position", position);
        if (seleteList!=null&&seleteList.size()>0){
            for (MemberSeleteModel model: seleteList){
                if (model.isSelect()){
                   content = model.getItems();
                    break;
                }
            }
        }
        intent.putExtra("content", content);

        setResult(101, intent);
        finish();
    }


    public List<MemberSeleteModel> getSeleteList(String params) {
        List<MemberSeleteModel> items = new ArrayList<>();
        switch (params) {
            case "sex"://性别
                ConstantSet.getSexItems(items);
                break;
            case "blood"://血型
                ConstantSet.getBloodItems(items);
                break;
            case "nation"://民族
                ConstantSet.getNationItems(items);
                break;
            case "marriage"://婚姻
                ConstantSet.getMarriageItems(items);
                break;
            case "job"://工作情况
                ConstantSet.getJobItems(items);
                break;
            case "occupation"://职业
                ConstantSet.getOccupationItems(items);
                break;
            case "education"://文化
                ConstantSet.getEducationItems(items);
                break;
            case "liveState"://居住
                ConstantSet.getLiveStateItems(items);
                break;
            case "distance"://子女距离
                ConstantSet.getDistanceItems(items);
                break;
        }

        return items;
    }

    @Override
    public void mySettingUpdateSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void mySettingUpdateSetSuccess() {
        loadingDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("position", position);
        if (seleteList!=null&&seleteList.size()>0){
            for (MemberSeleteModel model: seleteList){
                if (model.isSelect()){
                    content = model.getItems();
                    break;
                }
            }
        }
        intent.putExtra("content", content);

        setResult(101, intent);
        finish();
    }
}
