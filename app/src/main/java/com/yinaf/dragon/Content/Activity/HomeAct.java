package com.yinaf.dragon.Content.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Dialog.ExitConfirmDialog;
import com.yinaf.dragon.Content.Dialog.UpdateApkDialog;
import com.yinaf.dragon.Content.Fragment.FamilyFrg;
import com.yinaf.dragon.Content.Fragment.HomeFrg;
import com.yinaf.dragon.Content.Fragment.HomeFrg1;
import com.yinaf.dragon.Content.Fragment.MyFrg;
import com.yinaf.dragon.Content.Fragment.ServiceFrg;
import com.yinaf.dragon.Content.Net.GetUpdateAppAPI;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.yinaf.dragon.Tool.Utils.UIUtils;

import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by long on 2018-4-13.
 * 功能：首页页面
 */

public class HomeAct extends BasicAct implements GetUpdateAppAPI.GetUpdateAppListener {


    @BindView(R.id.home_fragment)
    FrameLayout homeFragment;
    @BindView(R.id.home_tv_home)
    TextView homeTvHome;
    @BindView(R.id.home_tv_family)
    TextView homeTvFamily;
    @BindView(R.id.home_tv_server)
    TextView homeTvServer;
    @BindView(R.id.home_tv_my)
    TextView homeTvMy;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

//    HomeFrg homeFrg;//首页fragment
    HomeFrg1 homeFrg;//首页fragment
    FamilyFrg familyFrg;//家人fragment
    MyFrg myFrg;//我的fragment
    ServiceFrg serviceFrg;//服务fragment

