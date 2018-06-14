package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yinaf.dragon.Content.Activity.family_set.widget.CustomDatePicker;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.MemberUpdateSetAPI;
import com.yinaf.dragon.Content.Net.MySettingUpdateSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options1Items;
import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options2Items;
import static com.yinaf.dragon.Content.Activity.family_set.ConstantSet.options3Items;

/**
 * 成员基本信息 填写选择
 */
public class MemberListAct extends BasicAct implements MemberUpdateSetAPI.MemberUpdateSetListener, MySettingUpdateSetAPI.MySettingUpdateSetListener {

    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;

    @BindView(R.id.tv_select_text)
    TextView edtTest;
    /**
     * 信息设置参数 key
     */
    private String params;
    /**
     *信息设置 位置
     */
    private int position;
    private int memberId;
    private CustomDatePicker customDatePicker;
    private Thread thread;
    private LoadingDialog loadingDialog;

    public MemberListAct() {
        super(R.layout.member_select_act, R.string.family_member_msg_set, R.string.txt_save
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
                    ConstantSet.initJsonData(getApplicationContext());
                }
            });
            thread.start();
        }
        loadingDialog = LoadingDialog.showDialog(this);
        initDatePicker();
        //获取信息
        Intent intent = getIntent();
        toolBarFriendsCircleTitle.setText(intent.getStringExtra("title"));
        edtTest.setText(intent.getStringExtra("content"));
        params = intent.getStringExtra("params");
        position = intent.getIntExtra("position", 0);
        memberId = intent.getIntExtra("memberId", 0);
    }

    @OnClick({R.id.tool_bar_friends_circle_right_text,R.id.tv_select_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_right_text:
                String s = edtTest.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    return;
                }
                loadingDialog.show();
                if (memberId==-1){
                    new MySettingUpdateSetAPI(this, params, s);
                }else {
                    new MemberUpdateSetAPI(this, memberId, params, s);
                }
                break;
            case R.id.tv_select_text:
                if (position == 2) {
                    //日期选择
                    customDatePicker.show(edtTest.getText().toString());
                } else {
                    //地址选择
                    onAddressPicker();
                }
                break;
        }
    }

    /**
     * 地址选择器
     */
    public void onAddressPicker() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {


            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);

                edtTest.setText(tx);
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

    /**
     * 开始提醒和结束提醒的时间选择器初始化
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        edtTest.setText(now.split(" ")[0]);

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间

                edtTest.setText(time.split(" ")[0]);
            }
        }, "1900-01-01 00:00", "2100-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(true); // 不允许循环滚动


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void memberUpdateSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void memberUpdateSetSuccess() {
        loadingDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("content", edtTest.getText().toString());

        setResult(101, intent);
        finish();
    }

    @Override
    public void mySettingUpdateSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void mySettingUpdateSetSuccess() {
        loadingDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("content", edtTest.getText().toString());

        setResult(101, intent);
        finish();
    }
}
