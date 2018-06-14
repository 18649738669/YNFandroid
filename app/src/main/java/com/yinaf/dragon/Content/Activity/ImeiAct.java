package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.BindWatchAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018-4-17.
 * 功能：填写IMEI码页面
 */

public class ImeiAct extends BasicAct implements BindWatchAPI.BindWatchAPIListener{


    @BindView(R.id.imei_et_prompt)
    EditText imeiEtPrompt;
    @BindView(R.id.imei_btn_next)
    Button imeiBtnNext;

    LoadingDialog loadingDialog;

    public ImeiAct() {
        super(R.layout.act_imei, R.string.title_activity_imei, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context , int memberId) {
        Intent intent = new Intent(context, ImeiAct.class);
        intent.putExtra("memberId",memberId);
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

    @OnClick({R.id.tool_bar_btn_back, R.id.imei_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_btn_back:
                CaptureAct.startActivity(this,getIntent().getIntExtra("memberId",0),0);
                finish();
                break;
            case R.id.imei_btn_next:
                if(imeiEtPrompt.getText().equals("")){
                    TipUtils.showTip("请输入IMEI码");
                    return;
                }
                loadingDialog.show();
                new BindWatchAPI(this,getIntent().getIntExtra("memberId",0),imeiEtPrompt.getText().toString());
                break;
        }
    }

    /**
     * 绑定腕表
     * @param content
     */
    @Override
    public void bindWatchSuccess(JSONObject content) {
        TipUtils.showTip("绑定成功");
        loadingDialog.dismiss();
        finish();
    }

    @Override
    public void bindWatchError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
