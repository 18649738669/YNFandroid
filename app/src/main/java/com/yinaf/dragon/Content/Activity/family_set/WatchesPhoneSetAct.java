package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yinaf.dragon.Content.Activity.FriendsCircleAct;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.WatchesSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.WatchSetAPI;
import com.yinaf.dragon.Content.Net.WatchesSetAPI;
import com.yinaf.dragon.Content.Utils.Verify.PhoneVerify;
import com.yinaf.dragon.Content.Utils.Verify.VerifyResult;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/30.
 * 功能：腕表手机号设置
 */

public class WatchesPhoneSetAct extends BasicAct implements WatchSetAPI.WatchSetListener{


    @BindView(R.id.edt_test)
    EditText edtTest;

    LoadingDialog loadingDialog;
    String phone;

    public WatchesPhoneSetAct() {
        super(R.layout.member_text_write_act, R.string.login_et_phone, R.string.txt_save
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);
    }


    public static void startActivity(Context context,String phone) {
        Intent intent = new Intent(context, WatchesPhoneSetAct.class);
        intent.putExtra("phone",phone);
        context.startActivity(intent);
    }



    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        phone = getIntent().getStringExtra("phone");
        if (phone.equals("")){
            edtTest.setHint(R.string.change_pwd_tv_title_1);
        }else {
            edtTest.setText(phone);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tool_bar_friends_circle_btn_back, R.id.tool_bar_friends_circle_right_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_btn_back:
                finish();
                break;
            case R.id.tool_bar_friends_circle_right_text:
                String phone1 = edtTest.getText().toString();
                VerifyResult phone_result = PhoneVerify.verify(phone1);
                if(!phone_result.isResult()){
                    TipUtils.showTip(phone_result.getErrorMsg());
                    return;
                }
                loadingDialog.show();
                new WatchSetAPI(this,SPHelper.getInt(Builds.SP_USER,"memberId"),
                        SPHelper.getInt(Builds.SP_USER,"watchId"),"phone",phone1);
                break;
        }
    }

    /**
     * 腕表设置接口
     */
    @Override
    public void watchSetSuccess() {

        EventBus.getDefault().post(new DataEventBus(1, 0, new DrugSetModel.ObjBean()));
        loadingDialog.dismiss();
        finish();

    }

    @Override
    public void watchSetError(long code, String msg) {
        loadingDialog.dismiss();
    }

}
