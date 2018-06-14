package com.yinaf.dragon.Content.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/04/26.
 * 功能：弹出框，提示用户是否退出应用程序
 */
public class ExitConfirmDialog extends Activity {

    public static void startActivity(Context context){
        Intent intent = new Intent(context,ExitConfirmDialog.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.dialog_exit_confirm);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.dialog_exit_btn_confirm)
    public void confirm(){

        ActivityCollector.finishAll();
    }

    @OnClick(R.id.dialog_exit_btn_cancel)
    public void cancel(){
        finish();
        ActivityCollector.removeActivity(this);
    }

}
