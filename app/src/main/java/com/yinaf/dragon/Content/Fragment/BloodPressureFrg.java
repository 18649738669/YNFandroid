package com.yinaf.dragon.Content.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.czp.library.ArcProgress;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.yinaf.dragon.Content.Activity.BloodPressureDataAct;
import com.yinaf.dragon.Content.Bean.LineChartManager;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetBloodPressureByDayAPI;
import com.yinaf.dragon.Content.Net.GetBloodPressureByMonthAPI;
import com.yinaf.dragon.Content.Net.GetBloodPressureByWeekAPI;
import com.yinaf.dragon.Content.Net.GetBloodPressureToDayAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicFrg;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by long on 2018/04/25.
 * 功能：血压页面
 */

public class BloodPressureFrg extends BasicFrg implements GetBloodPressureToDayAPI.GetBloodPressureToDayListener,
        GetBloodPressureByDayAPI.GetBloodPressureByDayListener,GetBloodPressureByWeekAPI.GetBloodPressureByWeekListener,
        GetBloodPressureByMonthAPI.GetBloodPressureByMonthListener{
    @BindView(R.id.blood_pressure_tv_title)
    TextView bloodPressureTvTitle;
    @BindView(R.id.blood_pressure_tv_title_1)
    TextView bloodPressureTvTitle1;
    @BindView(R.id.blood_pressure_tv_content)
    TextView bloodPressureTvContent;
    @BindView(R.id.blood_pressure_ap)
    ArcProgress bloodPressureAp;
    @BindView(R.id.blood_pressure_tv_time)
    TextView bloodPressureTvTime;
    @BindView(R.id.blood_pressure_ll_all_date)
    LinearLayout bloodPressureLlAllDate;
    @BindView(R.id.blood_pressure_tv_day)
    TextView bloodPressureTvDay;
    @BindView(R.id.blood_pressure_tv_weeks)
    TextView bloodPressureTvWeeks;
    @BindView(R.id.blood_pressure_tv_month)
    TextView bloodPressureTvMonth;
    @BindView(R.id.blood_pressure_tv_error)
    TextView bloodPressureTvError;
    @BindView(R.id.blood_pressure_lc)
    LineChart bloodPressureLc;
    Unbinder unbinder;

    //更新UI
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArcProgress progressBar = (ArcProgress) msg.obj;
            progressBar.setProgress(msg.what);
        }
    };


    LineChartManager lineChartManager;//折线图管理类
    LoadingDialog loadingDialog;
    int progressBarIndex;//进度条的进度

    //设置x轴的数据
    ArrayList<Float> xValues = new ArrayList<>();
    //设置y轴的数据()
    List<List<Float>> yValues = new ArrayList<>();
    //颜色集合
    List<Integer> colours = new ArrayList<>();
    //线的名字集合
    List<String> names = new ArrayList<>();

    List<Float> yValue = new ArrayList<>();
    //X轴底部显示内容
    List<String> list = new ArrayList<>();

    //时间选择器
    TimePickerView pvTime;


    public BloodPressureFrg() {
        super(R.layout.vp_frg_time_date_blood_pressure);
    }

    @Override
    public void initView(View view) {

        loadingDialog = LoadingDialog.showDialog(getContext());
        bloodPressureTvTime.setText(StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()));
        loadingDialog.show();
        new GetBloodPressureToDayAPI(this,getActivity().getIntent().getStringExtra("memberId"));
        new GetBloodPressureByDayAPI(this,StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()), getActivity().getIntent().getStringExtra("memberId"));
        initArcProgress();
        initLineChart();
        initPickerView();

    }

    /**
     * 时间选择器控件
     */
    public void initPickerView(){
        pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                bloodPressureTvTime.setText(sf.format(date));
                GetBloodPressureByDay();
            }
        })
                .setLabel("","","","","","")//默认设置为年月日
                .build();
    }

    /**
     * 获取成员每天的血压集合
     */
    @SuppressLint("ResourceAsColor")
    public void GetBloodPressureByDay(){
        loadingDialog.show();
        new GetBloodPressureByDayAPI(this,bloodPressureTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
        bloodPressureTvDay.setBackgroundResource(R.drawable.tv_background_style_8);
        bloodPressureTvDay.setTextColor(Color.argb(255,255,255,255));
        bloodPressureTvWeeks.setBackgroundResource(R.drawable.tv_background_style_7);
        bloodPressureTvWeeks.setTextColor(Color.argb(255,230,119,67));
        bloodPressureTvMonth.setBackgroundResource(R.drawable.tv_background_style_7);
        bloodPressureTvMonth.setTextColor(Color.argb(255,230,119,67));
    }

    /**
     * 初始化环形进度条
     */
    private void initArcProgress() {
        bloodPressureAp.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
            @Override
            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                String progressStr = String.valueOf("");
                float textX = x - (textPaint.measureText(progressStr) / 2);
                float textY = y - ((textPaint.descent() + textPaint.ascent()) / 2);
                canvas.drawText(progressStr, textX, textY, textPaint);
            }
        });
