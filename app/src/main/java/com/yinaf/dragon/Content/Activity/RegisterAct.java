package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.yinaf.dragon.Content.Fragment.Register1Frg;
import com.yinaf.dragon.Content.Fragment.Register2Frg;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018-4-12.
 * 功能：注册页面
 */

public class RegisterAct extends BasicAct {


    @BindView(R.id.register_fragment)
    FrameLayout registerFragment;

    Register1Frg register1Frg;//校验手机号以及滑动验证码页面
    static Register2Frg register2Frg;//输入验证码以及密码页面

    static FragmentManager fragmentManager;
    public static FragmentTransaction transaction;

    public static String phone;//手机号

    public RegisterAct() {
        super(R.layout.act_register, R.string.title_activity_register, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterAct.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        //获取Fragment的管理器
        register1Frg = new Register1Frg();
        register2Frg = new Register2Frg();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.register_fragment,register1Frg);
        transaction.commitAllowingStateLoss();

    }

    public static void SwitchFrg(){
        //跳转到fragment，第一个参数为所要替换的位置id，第二个参数是替换后的fragment
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.register_fragment,register2Frg);
        //提交事物
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
