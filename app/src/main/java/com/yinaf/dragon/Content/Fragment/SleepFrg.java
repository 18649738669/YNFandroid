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
import com.github.mikephil.charting.charts.BarChart;
import com.yinaf.dragon.Content.Activity.SleepDataAct;
import com.yinaf.dragon.Content.Bean.BarChartManager;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetSleepByDayAPI;
import com.yinaf.dragon.Content.Net.GetSleepByMonthAPI;
import com.yinaf.dragon.Content.Net.GetSleepByWeekAPI;
import com.yinaf.dragon.Content.Net.GetSleepToDayAPI;
import com.yinaf.dragon.Content.Net.GetStepByDayAPI;
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
 * 功能：睡眠页面
 */

public class SleepFrg extends BasicFrg implements GetSleepToDayAPI.GetSleepToDayListener,GetSleepByDayAPI.GetSleepByDayListener,
        GetSleepByWeekAPI.GetSleepByWeekListener,GetSleepByMonthAPI.GetSleepByMonthListener{
    @BindView(R.id.sleep_tv_title_1)
    TextView sleepTvTitle1;
    @BindView(R.id.sleep_tv_title_2)
    TextView sleepTvTitle2;
    @BindView(R.id.time_date_tv_content)
    TextView timeDateTvContent;
    @BindView(R.id.sleep_tv_time)
    TextView sleepTvTime;
    @BindView(R.id.sleep_ap)
    ArcProgress sleepAp;
    @BindView(R.id.sleep_ll_all_date)
    LinearLayout sleepLlAllDate;
    @BindView(R.id.sleep_tv_day)
    TextView sleepTvDay;
    @BindView(R.id.sleep_tv_weeks)
    TextView sleepTvWeeks;
    @BindView(R.id.sleep_tv_month)
    TextView sleepTvMonth;
    @BindView(R.id.sleep_bc)
    BarChart sleepBc;
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


    BarChartManager barChartManager;//睡眠柱状图管理类
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
    List<Float> yValue1 = new ArrayList<>();
    //X轴底部显示内容
    List<String> list = new ArrayList<>();
    //时间选择器
    TimePickerView pvTime;


    public SleepFrg() {
        super(R.layout.vp_frg_time_date_sleep);
    }

    @Override
    public void initView(View view) {

        loadingDialog = LoadingDialog.showDialog(getContext());
        sleepTvTime.setText(StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()));
        loadingDialog.show();
        new GetSleepToDayAPI(this,getActivity().getIntent().getStringExtra("memberId"));
        new GetSleepByDayAPI(this,StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()) ,getActivity().getIntent().getStringExtra("memberId"));
        initArcProgress();
        initBarChart();
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
                sleepTvTime.setText(sf.format(date));
                GetSleepByDay();
            }
        })
                .setLabel("","","","","","")//默认设置为年月日
                .build();
    }

    /**
     * 获取成员每天的睡眠时间集合
     */
    @SuppressLint("ResourceAsColor")
    public void GetSleepByDay(){
        loadingDialog.show();
        new GetSleepByDayAPI(this,sleepTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
        sleepTvDay.setBackgroundResource(R.drawable.tv_background_style_4);
        sleepTvDay.setTextColor(Color.argb(255,255,255,255));
        sleepTvWeeks.setBackgroundResource(R.drawable.tv_background_style_3);
        sleepTvWeeks.setTextColor(Color.argb(255,171,105,215));
        sleepTvMonth.setBackgroundResource(R.drawable.tv_background_style_3);
        sleepTvMonth.setTextColor(Color.argb(255,171,105,215));
    }


    /**
     * 初始化环形进度条
     */
    private void initArcProgress() {
        sleepAp.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
            @Override
            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                String progressStr = String.valueOf("");
                float textX = x - (textPaint.measureText(progressStr) / 2);
                float textY = y - ((textPaint.descent() + textPaint.ascent()) / 2);
                canvas.drawText(progressStr, textX, textY, textPaint);
            }
        });
    }

    /**
     * 初始化柱状图表
     */
    private void initBarChart() {

        barChartManager = new BarChartManager(sleepBc);

        colours.add(Color.argb(255,171,105,215));
        colours.add(Color.argb(255,199,167,220));
        names.add("深睡眠 单位：m");
        names.add("浅睡眠 单位：m");

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
    @OnClick({R.id.sleep_ll_all_date, R.id.sleep_tv_day, R.id.sleep_tv_weeks, R.id.sleep_tv_month,R.id.sleep_tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sleep_ll_all_date:
                SleepDataAct.startActivity(getContext(),getActivity().getIntent().getStringExtra("memberId"));
                break;
            case R.id.sleep_tv_day:
                GetSleepByDay();
                break;
            case R.id.sleep_tv_weeks:
                loadingDialog.show();
                new GetSleepByWeekAPI(this,sleepTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
                sleepTvDay.setBackgroundResource(R.drawable.tv_background_style_3);
                sleepTvDay.setTextColor(Color.argb(255,171,105,215));
                sleepTvWeeks.setBackgroundResource(R.drawable.tv_background_style_4);
                sleepTvWeeks.setTextColor(Color.argb(255,255,255,255));
                sleepTvMonth.setBackgroundResource(R.drawable.tv_background_style_3);
                sleepTvMonth.setTextColor(Color.argb(255,171,105,215));
                break;
            case R.id.sleep_tv_month:
                loadingDialog.show();
                new GetSleepByMonthAPI(this, sleepTvTime.getText().toString().substring(0,7), getActivity().getIntent().getStringExtra("memberId"));
                sleepTvDay.setBackgroundResource(R.drawable.tv_background_style_3);
                sleepTvDay.setTextColor(Color.argb(255,171,105,215));
                sleepTvWeeks.setBackgroundResource(R.drawable.tv_background_style_3);
                sleepTvWeeks.setTextColor(Color.argb(255,171,105,215));
                sleepTvMonth.setBackgroundResource(R.drawable.tv_background_style_4);
                sleepTvMonth.setTextColor(Color.argb(255,255,255,255));
                break;
            case R.id.sleep_tv_time:
                pvTime.show();
                break;
        }
    }

    /**
     * 获取成员当天总睡眠时间
     * @param content
     */
    @Override
    public void getSleepToDaySuccess(JSONObject content) {

        int duration = JSONUtils.getInt(content,"duration");//获取总时长
        int heartDuration = JSONUtils.getInt(content,"heartDuration");//获取深睡眠时长
        int quietDuration = JSONUtils.getInt(content,"quietDuration");//获取浅睡眠时长
        sleepTvTitle1.setText("深睡眠：" + heartDuration + "h");
        sleepTvTitle2.setText("浅睡眠：" + quietDuration + "h");
        timeDateTvContent.setText("总时长：" + duration + "h");
        progressBarIndex = (int)((float) duration / 24 * 100);
        addProrgress(sleepAp);
        loadingDialog.dismiss();

    }

    @Override
    public void getSleepToDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每天睡眠时间变化
     * @param content
     */
    @Override
    public void getSleepByDaySuccess(JSONArray content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        yValue1.clear();
        list.clear();
        try {
            list.add("20:00");
            for (int i = 0; i < content.length(); i++){//添加x轴数据
                JSONObject jsonObject = content.getJSONObject(i);
                String hour = JSONUtils.getString(jsonObject,"hour");
                list.add(hour);
                xValues.add((float)i);
            }
            for (int j = 0; j < content.length(); j++){//添加y轴数据
                JSONObject jsonObject = content.getJSONObject(j);
                int heartDuration = JSONUtils.getInt(jsonObject,"heartDuration");//深睡眠
                yValue.add((float)heartDuration);
            }
            yValues.add(yValue);
            for (int j = 0; j < content.length(); j++){//添加y轴数据
                JSONObject jsonObject = content.getJSONObject(j);
                int quietDuration = JSONUtils.getInt(jsonObject,"quietDuration");//浅睡眠
                yValue1.add((float)quietDuration);
            }
            yValues.add(yValue1);
            initBarChart();
            barChartManager.showBarChart(getContext(),xValues,yValues,names,colours,list);
            barChartManager.setDescription("");
            barChartManager.setXAxis(xValues.size()+1,0f,xValues.size()+1);
            loadingDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSleepByDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每周睡眠时间变化
     * @param content
     */
    @Override
    public void getSleepByWeekSuccess(JSONArray content) {

        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            for (int i = 0; i < content.length(); i++){//添加x轴数据
                JSONObject jsonObject = content.getJSONObject(i);
                String day = JSONUtils.getString(jsonObject,"day");
                list.add(i + 1 + "");
                xValues.add((float)i);
            }
            for (int j = 0; j < content.length(); j++){//添加y轴数据
                JSONObject jsonObject = content.getJSONObject(j);
                int heartDuration = JSONUtils.getInt(jsonObject,"heartDuration");//深睡眠
                yValue.add((float)heartDuration);
            }
            yValues.add(yValue);
            yValue.clear();
            for (int j = 0; j < content.length(); j++){//添加y轴数据
                JSONObject jsonObject = content.getJSONObject(j);
                int quietDuration = JSONUtils.getInt(jsonObject,"quietDuration");//浅睡眠
                yValue.add((float)quietDuration);
            }
            yValues.add(yValue);
            initBarChart();
            barChartManager.showBarChart(getContext(),xValues,yValues,names,colours,list);
            barChartManager.setXAxis(xValues.size()+1,0f,xValues.size()+1);
            barChartManager.setDescription("");
            loadingDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getSleepByWeekError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每月睡眠时间变化
     * @param content
     */
    @Override
    public void getSleepByMonthSuccess(JSONArray content) {

        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            list.add("0");
            for (int i = 0; i < content.length(); i++){//添加x轴数据
                JSONObject jsonObject = content.getJSONObject(i);
                String day = JSONUtils.getString(jsonObject,"day");
                list.add(day);
                xValues.add((float)i);
            }
            for (int j = 0; j < content.length(); j++){//添加y轴数据
                JSONObject jsonObject = content.getJSONObject(j);
                int heartDuration = JSONUtils.getInt(jsonObject,"heartDuration");//深睡眠
                yValue.add((float)heartDuration);
            }
            yValues.add(yValue);
            yValue.clear();
            for (int j = 0; j < content.length(); j++){//添加y轴数据
                JSONObject jsonObject = content.getJSONObject(j);
                int quietDuration = JSONUtils.getInt(jsonObject,"quietDuration");//浅睡眠
                yValue.add((float)quietDuration);
            }
            yValues.add(yValue);
            initBarChart();
            barChartManager.showBarChart(getContext(),xValues,yValues,names,colours,list);
            barChartManager.setDescription("");
//            barChartManager.setXAxis(xValues.size()+1,0f,xValues.size()+1);
            loadingDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getSleepByMonthError(long code, String msg) {
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