//        addProrgress(bloodPressureAp);
    }

    /**
     * 初始化柱状图表
     */
    private void initLineChart() {

        lineChartManager = new LineChartManager(bloodPressureLc);
        //颜色集合
        colours.add(Color.argb(255,230,119,67));
        colours.add(Color.argb(255,233,206,136));
        //线的名字集合
        names.add("收缩压");
        names.add("舒张压");


    }

    //开启线程的方法
    public void addProrgress(ArcProgress progressBar) {
        Thread thread = new Thread(new ProgressThread(progressBar));
        thread.start();
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
    @OnClick({R.id.blood_pressure_ll_all_date, R.id.blood_pressure_tv_day, R.id.blood_pressure_tv_weeks, R.id.blood_pressure_tv_month,R.id.blood_pressure_tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.blood_pressure_ll_all_date:
                BloodPressureDataAct.startActivity(getContext(),getActivity().getIntent().getStringExtra("memberId"));
                break;
            case R.id.blood_pressure_tv_day:
                GetBloodPressureByDay();
                break;
            case R.id.blood_pressure_tv_weeks:
                loadingDialog.show();
                new GetBloodPressureByWeekAPI(this,bloodPressureTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
                bloodPressureTvDay.setBackgroundResource(R.drawable.tv_background_style_7);
                bloodPressureTvDay.setTextColor(Color.argb(255,230,119,67));
                bloodPressureTvWeeks.setBackgroundResource(R.drawable.tv_background_style_8);
                bloodPressureTvWeeks.setTextColor(Color.argb(255,255,255,255));
                bloodPressureTvMonth.setBackgroundResource(R.drawable.tv_background_style_7);
                bloodPressureTvMonth.setTextColor(Color.argb(255,230,119,67));
                break;
            case R.id.blood_pressure_tv_month:
                loadingDialog.show();
                new GetBloodPressureByMonthAPI(this,bloodPressureTvTime.getText().toString().substring(0,7), getActivity().getIntent().getStringExtra("memberId"));
                bloodPressureTvDay.setBackgroundResource(R.drawable.tv_background_style_7);
                bloodPressureTvDay.setTextColor(Color.argb(255,230,119,67));
                bloodPressureTvWeeks.setBackgroundResource(R.drawable.tv_background_style_7);
                bloodPressureTvWeeks.setTextColor(Color.argb(255,230,119,67));
                bloodPressureTvMonth.setBackgroundResource(R.drawable.tv_background_style_8);
                bloodPressureTvMonth.setTextColor(Color.argb(255,255,255,255));
                break;
            case R.id.blood_pressure_tv_time:
                pvTime.show();
                break;
        }
    }

    /**
     * 获取成员当天血压
     * @param content
     */
    @Override
    public void getBloodPressureToDaySuccess(JSONObject content) {

        int dbp = JSONUtils.getInt(content,"dbp");//获取高压/收缩压
        int sbp = JSONUtils.getInt(content,"sbp");//获取低压/舒张压
        bloodPressureTvTitle.setText("高压：" + dbp);
        bloodPressureTvTitle1.setText("低压：" + sbp);
        bloodPressureTvContent.setVisibility(View.GONE);
        progressBarIndex = (int)((float) dbp / 140 * 100);
        addProrgress(bloodPressureAp);
        loadingDialog.dismiss();
    }

    @Override
    public void getBloodPressureToDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);

    }

    /**
     * 获取成员每天血压
     * @param content
     */
    @Override
    public void getBloodPressureByDaySuccess(JSONObject content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            bloodPressureTvError.setText("总计 "+JSONUtils.getInt(content,"alert")+" 次异常");
            JSONArray data = JSONUtils.getJSONArray(content,"data");
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                String hour = JSONUtils.getString(jsonObject1,"hour");
                list.add(hour);
                xValues.add((float)i);
            }
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                int sbp = JSONUtils.getInt(jsonObject1,"sbp");
                yValue.add((float)sbp);
            }
            yValues.add(yValue);
            List<Float> yValue1 = new ArrayList<>();
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                int dbp = JSONUtils.getInt(jsonObject1,"dbp");
                yValue1.add((float)dbp);
            }
            yValues.add(yValue1);
            initLineChart();
            //创建多条折线的图表
            lineChartManager.showLineChart(getContext(),xValues, yValues, names, colours,list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(100, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getBloodPressureByDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每周血压
     * @param content
     */
    @Override
    public void getBloodPressureByWeekSuccess(JSONObject content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            bloodPressureTvError.setText("总计 "+JSONUtils.getInt(content,"alert")+" 次异常");
            JSONArray data = JSONUtils.getJSONArray(content,"data");
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                String day = JSONUtils.getString(jsonObject1,"day");
                list.add(i + 1 +"");
                xValues.add((float)i);
            }
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                int sbp = JSONUtils.getInt(jsonObject1,"sbp");
                yValue.add((float)sbp);
            }
            yValues.add(yValue);
            List<Float> yValue1 = new ArrayList<>();
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                int dbp = JSONUtils.getInt(jsonObject1,"dbp");
                yValue1.add((float)dbp);
            }
            yValues.add(yValue1);
            initLineChart();
            //创建多条折线的图表
            lineChartManager.showLineChart(getContext(),xValues, yValues, names, colours,list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(100, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBloodPressureByWeekError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每月血压
     * @param content
     */
    @Override
    public void getBloodPressureByMonthSuccess(JSONObject content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            bloodPressureTvError.setText("总计 "+JSONUtils.getInt(content,"alert")+" 次异常");
            JSONArray data = JSONUtils.getJSONArray(content,"data");
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                String day = JSONUtils.getString(jsonObject1,"day");
                list.add(day);
                xValues.add((float)i);
            }
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                int sbp = JSONUtils.getInt(jsonObject1,"sbp");
                yValue.add((float)sbp);
            }
            yValues.add(yValue);
            List<Float> yValue1 = new ArrayList<>();
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                int dbp = JSONUtils.getInt(jsonObject1,"dbp");
                yValue1.add((float)dbp);
            }
            yValues.add(yValue1);
            initLineChart();
            //创建多条折线的图表
            lineChartManager.showLineChart(getContext(),xValues, yValues, names, colours,list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(100, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBloodPressureByMonthError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    //子线程
    class ProgressThread implements Runnable {
        int i = 0;
        private ArcProgress progressBar;

        public ProgressThread(ArcProgress progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            for (i = 0; i <= progressBarIndex; i++) {
                //实例化消息对象
                Message msg = new Message();
                msg.what = i;
                //发送进度条的进度
                msg.obj = progressBar;
                //系统时钟 睡 100 毫秒
                SystemClock.sleep(100);
                handler.sendMessage(msg);
            }
        }
    }
}
