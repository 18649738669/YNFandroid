package com.yinaf.dragon.Content.Activity.family_set;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Activity.family_set.widget.CustomDatePicker;
import com.yinaf.dragon.Content.Activity.family_set.widget.DatePickerView;
import com.yinaf.dragon.Content.Activity.family_set.widget.DatePickerView1;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.DrugSetAddAPI;
import com.yinaf.dragon.Content.Net.DrugSetUpdateAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 增加服药提醒
 */
public class DrugSetAddAct extends BasicAct implements DrugSetAddAPI.DrugSetAddListener, DrugSetUpdateAPI.DrugSetUpdateListener {

    @BindView(R.id.edt_title)
    EditText edtTitle;

    @BindView(R.id.edt_remarks)
    EditText edt_remarks;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;

    @BindView(R.id.btn_add)
    Button btn_add;


    @BindView(R.id.hour_pv)
    DatePickerView hour;

    @BindView(R.id.minute_pv)
    DatePickerView1 minute_pv;
    @BindView(R.id.tool_bar_friends_circle_title)
    TextView tool_bar_friends_circle_title;

    private CustomDatePicker customDatePicker;

    private int type_time = 1;//选择的时间 1 开始,2 结束

    private String hours_time = "12";
    private String minute_time = "00";
    private int type;
    private DrugSetModel.ObjBean model;
    private int pos;
    private LoadingDialog loadingDialog;


    public DrugSetAddAct() {
        super(R.layout.drug_set_add_act, R.string.family_drug_set_add, 0
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        initDatePicker();
        loadingDialog = LoadingDialog.showDialog(this);
        type = getIntent().getIntExtra("type", 0);
        //设置时间选择器
        hour.setData(ConstantSet.getDrugTime().get(0));
        minute_pv.setData(ConstantSet.getDrugTime().get(1));
        if (type == 1) {
            pos = getIntent().getIntExtra("pos", 0);
            tool_bar_friends_circle_title.setText("修改提醒");
            Bundle extras = getIntent().getExtras();
            model = (DrugSetModel.ObjBean) extras.getSerializable("model");
            String[] split = model.getRemindTime().split(":");
            hours_time = split[0];
            minute_time = split[1];
            hour.setSelected(hours_time);

            minute_pv.setSelected(minute_time);
            edtTitle.setText(model.getMedicineTitle());
            tvStartTime.setText(model.getStartTime());
            tvEndTime.setText(model.getEndTime());
            edt_remarks.setText(model.getMedicineRemarks());
            btn_add.setText("保存");
        } else {
            //选择的时间
            hour.setSelected(hours_time);

            minute_pv.setSelected(minute_time);
        }


        hour.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (!TextUtils.isEmpty(text)) {
                    hours_time = text;
                }
            }
        });
        minute_pv.setOnSelectListener(new DatePickerView1.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (!TextUtils.isEmpty(text)) {
                    minute_time = text;
                }
            }
        });
    }

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_end_time:
                //选择结束日期
                type_time = 2;
                customDatePicker.show(tvEndTime.getText().toString());
                break;
            case R.id.tv_start_time:
                type_time = 1;
                //选择开始日期
                customDatePicker.show(tvStartTime.getText().toString());
                break;
            case R.id.btn_add:

                if (edtTitle.getText().toString().length() <= 0) {
                    TipUtils.showTip("标题不能为空");
                    return;
                }
                if (tvStartTime.getText().toString().length() <= 0) {
                    TipUtils.showTip("开始日期不能为空");
                    return;
                }
                if (tvEndTime.getText().toString().length() <= 0) {
                    TipUtils.showTip("结束日期不能为空");
                    return;
                }
                if (hours_time.length() <= 0 && minute_time.length() <= 0) {
                    TipUtils.showTip("提醒时间不能为空");
                    return;
                }
                if (type == 0) {
                    loadingDialog.show();
                    new DrugSetAddAPI(this, getIntent().getIntExtra("memberId", 0),
                            getIntent().getIntExtra("watchId", 0), edtTitle.getText().toString(), edt_remarks.getText().toString()
                            , tvStartTime.getText().toString(), tvEndTime.getText().toString()
                            , hours_time + ":" + minute_time);
                }else {

                    loadingDialog.show();
                    new DrugSetUpdateAPI(this, model.getMemberId(),model.getMedicineId(),model.getWatchId()
                            , edtTitle.getText().toString(), edt_remarks.getText().toString()
                            , tvStartTime.getText().toString(), tvEndTime.getText().toString()
                            , hours_time + ":" + minute_time);
                }
                break;
        }
    }

    /**
     * 开始提醒和结束提醒的时间选择器初始化
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        tvStartTime.setText(now.split(" ")[0]);
        tvEndTime.setText(now.split(" ")[0]);

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (type_time == 1) {
                    tvStartTime.setText(time.split(" ")[0]);
                } else {
                    tvEndTime.setText(time.split(" ")[0]);
                }
            }
        }, "2010-01-01 00:00", "2100-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(true); // 不允许循环滚动


    }


    @Override
    public void drugSetAddSuccess() {
        loadingDialog.dismiss();
        EventBus.getDefault().post(new DataEventBus(type,pos,new DrugSetModel.ObjBean()));

        finish();
    }

    @Override
    public void drugSetAddError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void drugSetUpdateSuccess() {
        loadingDialog.dismiss();
        EventBus.getDefault().post(new DataEventBus(type,pos,new DrugSetModel.ObjBean()));

        finish();
    }

    @Override
    public void drugSetUpdateError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
