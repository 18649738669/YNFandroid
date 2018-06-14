package com.yinaf.dragon.Content.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.AddFamilyAct;
import com.yinaf.dragon.Content.Activity.RegisterAct;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetMemberAPI;
import com.yinaf.dragon.Content.Net.LoginAPI;
import com.yinaf.dragon.Content.Net.RegisterAPI;
import com.yinaf.dragon.Content.Net.SendCodeMessageAPI;
import com.yinaf.dragon.Content.Utils.Verify.CodeVerify;
import com.yinaf.dragon.Content.Utils.Verify.PswVerify;
import com.yinaf.dragon.Content.Utils.Verify.VerifyResult;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicFrg;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.ResUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;



/**
 * Created by long on 2018-4-12.
 * 功能：注册页面 --> 输入验证码以及密码页面
 */

public class Register2Frg extends BasicFrg implements SendCodeMessageAPI.SendCodeMessageListener,
        RegisterAPI.RegisterAPIListener,LoginAPI.LoginAPIListener,GetMemberAPI.GetMemberAPIListener{


    Unbinder unbinder;
    @BindView(R.id.register_fragment_et_code)
    EditText registerFragmentEtCode;
    @BindView(R.id.register_tv_time)
    TextView registerTvTime;
    @BindView(R.id.register_fragment_et_pwd)
    EditText registerFragmentEtPwd;
    @BindView(R.id.register_btn)
    Button registerBtn;
    String code;//验证码
    String pwd;//密码
    Timer timer;
    TimerTask task;
    int count = 59;
    DatabaseHelper dbHelper;
    LoadingDialog loadingDialog;



    private Handler handler = new Handler(){

        @SuppressLint("ResourceAsColor")
        @Override
        public void handleMessage(Message msg) {
            if(count == 0){
                registerTvTime.setEnabled(true);//设置倒计时可点击
                registerTvTime.setText("发送验证码");
                registerTvTime.setBackground(null);
                registerTvTime.setTextColor(R.color.common_red);
                registerTvTime.setTextSize(15);
                task.cancel();
                timer.cancel();
            }else {
                registerTvTime.setText(count+"");

            }
        }
    };


    public Register2Frg() {
        super(R.layout.frg_register_2);
    }

    @Override
    public void initView(View view) {

        loadingDialog = LoadingDialog.showDialog(getContext());
        registerTvTime.setEnabled(false);//设置倒计时不可点击
        NewTimer();

    }

    /**
     * 新建计时器
     */
    private void NewTimer(){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                count--;
                handler.sendEmptyMessage(count);

            }
        };
        timer.schedule(task,0,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放资源
        if(task != null){
            task.cancel();
            task = null;
        }
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.register_tv_time, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_tv_time:
                registerTvTime.setText("59");
                registerTvTime.setBackgroundResource(R.drawable.bg_style_4);
                registerTvTime.setTextColor(R.color.common_black);
                registerTvTime.setTextSize(30);
                registerTvTime.setEnabled(false);//设置倒计时不可点击
                count = 59;
                loadingDialog.show();
                new SendCodeMessageAPI(this, RegisterAct.phone);
                NewTimer();
                break;
            case R.id.register_btn:
                code = registerFragmentEtCode.getText().toString();
                VerifyResult code_result = CodeVerify.verify(code);
                if (!code_result.isResult()){
                    TipUtils.showTip(code_result.getErrorMsg());
                    registerFragmentEtCode.setText("");
                    registerFragmentEtPwd.setText("");
                }
                pwd = registerFragmentEtPwd.getText().toString();
                VerifyResult pwd_result = PswVerify.verify(pwd);
                if(!pwd_result.isResult()){
                    TipUtils.showTip(pwd_result.getErrorMsg());
                    registerFragmentEtCode.setText("");
                    registerFragmentEtPwd.setText("");
                    return;
                }

                if(NetworkUtils.isNetworkConnect(getContext())){
                    loadingDialog.show();
                    new RegisterAPI(this, RegisterAct.phone,code,pwd);
                }else{
                    TipUtils.showTip(ResUtils.getString(R.string.act_login_tip_unconnect_network));
                }
                break;
        }
    }

    /**
     * 发送验证码接口
     * @param content
     */
    @Override
    public void codeMessageSuccess(JSONObject content) {
        loadingDialog.dismiss();
    }

    @Override
    public void codeMessageError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 注册接口
     * @param content
     */
    @Override
    public void registerSuccess(JSONObject content) {
        loadingDialog.dismiss();
        loadingDialog.show();
        SPHelper.save(Builds.SP_USER,"sessionId","");//sessionId
        new LoginAPI(this, RegisterAct.phone,"",pwd);
    }

    @Override
    public void registerError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
        registerFragmentEtCode.setText("");
        registerFragmentEtPwd.setText("");
    }

    /**
     * 登陆接口
     * @param content
     */
    @Override
    public void loginSuccess(JSONObject content) {
        loadingDialog.dismiss();
        String sessionId = JSONUtils.getString(content, "sessionId");
        String userName = JSONUtils.getString(content, "userName");
        String realName = JSONUtils.getString(content, "realName");
        String image = JSONUtils.getString(content, "image");
        SPHelper.save(Builds.SP_USER,"userName",userName);//用户名
        SPHelper.save(Builds.SP_USER,"sessionId",sessionId);//sessionId
        SPHelper.save(Builds.SP_USER,"realName",realName);//昵称
        SPHelper.save(Builds.SP_USER,"image",image);//头像图片地址
        SPHelper.save(Builds.SP_USER,"passWord",pwd);//密码
        TipUtils.showTip("注册成功");
        dbHelper = new DatabaseHelper(getContext(),SPHelper.getString(Builds.SP_USER, "userName"));
        loadingDialog.show();
        new GetMemberAPI(this);
    }

    @Override
    public void loginError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取用户的所有成员
     * @param content
     */
    @Override
    public void getMemberSuccess(JSONArray content) {

        try {
            for (int i = 0 ; i < content.length() ; i++){
                JSONObject jsonObject = content.getJSONObject(i);
                if (dbHelper.isMemberExists(JSONUtils.getInt(jsonObject,"memberId"))){
                    //本地已存在此成员信息,则更新成员信息
                    Member member = dbHelper.selectMemberByMemberId(JSONUtils.getInt(jsonObject,"memberId"));
                    if (jsonObject.getString("watchId").equals("")){
                        member.setWatchId(-1);
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
                    if (jsonObject.getString("watchId").equals("")){
                        member.setWatchId(-1);
                    }else {
                        member.setWatchId(JSONUtils.getInt(jsonObject,"watchId"));
                    }
                    member.setIsLead(JSONUtils.getInt(jsonObject,"isLead"));
                    member.setImage(JSONUtils.getString(jsonObject,"image"));
                    member.setRealName(JSONUtils.getString(jsonObject,"realName"));
                    member.setMemberNum(JSONUtils.getString(jsonObject,"memberNum"));
                    member.setRela(JSONUtils.getString(jsonObject,"rela"));
                    dbHelper.insertNewMember(member);
                }
            }
            loadingDialog.dismiss();
            AddFamilyAct.startActivity(getContext(),1);
            getActivity().finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getMemberError(long code, String msg) {
        loadingDialog.dismiss();
    }
}
