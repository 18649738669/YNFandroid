package com.yinaf.dragon.Content.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.AddressBookSetAct;
import com.yinaf.dragon.Content.Activity.family_set.AddressSetAct;
import com.yinaf.dragon.Content.Activity.family_set.AppSetAct;
import com.yinaf.dragon.Content.Activity.family_set.ContactsSetAct;
import com.yinaf.dragon.Content.Activity.family_set.FootstepSetAct;
import com.yinaf.dragon.Content.Activity.family_set.FrequencySetAct;
import com.yinaf.dragon.Content.Activity.family_set.MemberSetAct;
import com.yinaf.dragon.Content.Activity.family_set.PoliceSetAct;
import com.yinaf.dragon.Content.Activity.family_set.DrugSetAct;
import com.yinaf.dragon.Content.Activity.family_set.WatchesSetAct;
import com.yinaf.dragon.Content.Dialog.ChooseBindingDialog;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 家人 - 设置
 */
public class FamilySettingAct extends BasicAct {



    public FamilySettingAct() {
        super(R.layout.family_set_act, R.string.family_tv_set_up_the, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.tool_bar_friends_circle_btn_back, R.id.tv_member, R.id.tv_watches, R.id.tv_drug, R.id.tv_address_book
            , R.id.tv_contacts, R.id.tv_app, R.id.tv_address, R.id.tv_footstep, R.id.tv_police
            , R.id.tv_frequency})
    public void onClick(View view) {
        Intent intent = null;
        int watchId = SPHelper.getInt(Builds.SP_USER,"watchId");
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_btn_back:
                finish();
                break;
            case R.id.tv_member:
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    //成员设置
                    intent = new Intent(getApplicationContext(), MemberSetAct.class);
                    intent.putExtra("watchId", watchId);
                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);
                }
                break;
            case R.id.tv_watches:
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    //手表设置
                    intent = new Intent(getApplicationContext(), WatchesSetAct.class);
                    intent.putExtra("watchId", watchId);

                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);
                }
                break;
            case R.id.tv_drug:
                //药物提醒

                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    intent = new Intent(getApplicationContext(), DrugSetAct.class);
                    intent.putExtra("watchId", watchId);

                    intent.putExtra("watchId", getIntent().getIntExtra("watchId", 0));
                    startActivity(intent);

                }
                break;
            case R.id.tv_address_book:
                //通讯录
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    intent = new Intent(getApplicationContext(), AddressBookSetAct.class);
                    intent.putExtra("watchId", watchId);

                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);
                }
                break;
            case R.id.tv_contacts:
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    //紧急联系人
                    intent = new Intent(getApplicationContext(), ContactsSetAct.class);
                    intent.putExtra("watchId", watchId);

                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);

                }
                break;
            case R.id.tv_app:
                //关联 APP
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    intent = new Intent(getApplicationContext(), AppSetAct.class);
                    intent.putExtra("watchId", watchId);

                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);
                }
                break;
            case R.id.tv_address:
                //地址管理
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    intent = new Intent(getApplicationContext(), AddressSetAct.class);
                    intent.putExtra("watchId", watchId);

                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);
                }
                break;
            case R.id.tv_footstep:
                //步长设置
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    intent = new Intent(getApplicationContext(), FootstepSetAct.class);
                    intent.putExtra("watchId", watchId);
                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);
                }
                break;
            case R.id.tv_police:
                //报警范围
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    intent = new Intent(getApplicationContext(), PoliceSetAct.class);
                    intent.putExtra("watchId", watchId);
                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);
                }
                break;
            case R.id.tv_frequency:
                //发送频率
                if (watchId == 0 || watchId == -1){
                    ChooseBindingDialog.startActivity(this,1);

                }else {
                    intent = new Intent(getApplicationContext(), FrequencySetAct.class);
                    intent.putExtra("watchId", watchId);
                    intent.putExtra("memberId", getIntent().getIntExtra("memberId", 0));
                    startActivity(intent);
                }
                break;
        }

    }


}
