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

import com.yinaf.dragon.Content.Activity.ChangePwdAct;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.ChangePwdAPI;
import com.yinaf.dragon.Content.Net.SendCodeMessageAPI;
import com.yinaf.dragon.Content.Utils.Verify.CodeVerify;
import com.yinaf.dragon.Content.Utils.Verify.PswVerify;
import com.yinaf.dragon.Content.Utils.Verify.VerifyResult;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicFrg;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.ResUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

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

public class ChangePwd2Frg extends BasicFrg implements SendCodeMessageAPI.SendCodeMessageListener,ChangePwdAPI.ChangePwdAPIListener{


    Unbinder unbinder;
    @BindView(R.id.change_pwd_fragment_et_code)
    EditText changePwdFragmentEtCode;
    @BindView(R.id.change_pwd_tv_time)
    TextView changePwdTvTime;
    @BindView(R.id.change_pwd_fragment_et_pwd)
    EditText changePwdFragmentEtPwd;
    @BindView(R.id.change_pwd_btn)
    Button changePwdBtn;

    Timer timer;
    TimerTask task;
    private int count = 59;
    LoadingDialog loadingDialog;

    private Handler handler = new Handler(){

        @SuppressLint("ResourceAsColor")
        @Override
        public void handleMessage(Message msg) {
            if(count == 0){
                changePwdTvTime.setText("发送验证码");
                changePwdTvTime.setBackground(null);
                changePwdTvTime.setTextColor(R.color.common_red);
                changePwdTvTime.setTextSize(15);
                changePwdTvTime.setEnabled(true);//设置倒计时可点击
                task.cancel();
                timer.cancel();
            }else {
                changePwdTvTime.setText(count+"");

            }
        }
    };


    public ChangePwd2Frg() {
        super(R.layout.frg_change_pwd_2);
    }

    @Override
    public void initView(View view) {
        loadingDialog = LoadingDialog.showDialog(getContext());
        changePwdTvTime.setEnabled(false);//设置倒计时不可点击
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
    @OnClick({R.id.change_pwd_tv_time, R.id.change_pwd_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_pwd_tv_time:
                changePwdTvTime.setText("59");
                changePwdTvTime.setBackgroundResource(R.drawable.bg_style_4);
                changePwdTvTime.setTextColor(R.color.common_black);
                changePwdTvTime.setTextSize(30);
                changePwdTvTime.setEnabled(false);//设置倒计时不可点击
                count = 59;
                loadingDialog.show();
                new SendCodeMessageAPI(this, ChangePwdAct.phone);
                NewTimer();
                break;
            case R.id.change_pwd_btn:

                String code = changePwdFragmentEtCode.getText().toString();
                VerifyResult code_result = CodeVerify.verify(code);
                if (!code_result.isResult()){
                    TipUtils.showTip(code_result.getErrorMsg());
                    changePwdFragmentEtCode.setText("");
                    changePwdFragmentEtPwd.setText("");
                }
                String pwd = changePwdFragmentEtPwd.getText().toString();
                VerifyResult pwd_result = PswVerify.verify(pwd);
                if(!pwd_result.isResult()){
                    TipUtils.showTip(pwd_result.getErrorMsg());
                    changePwdFragmentEtCode.setText("");
                    changePwdFragmentEtPwd.setText("");
                    return;
                }

                if(NetworkUtils.isNetworkConnect(getContext())){

                    loadingDialog.show();
                    new ChangePwdAPI(this,ChangePwdAct.phone,code,pwd);
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

        TipUtils.showTip("验证码发送成功");
        loadingDialog.dismiss();
    }

    @Override
    public void codeMessageError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 修改密码接口
     * @param content
     */
    @Override
    public void changeSuccess(JSONObject content) {
        loadingDialog.dismiss();
        TipUtils.showTip("密码修改成功");
        getActivity().finish();
    }

    @Override
    public void changeError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
        changePwdFragmentEtCode.setText("");
        changePwdFragmentEtPwd.setText("");
    }
}