    //权限
    private static final String[] INITIAL_PERMS={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
    };
    private static final int INITIAL_REQUEST=1337;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomeAct.class);
        context.startActivity(intent);
    }

    public HomeAct() {
        super(R.layout.act_home, R.string.title_activity_home, false, TOOLBAR_TYPE_NO_TOOLBAR,R.color.common_blue);
    }

    @TargetApi(android.os.Build.VERSION_CODES.M)
    private void getPermission() {
        //动态设置权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_BOOT_COMPLETED)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED ) {
        } else {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission();
        }
        //获取Fragment的管理器,默认加载首页fragment
        homeFrg = new HomeFrg1();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.home_fragment,homeFrg);
        transaction.commitAllowingStateLoss();
        setHomeTvHome(true);
        setJPushAliasAndTags();
        String currentVersion = UIUtils.getVersion();

        new GetUpdateAppAPI(this,currentVersion);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.home_tv_home, R.id.home_tv_family, R.id.home_tv_server, R.id.home_tv_my})
    public void onViewClicked(View view) {
        transaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.home_tv_home:
                if (homeFrg == null){
                    homeFrg = new HomeFrg1();
                }
                if (!homeFrg.isAdded()){
                    transaction.add(R.id.home_fragment,homeFrg);
                }else {
                    if(familyFrg != null){
                        transaction.hide(familyFrg);
                    }
                    if (myFrg != null){
                        transaction.hide(myFrg);
                    }
                    if(myFrg != null){
                        transaction.hide(myFrg);
                    }
                    if(serviceFrg != null){
                        transaction.hide(serviceFrg);
                    }
                    transaction.show(homeFrg);
                }
                setHomeTvHome(true);
                setHomeTvFamily(false);
                setHomeTvServer(false);
                setHomeTvMy(false);
                break;
            case R.id.home_tv_family:
                if (familyFrg == null){
                    familyFrg = new FamilyFrg();
                }
                if (!familyFrg.isAdded()){
                    transaction.add(R.id.home_fragment,familyFrg);
                }else{
                    if(homeFrg != null){
                        transaction.hide(homeFrg);
                    }
                    if (myFrg != null){
                        transaction.hide(myFrg);
                    }
                    if(myFrg != null){
                        transaction.hide(myFrg);
                    }
                    if(serviceFrg != null){
                        transaction.hide(serviceFrg);
                    }
                    transaction.show(familyFrg);
                }
                setHomeTvHome(false);
                setHomeTvFamily(true);
                setHomeTvServer(false);
                setHomeTvMy(false);
                break;
            case R.id.home_tv_server:
                if (serviceFrg == null){
                    serviceFrg = new ServiceFrg();
                }
                if (!serviceFrg.isAdded()){
                    transaction.add(R.id.home_fragment,serviceFrg);
                }else{
                    if(homeFrg != null){
                        transaction.hide(homeFrg);
                    }
                    if(familyFrg != null){
                        transaction.hide(familyFrg);
                    }
                    if(myFrg != null){
                        transaction.hide(myFrg);
                    }
                    transaction.show(serviceFrg);
                }
                setHomeTvHome(false);
                setHomeTvFamily(false);
                setHomeTvServer(true);
                setHomeTvMy(false);
                break;
            case R.id.home_tv_my:
                if (myFrg == null){
                    myFrg = new MyFrg();
                }
                if (!myFrg.isAdded()){
                    transaction.add(R.id.home_fragment,myFrg);
                }else{
                    if(homeFrg != null){
                        transaction.hide(homeFrg);
                    }
                    if(familyFrg != null){
                        transaction.hide(familyFrg);
                    }
                    if(serviceFrg != null){
                        transaction.hide(serviceFrg);
                    }
                    transaction.show(myFrg);
                }
                setHomeTvHome(false);
                setHomeTvFamily(false);
                setHomeTvServer(false);
                setHomeTvMy(true);
                break;
        }
        transaction.commitAllowingStateLoss();

    }

    /**
     * 设置Home的文字颜色以及图片
     */
    @SuppressLint("ResourceAsColor")
    public void setHomeTvHome(boolean bool){
        Drawable img;
        if (bool){
            homeTvHome.setTextColor(Color.argb(255,0,170,239));
            //设置TextView的上图标
            img = getResources().getDrawable(R.drawable.home2);
        }else {
            homeTvHome.setTextColor(Color.LTGRAY);
            //设置TextView的上图标
            img = getResources().getDrawable(R.drawable.home);
        }
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getIntrinsicWidth(), img.getIntrinsicHeight());
        homeTvHome.setCompoundDrawables(null, img, null, null); //设置上图标

    }

    /**
     * 设置Family的文字颜色以及图片
     */
    @SuppressLint("ResourceAsColor")
    public void setHomeTvFamily(boolean bool){

        Drawable img;
        if (bool){
            homeTvFamily.setTextColor(Color.argb(255,0,170,239));
            //设置TextView的上图标
            img = getResources().getDrawable(R.drawable.watches2);
        }else {
            homeTvFamily.setTextColor(Color.LTGRAY);
            //设置TextView的上图标
            img = getResources().getDrawable(R.drawable.watches);

        }
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getIntrinsicWidth(), img.getIntrinsicHeight());
        homeTvFamily.setCompoundDrawables(null, img, null, null); //设置上图标

    }

    /**
     * 设置Server的文字颜色以及图片
     */
    @SuppressLint("ResourceAsColor")
    public void setHomeTvServer(boolean bool){

        Drawable img;
        if (bool){
            homeTvServer.setTextColor(Color.argb(255,0,170,239));
            //设置TextView的上图标
            img = getResources().getDrawable(R.drawable.serve2);
        }else {
            homeTvServer.setTextColor(Color.LTGRAY);
            //设置TextView的上图标
            img = getResources().getDrawable(R.drawable.serve);

        }
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getIntrinsicWidth(), img.getIntrinsicHeight());
        homeTvServer.setCompoundDrawables(null, img, null, null); //设置上图标

    }

    /**
     * 设置My的文字颜色以及图片
     */
    @SuppressLint("ResourceAsColor")
    public void setHomeTvMy(boolean bool){

        Drawable img;
        if (bool){
            homeTvMy.setTextColor(Color.argb(255,0,170,239));
            //设置TextView的上图标
            img = getResources().getDrawable(R.drawable.my2);
        }else {
            homeTvMy.setTextColor(Color.LTGRAY);
            //设置TextView的上图标
            img = getResources().getDrawable(R.drawable.my);

        }
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img.setBounds(0, 0, img.getIntrinsicWidth(), img.getIntrinsicHeight());
        homeTvMy.setCompoundDrawables(null, img, null, null); //设置上图标
    }

    //记录用户首次点击返回键的时间
    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                TipUtils.showTip("再按一次退出程序");
                firstTime=System.currentTimeMillis();
            }else{
//                finish();
                ActivityCollector.finishAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置极光推送的标签和别名
     */
    public void setJPushAliasAndTags(){
        String sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
        if(!TextUtils.isEmpty(sessionId)){
            Set<String> tagSet = new LinkedHashSet<String>();
            tagSet.add("sessionId");
           JPushInterface.setAliasAndTags(this, sessionId, tagSet, new TagAliasCallback() {
                @Override
                public void gotResult(int code, String alias, Set<String> tags) {

                }
            });
        }
    }

    /**
     * 清除极光推送的标签和别名
     */
    public void clearJPushAliasAndTags(){
        Set<String> tagSet = new LinkedHashSet<String>();
        JPushInterface.setAliasAndTags(this, "", tagSet, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String alias, Set<String> tags) {

            }
        });
    }

    /**
     * app更新接口
     * @param content
     */
    @Override
    public void getUpdateAppSuccess(JSONObject content) {
        boolean success = JSONUtils.getBoolean(content,"success");
        JSONObject jsonObject = JSONUtils.getJSONObject(content,"obj");
        String update_des = JSONUtils.getString(jsonObject,"upgradeInfo").replace("/n","\n");
        String app_path = JSONUtils.getString(jsonObject,"updateUrl");
        String version = JSONUtils.getString(jsonObject,"serverVersion");
        int lastForce = JSONUtils.getInt(jsonObject,"lastForce");
        if (success) {
            UpdateApkDialog.startActivity(HomeAct.this,update_des,app_path,version,lastForce);
        }
    }

    /**
     * 没有新版本
     * @param code
     * @param msg
     */
    @Override
    public void getUpdateAppError(long code, String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearJPushAliasAndTags();
    }
}
