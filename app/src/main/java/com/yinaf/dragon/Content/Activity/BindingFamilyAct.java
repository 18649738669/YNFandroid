package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.BindMemberAPI;
import com.yinaf.dragon.Content.Net.ValidateMemberAPI;
import com.yinaf.dragon.Content.Receiver.RefreshMemberReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/04/24.
 * 功能：绑定家人页面
 */

public class BindingFamilyAct extends BasicAct implements ValidateMemberAPI.ValidateMemberAPIListener,
        BindMemberAPI.BindMemberAPIListener{


    @BindView(R.id.binding_family_et_member_id)
    EditText bindingFamilyEtMemberId;
    @BindView(R.id.binding_family_et_code)
    EditText bindingFamilyEtCode;
    @BindView(R.id.binding_family_tv_code)
    TextView bindingFamilyTvCode;
    @BindView(R.id.binding_family_btn_add)
    Button bindingFamilyBtnAdd;
    LoadingDialog loadingDialog;
    String userName;
    int memberId;

    public BindingFamilyAct() {
        super(R.layout.act_binding_family, R.string.title_activity_binding_family, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BindingFamilyAct.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.binding_family_tv_code, R.id.binding_family_btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.binding_family_tv_code:
                String memberNum = bindingFamilyEtMemberId.getText().toString();
                if (memberNum == null  || memberNum.equals("")){
                    TipUtils.showTip("请输入成员编号");
                    return;
                }else {
                    loadingDialog.show();
                    new ValidateMemberAPI(this, memberNum);
                }
                break;
            case R.id.binding_family_btn_add:
                String code = bindingFamilyEtCode.getText().toString();
                if (code == null ||  code.equals("")){
                    TipUtils.showTip("请输入验证码");
                    return;
                }else {
                    loadingDialog.show();
                    new BindMemberAPI(this, userName, memberId, code);
                }
                break;
        }
    }

    /**
     * 验证成员
     * @param content
     */
    @Override
    public void validateMemberSuccess(JSONObject content) {

        memberId = JSONUtils.getInt(content,"memberId");
        userName = JSONUtils.getString(content,"userName");
        loadingDialog.dismiss();
        TipUtils.showTip("验证码发送成功");
    }

    @Override
    public void validateMemberError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 绑定成员接口
     * @param content
     */
    @Override
    public void bindMemberSuccess(JSONObject content) {
        loadingDialog.dismiss();
        TipUtils.showTip("成员添加成功");
        RefreshMemberReceiver.send(this);
        finish();
    }

    @Override
    public void bindMemberError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
