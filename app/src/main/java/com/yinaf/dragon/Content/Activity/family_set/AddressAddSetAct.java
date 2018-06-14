package com.yinaf.dragon.Content.Activity.family_set;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddressSetAddAPI;
import com.yinaf.dragon.Content.Net.AddressSetUpdateAPI;
import com.yinaf.dragon.Content.Utils.Verify.PhoneVerify;
import com.yinaf.dragon.Content.Utils.Verify.VerifyResult;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options1Items;
import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options2Items;
import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options3Items;

/**
 * 家庭成员 - 添加地址
 */
public class AddressAddSetAct extends BasicAct implements AddressSetAddAPI.AddressSetAddListener
        , AddressSetUpdateAPI.AddressSetUpdateListener {


    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.btn_add)
    Button btnAdd;
    @BindView(R.id.sw_switch)
    Switch sw_switch;

    private int type;
    private Thread thread;
    private boolean isCity = false;

    private AddressSetModel.ObjBean objBean;
    private String province;
    private int memberId;
    private LoadingDialog loadingDialog;

    public AddressAddSetAct() {
        super(R.layout.address_add_set_act, R.string.family_address_add_set, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        //生成城市列表数据
        if (thread == null) {//如果已创建就不再重新创建子线程了

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 子线程中解析省市区数据
                    isCity = ConstantSet.initJsonData(getApplicationContext());
                }
            });
            thread.start();
        }
        loadingDialog = LoadingDialog.showDialog(this);
        //判断是增加还是修改
        type = getIntent().getIntExtra("type", 0);

        memberId = getIntent().getIntExtra("memberId", 0);
        if (type == 1) {
            objBean = (AddressSetModel.ObjBean) getIntent().getExtras().getSerializable("model");
            edtName.setText(objBean.getReceiver());
            edtPhone.setText(objBean.getPhone());
            tvArea.setText(objBean.getRegion());
            edtAddress.setText(objBean.getAddress());
            toolBarFriendsCircleTitle.setText("修改地址");
            btnAdd.setText("保存");
            if (objBean.getDefcode() == 0) {
                sw_switch.setSelected(false);
            } else {
                sw_switch.setSelected(false);
            }
        }

    }


    @OnClick({R.id.tv_area, R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_area:
                //区域选择
                if (isCity) {
                    onAddressPicker();
                } else {
                    TipUtils.showTip("城市信息获取失败");
                }
                break;
            case R.id.btn_add:
                String defcode = "0";
                if (sw_switch.isChecked()) {
                    defcode = "1";
                }
                String phone = edtPhone.getText().toString();
                VerifyResult phone_result = PhoneVerify.verify(phone);
                if(!phone_result.isResult()){
                    TipUtils.showTip(phone_result.getErrorMsg());
                    return;
                }
                if (type == 0 && TextUtils.isEmpty(edtName.getText().toString())
                        && TextUtils.isEmpty(edtAddress.getText().toString())
                        && TextUtils.isEmpty(edtPhone.getText().toString())
                        && TextUtils.isEmpty(tvArea.getText().toString())) {
                    TipUtils.showTip("填写信息不能为空!");
                    return;
                } else if (type == 1) {
                    loadingDialog.show();
                    new AddressSetUpdateAPI(this, memberId, edtName.getText().toString()
                            , edtAddress.getText().toString(), edtPhone.getText().toString()
                            , province, defcode, "", objBean.getAddressId() + "");

                } else {
                    loadingDialog.show();
                    new AddressSetAddAPI(this, memberId, edtName.getText().toString()
                            , edtAddress.getText().toString(), edtPhone.getText().toString()
                            , province, defcode, "");
                }


                break;
        }
    }

    public void onAddressPicker() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {


            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                province = options1Items.get(options1).getPickerViewText() +
                        "-" + options2Items.get(options1).get(options2) +
                        "-" + options3Items.get(options1).get(options2).get(options3);
                tvArea.setText(tx);
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();

    }


    @Override
    public void addressSetAddSuccess() {
        EventBus.getDefault().post(new DataEventBus(type, 0, new DrugSetModel.ObjBean()));
        loadingDialog.dismiss();
        finish();
    }

    @Override
    public void addressSetAddError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void addressSetUpdateSuccess() {
        loadingDialog.dismiss();
        EventBus.getDefault().post(new DataEventBus(type, 0, new DrugSetModel.ObjBean()));

        finish();
    }

    @Override
    public void addressSetUpdateError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
