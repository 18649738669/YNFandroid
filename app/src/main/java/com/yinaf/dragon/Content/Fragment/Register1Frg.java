package com.yinaf.dragon.Content.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yinaf.dragon.Content.Activity.RegisterAct;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.SendCodeMessageAPI;
import com.yinaf.dragon.Content.Net.VerifyPhoneRegisterAPI;
import com.yinaf.dragon.Content.Utils.Verify.PhoneVerify;
import com.yinaf.dragon.Content.Utils.Verify.VerifyResult;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicFrg;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.ResUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by long on 2018-4-12.
 * 功能：注册页面 --> 校验手机号以及滑动验证码页面
 */

public class Register1Frg extends BasicFrg implements VerifyPhoneRegisterAPI.VerifyPhoneRegisterListener ,
        SendCodeMessageAPI.SendCodeMessageListener{


    @BindView(R.id.register_fragment_et_phone)
    EditText registerFragmentEtPhone;
    @BindView(R.id.register_fm_user_server_agreement)
    TextView registerFmUserServerAgreement;
    @BindView(R.id.register_btn_next)
    Button registerBtnNext;
    @BindView(R.id.register_tv_existing_account)
    TextView registerTvExistingAccount;
    Unbinder unbinder;
    String phone;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    LoadingDialog loadingDialog;

    public Register1Frg() {
        super(R.layout.frg_register_1);
    }

    @Override
    public void initView(View view) {
        loadingDialog = LoadingDialog.showDialog(getContext());

        //获取Fragment的管理器
        fragmentManager = getFragmentManager();
        //开启fragment的事物,在这个对象里进行fragment的增删替换等操作。
        transaction=fragmentManager.beginTransaction();
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

    @SuppressLint("ResourceType")
    @OnClick({R.id.register_btn_next, R.id.register_tv_existing_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_btn_next:
                phone = registerFragmentEtPhone.getText().toString();
                RegisterAct.phone = phone;
                VerifyResult phone_result = PhoneVerify.verify(phone);
                if(!phone_result.isResult()){
                    TipUtils.showTip(phone_result.getErrorMsg());
                    return;
                }
                if(NetworkUtils.isNetworkConnect(getContext())){

                    loadingDialog.show();
                    new VerifyPhoneRegisterAPI(this,phone);
                }else{
                    TipUtils.showTip(ResUtils.getString(R.string.act_login_tip_unconnect_network));
                }
                break;
            case R.id.register_tv_existing_account:
                getActivity().finish();
                break;
            case R.id.register_fm_user_server_agreement:
                Toast.makeText(getContext(),"用户服务协议",Toast.LENGTH_LONG).show();
                break;
            default:break;
        }
    }

    /**
     * 检测手机号是否已经注册为颐纳福会员
     * @param content
     */
    @Override
    public void verifySuccess(JSONObject content) {
        new SendCodeMessageAPI(this,phone);
    }

    @Override
    public void verifyError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 发送注册短信验证码
     * @param content
     */
    @Override
    public void codeMessageSuccess(JSONObject content) {
        loadingDialog.dismiss();
        RegisterAct.SwitchFrg();
    }

    @Override
    public void codeMessageError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
