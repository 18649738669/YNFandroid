package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddressBookSetAddAPI;
import com.yinaf.dragon.Content.Net.AddressBookSetUpdateAPI;
import com.yinaf.dragon.Content.Utils.Verify.PhoneVerify;
import com.yinaf.dragon.Content.Utils.Verify.VerifyResult;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options1Items;
import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options2Items;
import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options3Items;

/**
 * 通讯录添加页
 */
public class AddressBookSetAddAct extends BasicAct implements AddressBookSetAddAPI.AddressBookSetAddListener, AddressBookSetUpdateAPI.AddressBookSetUpdateListener {

    @BindView(R.id.tv_area)
    TextView tv_area;


    @BindView(R.id.tv_relation)
    TextView tv_relation;
    @BindView(R.id.tv_address)
    EditText tv_address;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.tool_bar_friends_circle_title)
    TextView tool_bar_friends_circle_title;
    @BindView(R.id.btn_add)
    Button btn_add;
    private Thread thread;

    public String areas;
    public String city;
    public String province;
    /**
     * 城市数据解析是否成功
     */
    private boolean isCity = false;
    private int type = 0;
    private AddressBookSetModel.ObjBean model;
    private LoadingDialog loadingDialog;


    public AddressBookSetAddAct() {
        super(R.layout.address_book_add_set_act, R.string.family_address_book_add_set, 0
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
        if (type == 1) {
            //修改提醒记录
            Bundle extras = getIntent().getExtras();
            model = (AddressBookSetModel.ObjBean) extras.getSerializable("model");
            edt_name.setText(model.getTrueName());
            edt_phone.setText(model.getPhone());
            tv_relation.setText(model.getRela());
            tv_address.setText(model.getAddress());
            tv_area.setText(model.getProvince() + model.getCity() + model.getAreas());

            province = model.getProvince();
            areas = model.getAreas();
            city = model.getCity();

            tool_bar_friends_circle_title.setText("修改联系人");
            btn_add.setText("保存");
        }

    }

    @OnClick({R.id.tv_relation, R.id.tv_area, R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_relation:
                //关系选择
                Intent intent = new Intent(getApplicationContext(), RelationSelectAct.class);
                intent.putExtra("content", tv_relation.getText().toString());
                startActivityForResult(intent
                        , 100);
                break;
            case R.id.tv_area:
                //区域选择
                if (isCity) {
                    onAddressPicker();
                } else {
                    TipUtils.showTip("城市信息获取失败");
                }

                break;
            case R.id.btn_add:
                String phone = edt_phone.getText().toString();
                VerifyResult phone_result = PhoneVerify.verify(phone);
                if(!phone_result.isResult()){
                    TipUtils.showTip(phone_result.getErrorMsg());
                    return;
                }
                if (TextUtils.isEmpty(tv_address.getText().toString()) && TextUtils.isEmpty(edt_phone.getText().toString())
                        && TextUtils.isEmpty(tv_relation.getText().toString()) && TextUtils.isEmpty(edt_name.getText().toString())
                        && TextUtils.isEmpty(province) && TextUtils.isEmpty(city) && TextUtils.isEmpty(areas)) {
                    TipUtils.showTip("填写数据不能为空");
                    return;
                } else {
                    if (type == 1) {
                        loadingDialog.show();
                        new AddressBookSetUpdateAPI(this, model.getMemberId(), model.getBookId()
                                , tv_address.getText().toString(), edt_phone.getText().toString()
                                , tv_relation.getText().toString(), edt_name.getText().toString(), province
                                , city, areas);
                    } else {
                        loadingDialog.show();
                        new AddressBookSetAddAPI(this, getIntent().getIntExtra("memberId", 0)

                                , tv_address.getText().toString(), edt_phone.getText().toString()
                                , tv_relation.getText().toString(), edt_name.getText().toString(), province
                                , city, areas);
                    }

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
                province = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                areas = options3Items.get(options1).get(options2).get(options3);
                tv_area.setText(tx);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 001) {
                //选择后的关系名称
                String currentValue = data.getStringExtra("currentValue");
                tv_relation.setText(currentValue);
            }
        }
    }

    @Override
    public void addressBookSetAddSuccess() {
        EventBus.getDefault().post(new DataEventBus(type, 0, new DrugSetModel.ObjBean()));
        loadingDialog.dismiss();
        finish();
    }

    @Override
    public void addressBookSetAddError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void addressBookSetUpdateSuccess() {
        loadingDialog.dismiss();
        EventBus.getDefault().post(new DataEventBus(type, 0, new DrugSetModel.ObjBean()));

        finish();
    }

    @Override
    public void addressBookSetUpdateError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
