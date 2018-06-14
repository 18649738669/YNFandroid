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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.czp.library.ArcProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.yinaf.dragon.Content.Activity.StepGaugeDataAct;
import com.yinaf.dragon.Content.Activity.TimeDateAct;
import com.yinaf.dragon.Content.Bean.BarChartManager;
import com.yinaf.dragon.Content.Bean.StepGuageData;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetStepByDayAPI;
import com.yinaf.dragon.Content.Net.GetStepByMonthAPI;
import com.yinaf.dragon.Content.Net.GetStepByWeekAPI;
import com.yinaf.dragon.Content.Net.GetStepToDayAPI;
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
 * 功能：计步页面
 */

public class StepGaugeFrg extends BasicFrg implements GetStepToDayAPI.GetStepToDayListener,
        GetStepByDayAPI.GetStepByDayListener,GetStepByWeekAPI.GetStepByWeekListener,
        GetStepByMonthAPI.GetStepByMonthListener{

    @BindView(R.id.step_gauge_ap)
    ArcProgress stepGaugeAp;
    @BindView(R.id.step_gauge_ll_all_date)
    LinearLayout stepGaugeLlAllDate;
    @BindView(R.id.step_gauge_tv_time)
    TextView stepGaugeTvTime;
    @BindView(R.id.step_gauge_tv_title)
    TextView stepGaugeTvTitle;
    @BindView(R.id.step_gauge_tv_content)
    TextView stepGaugeTvContent;
    @BindView(R.id.step_gauge_tv_day)
    TextView stepGaugeTvDay;
    @BindView(R.id.step_gauge_tv_weeks)
    TextView stepGaugeTvWeeks;
    @BindView(R.id.step_gauge_tv_month)
    TextView stepGaugeTvMonth;
    @BindView(R.id.step_gauge_bc)
    BarChart stepGaugeBc;
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

    BarChartManager barChartManager;//柱状图管理类
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

    public StepGaugeFrg() {
        super(R.layout.vp_frg_time_date_step_gauge);
    }

    @Override
    public void initView(View view) {
        loadingDialog = LoadingDialog.showDialog(getContext());
        stepGaugeTvTime.setText(StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()));
        loadingDialog.show();
        new GetStepToDayAPI(this,getActivity().getIntent().getStringExtra("memberId"));
        new GetStepByDayAPI(this,StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()) ,getActivity().getIntent().getStringExtra("memberId"));
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
                stepGaugeTvTime.setText(sf.format(date));
                GetStepByDay();
            }
        })
                .setLabel("","","","","","")//默认设置为年月日
                .build();
    }

    /**
     * 获取成员每天的步数集合
     */
    @SuppressLint("ResourceAsColor")
    public void GetStepByDay(){
        loadingDialog.show();
        new GetStepByDayAPI(this,stepGaugeTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
        stepGaugeTvDay.setBackgroundResource(R.drawable.tv_background_style_2);
        stepGaugeTvDay.setTextColor(Color.argb(255,255,255,255));
        stepGaugeTvWeeks.setBackgroundResource(R.drawable.tv_background_style_1);
        stepGaugeTvWeeks.setTextColor(Color.argb(255,0,170,239));
        stepGaugeTvMonth.setBackgroundResource(R.drawable.tv_background_style_1);
        stepGaugeTvMonth.setTextColor(Color.argb(255,0,170,239));
    }

    /**
     * 初始化环形进度条
     */
    private void initArcProgress() {
        stepGaugeAp.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
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

        barChartManager = new BarChartManager(stepGaugeBc);
        colours.add(Color.argb(255,0,170,239));
        names.add("计步");

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
    @OnClick({R.id.step_gauge_ll_all_date,R.id.step_gauge_tv_day, R.id.step_gauge_tv_weeks, R.id.step_gauge_tv_month, R.id.step_gauge_tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.step_gauge_ll_all_date:
                StepGaugeDataAct.startActivity(getContext(),getActivity().getIntent().getStringExtra("memberId"));
                break;
            case R.id.step_gauge_tv_day:
                GetStepByDay();
                break;
            case R.id.step_gauge_tv_weeks:
                loadingDialog.show();
                new GetStepByWeekAPI(this,stepGaugeTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
                stepGaugeTvDay.setBackgroundResource(R.drawable.tv_background_style_1);
                stepGaugeTvDay.setTextColor(Color.argb(255,0,170,239));
                stepGaugeTvWeeks.setBackgroundResource(R.drawable.tv_background_style_2);
                stepGaugeTvWeeks.setTextColor(Color.argb(255,255,255,255));
                stepGaugeTvMonth.setBackgroundResource(R.drawable.tv_background_style_1);
                stepGaugeTvMonth.setTextColor(Color.argb(255,0,170,239));
                break;
            case R.id.step_gauge_tv_month:
                loadingDialog.show();
                new GetStepByMonthAPI(this,stepGaugeTvTime.getText().toString().substring(0,7), getActivity().getIntent().getStringExtra("memberId"));
                stepGaugeTvDay.setBackgroundResource(R.drawable.tv_background_style_1);
                stepGaugeTvDay.setTextColor(Color.argb(255,0,170,239));
                stepGaugeTvWeeks.setBackgroundResource(R.drawable.tv_background_style_1);
                stepGaugeTvWeeks.setTextColor(Color.argb(255,0,170,239));
                stepGaugeTvMonth.setBackgroundResource(R.drawable.tv_background_style_2);
                stepGaugeTvMonth.setTextColor(Color.argb(255,255,255,255));
                break;
            case R.id.step_gauge_tv_time:
                pvTime.show();
                break;
        }
    }

    /**
     * 获取成员当天总步数
     * @param content
     */
    @Override
    public void getStepToDaySuccess(JSONObject content) {

        int calorie = JSONUtils.getInt(content,"calorie");//获取卡路里
        int step = JSONUtils.getInt(content,"step");//获取步数
        stepGaugeTvTitle.setText(step + "步");
        stepGaugeTvContent.setText("消耗热量"+calorie+"kcal");
        progressBarIndex = (int)((float) step / 20000 * 100);
        addProrgress(stepGaugeAp);
        loadingDialog.dismiss();

    }

    @Override
    public void getStepToDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每天步数变化
     * @param content
     */
    @Override
    public void getStepByDaySuccess(JSONArray content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            for (int i = 0; i < content.length(); i++){
                JSONObject jsonObject = content.getJSONObject(i);
                String hour = JSONUtils.getString(jsonObject,"hour");
                int step = JSONUtils.getInt(jsonObject,"step");
                list.add(hour);
                xValues.add((float)i);
                yValue.add((float)step);
            }
            yValues.add(yValue);
            initBarChart();
            barChartManager.showBarChart(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            barChartManager.setDescription("");
            loadingDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getStepByDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每周步数变化
     * @param content
     */
    @Override
    public void getStepByWeekSuccess(JSONArray content) {

        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            for (int i = 0; i < content.length(); i++){
                JSONObject jsonObject = content.getJSONObject(i);
                String hour = JSONUtils.getString(jsonObject,"day");
                int step = JSONUtils.getInt(jsonObject,"step");
                list.add(i + 1 +"");
                xValues.add((float)i);
                yValue.add((float)step);
            }
            yValues.add(yValue);
            initBarChart();
            barChartManager.showBarChart(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            barChartManager.setDescription("");
            loadingDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getStepByWeekError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每月步数变化
     * @param content
     */
    @Override
    public void getStepByMonthSuccess(JSONArray content) {

        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            for (int i = 0; i < content.length(); i++){
                JSONObject jsonObject = content.getJSONObject(i);
                String hour = JSONUtils.getString(jsonObject,"day");
                int step = JSONUtils.getInt(jsonObject,"step");
                list.add(hour);
                xValues.add((float)i);
                yValue.add((float)step);
            }
            yValues.add(yValue);
            initBarChart();
            barChartManager.showBarChart(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            barChartManager.setDescription("");
            loadingDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getStepByMonthError(long code, String msg) {

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
