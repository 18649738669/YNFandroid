package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.yinaf.dragon.Content.CustomUi.CustomRadioGroup;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.MemberUpdateSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关系选择页
 */
public class RelationSelectAct extends BasicAct implements MemberUpdateSetAPI.MemberUpdateSetListener {


    @BindView(R.id.add_family_rg)
    CustomRadioGroup addFamilyRg;

    @BindView(R.id.btn_add)
    Button btn_add;
    private int type;
    /**
     * 信息设置参数 key
     */
    private String params;
    /**
     *信息设置 位置
     */
    private int position;
    private int memberId;
    private LoadingDialog loadingDialog;


    public RelationSelectAct() {
        super(R.layout.relation_select_act, R.string.family_relation_select, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        String currentValue = getIntent().getStringExtra("content");
        if (type==1){//成员基本信息设置页 跳转过来的
            params = intent.getStringExtra("params");
            position = intent.getIntExtra("position", 0);
            memberId = intent.getIntExtra("memberId", 0);
            btn_add.setText("保存");
        }

        addFamilyRg.setColumn(3);//设置列数
        addFamilyRg.setValues(ConstantSet.getRelationList());//设置记录列表
        addFamilyRg.setView(this);//设置视图
        if (!TextUtils.isEmpty(currentValue)) {
            int index = 0;
            //获取当前关系的位置
            for (int i = 0; i < ConstantSet.getRelationList().size(); i++) {
                if (ConstantSet.getRelationList().get(i).equals(currentValue)) {
                    index = i;
                    break;
                }
            }
            addFamilyRg.setInitChecked(index);
        }
    }

    @OnClick({R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                String currentValue = addFamilyRg.getCurrentValue();//获取当前被选择的按钮值
                if (!TextUtils.isEmpty(currentValue)&&type==0) {
                    //把选中的值传回
                    Intent intent = new Intent();
                    intent.putExtra("currentValue", currentValue);
                    setResult(001, intent);
                    finish();
                    return;
                }
                loadingDialog.show();
                new MemberUpdateSetAPI(this,memberId,params,currentValue);

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
        intent.putExtra("content", addFamilyRg.getCurrentValue());

        setResult(101, intent);
        finish();
    }
}
