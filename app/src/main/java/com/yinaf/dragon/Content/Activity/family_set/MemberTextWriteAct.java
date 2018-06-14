package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.MemberUpdateSetAPI;
import com.yinaf.dragon.Content.Net.MySettingUpdateSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 成员资料的文本提醒
 */
public class MemberTextWriteAct extends BasicAct implements MemberUpdateSetAPI.MemberUpdateSetListener, MySettingUpdateSetAPI.MySettingUpdateSetListener {

    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;
    @BindView(R.id.tv_unit)
        TextView tv_unit;

    @BindView(R.id.edt_test)
    EditText edtTest;
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

    public MemberTextWriteAct() {
        super(R.layout.member_text_write_act, R.string.family_member_msg_set, R.string.txt_save
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        //获取信息
        Intent intent = getIntent();
        toolBarFriendsCircleTitle.setText(intent.getStringExtra("title"));
        edtTest.setText(intent.getStringExtra("content"));
        params = intent.getStringExtra("params");
        position = intent.getIntExtra("position", 0);
        memberId = intent.getIntExtra("memberId", 0);
        //   8 身高  9://体重 16://收入
        if (position==8||position==9||position==16){
            tv_unit.setVisibility(View.VISIBLE);
            if (position==9){
                tv_unit.setText("KG");
            }else if (position==16){
                tv_unit.setText("元");
            }else {
                tv_unit.setText("CM");
            }
        }else {
            tv_unit.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tool_bar_friends_circle_right_text)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_right_text:
                String s = edtTest.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    TipUtils.showTip("填写信息不能为空");
                    return;
                }
                loadingDialog.show();
                if (memberId==-1){
                    new MySettingUpdateSetAPI(this, params, s);
                }else {
                    new MemberUpdateSetAPI(this, memberId, params, s);
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
        intent.putExtra("content", edtTest.getText().toString());

        setResult(101, intent);
        finish();
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
        intent.putExtra("content", edtTest.getText().toString());

        setResult(101, intent);
        finish();
    }
}
