package com.yinaf.dragon.Content.Activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetMemberAPI;
import com.yinaf.dragon.Content.Net.LoginAPI;
import com.yinaf.dragon.Content.Utils.ActivityCollector;
import com.yinaf.dragon.Content.Utils.Verify.PhoneVerify;
import com.yinaf.dragon.Content.Utils.Verify.PswVerify;
import com.yinaf.dragon.Content.Utils.Verify.VerifyResult;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.App;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.KeyboardUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.ResUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by long on 2018-4-11.
 * 功能：登录页面
 */

public class LoginAct extends BasicAct implements LoginAPI.LoginAPIListener,GetMemberAPI.GetMemberAPIListener{

    //权限
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
    };
    private static final int INITIAL_REQUEST = 1337;
    @BindView(R.id.tool_bar_btn_back)
    RelativeLayout toolBarBtnBack;
    @BindView(R.id.tool_bar_title)
    TextView toolBarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.login_et_phone)
    EditText loginEtPhone;
    @BindView(R.id.login_et_pwd)
    EditText loginEtPwd;
    @BindView(R.id.login_btn_landing)
    Button loginBtnLanding;
    @BindView(R.id.login_tv_register)
    TextView loginTvRegister;
    @BindView(R.id.login_tv_forgot_pwd)
    TextView loginTvForgotPwd;

    public String phone;//手机号
    public String pwd;//密码
    private DatabaseHelper dbHelper;
    LoadingDialog loadingDialog;




    public static void startNewLoginAct() {
        Context context = App.getContext();
        Intent intent = new Intent(context, LoginAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public LoginAct() {
        super(R.layout.act_login, R.string.title_activity_login, false, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginAct.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);



    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getPermission() {
        //动态设置权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            KeyboardUtils.hideKeyboard(this, getCurrentFocus());
        }
        return super.onTouchEvent(event);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.login_tv_register,R.id.login_tv_forgot_pwd,R.id.login_btn_landing})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.login_tv_register:
                RegisterAct.startActivity(this);
                break;
            case R.id.login_tv_forgot_pwd:
                ChangePwdAct.startActivity(this);
                break;
            case R.id.login_btn_landing:

                phone = loginEtPhone.getText().toString();
                VerifyResult phone_result = PhoneVerify.verify(phone);
               if(!phone_result.isResult()){
                    TipUtils.showTip(phone_result.getErrorMsg());
                    return;
                }
                pwd = loginEtPwd.getText().toString();
                VerifyResult pwd_result = PswVerify.verify(pwd);
                if(!pwd_result.isResult()){
                    TipUtils.showTip(pwd_result.getErrorMsg());
                    return;
                }
                SPHelper.save(Builds.SP_USER,"sessionId","");//sessionId
                String sessionId = SPHelper.getString(Builds.SP_USER,"sessionId");
                if(NetworkUtils.isNetworkConnect(this)){

                    loadingDialog.show();
                    new LoginAPI(this,phone,sessionId,pwd);

                }else{
                    TipUtils.showTip(ResUtils.getString(R.string.act_login_tip_unconnect_network));
                }
                break;

            default:break;

        }
    }

    /**
     * 登陆接口
     * @param content
     */
    @Override
    public void loginSuccess(JSONObject content) {

        String sessionId = JSONUtils.getString(content, "sessionId");
        String userName = JSONUtils.getString(content, "userName");
        String realName = JSONUtils.getString(content, "realName");
        String image = JSONUtils.getString(content, "image");
        String userId = JSONUtils.getString(content, "userId");
        SPHelper.save(Builds.SP_USER,"userName",userName);//用户名
        SPHelper.save(Builds.SP_USER,"userId",userId);//用户ID
        SPHelper.save(Builds.SP_USER,"sessionId",sessionId);//sessionId
        SPHelper.save(Builds.SP_USER,"realName",realName);//昵称
        SPHelper.save(Builds.SP_USER,"image",image);//头像图片地址
        SPHelper.save(Builds.SP_USER,"passWord",pwd);//密码
        TipUtils.showTip("登陆成功");
        dbHelper = new DatabaseHelper(this,SPHelper.getString(Builds.SP_USER, "userName"));

        new GetMemberAPI(this);


    }

    @Override
    public void loginError(long code, String msg) {

        loginEtPwd.setText("");
        loadingDialog.dismiss();
        TipUtils.showTip(msg);

    }

    /**
     * 获取用户的所有成员
     * @param content
     */
    @Override
    public void getMemberSuccess(JSONArray content) {
        dbHelper.deleteMemberData();
            try {
                for (int i = 0 ; i < content.length() ; i++){
                    JSONObject jsonObject = content.getJSONObject(i);
                    if (dbHelper.isMemberExists(JSONUtils.getInt(jsonObject,"memberId"))){
                        //本地已存在此成员信息,则更新成员信息
                        Member member = dbHelper.selectMemberByMemberId(JSONUtils.getInt(jsonObject,"memberId"));
                        if (jsonObject.getString("watchId").equals("")){
                            member.setWatchId(0);
                        }else {
                            member.setWatchId(JSONUtils.getInt(jsonObject,"watchId"));
                        }
                        member.setIsLead(JSONUtils.getInt(jsonObject,"isLead"));
                        member.setImage(JSONUtils.getString(jsonObject,"image"));
                        member.setRealName(JSONUtils.getString(jsonObject,"realName"));
                        member.setMemberNum(JSONUtils.getString(jsonObject,"memberNum"));
                        member.setRela(JSONUtils.getString(jsonObject,"rela"));
                        dbHelper.updateMember(member);

                    }else {
                        //本地不存在此成员信息，则插入一条成员信息
                        Member member = new Member();
                        member.setMemberId(JSONUtils.getInt(jsonObject,"memberId"));
                        member.setWatchId(JSONUtils.getInt(jsonObject,"watchId"));
                        member.setIsLead(JSONUtils.getInt(jsonObject,"isLead"));
                        member.setImage(JSONUtils.getString(jsonObject,"image"));
                        member.setRealName(JSONUtils.getString(jsonObject,"realName"));
                        member.setMemberNum(JSONUtils.getString(jsonObject,"memberNum"));
                        member.setRela(JSONUtils.getString(jsonObject,"rela"));
                        dbHelper.insertNewMember(member);
                    }
                }
                loadingDialog.dismiss();
                HomeAct.startActivity(this);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void getMemberError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
