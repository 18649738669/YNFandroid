package com.yinaf.dragon.Content.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.AddFamilyAct;
import com.yinaf.dragon.Content.Activity.CaptureAct;
import com.yinaf.dragon.Content.Activity.FriendsCircleAct;
import com.yinaf.dragon.Content.Activity.family_set.WatchesPhoneSetAct;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/04/26.
 * 功能：弹出框，提示用户是否选择前往绑定腕表或者添加成员
 */
public class ChooseBindingDialog extends Activity {


    @BindView(R.id.dialog_exit_confirm_tv_title)
    TextView dialogExitConfirmTvTitle;
    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    int isMember;

    /**
     * @param context
     * @param isMember 是否存在成员 （0 不存在成员  1 存在成员但是没有绑定腕表 2 是否存在电话）
     */
    public static void startActivity(Context context, int isMember) {
        Intent intent = new Intent(context, ChooseBindingDialog.class);
        intent.putExtra("isMember", isMember);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.dialog_choose_binding);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        isMember = getIntent().getIntExtra("isMember", 0);
        if (isMember == 0) {
            dialogExitConfirmTvTitle.setText(R.string.dialog_exit_confirm_tv_title);
        }else if (isMember == 1){
            dialogExitConfirmTvTitle.setText(R.string.dialog_exit_confirm_tv_title1);
        }else if (isMember == 2){
            dialogExitConfirmTvTitle.setText(R.string.dialog_exit_confirm_tv_title2);
        }
    }


    @OnClick(R.id.dialog_choose_binding_btn_confirm)
    public void confirm() {
        if (isMember == 0){
            FriendsCircleAct.startActivity(this);
        }else if (isMember == 1){
            Intent intent = new Intent(this, CaptureAct.class);
            startActivityForResult(intent, REQUEST_CODE);
        }else if (isMember == 2){
            WatchesPhoneSetAct.startActivity(this,"");
        }

        finish();
        ActivityCollector.removeActivity(this);
    }

    @OnClick(R.id.dialog_choose_binding_btn_cancel)
    public void cancel() {
        finish();
        ActivityCollector.removeActivity(this);
    }

}
