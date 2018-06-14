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
import com.yinaf.dragon.Content.Activity.BloodSugarDataAct;
import com.yinaf.dragon.Content.Bean.LineChartManager;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetBloodPressureByDayAPI;
import com.yinaf.dragon.Content.Net.GetBloodPressureToDayAPI;
import com.yinaf.dragon.Content.Net.GetBloodSugarByDayAPI;
import com.yinaf.dragon.Content.Net.GetBloodSugarByMonthAPI;
import com.yinaf.dragon.Content.Net.GetBloodSugarByWeekAPI;
import com.yinaf.dragon.Content.Net.GetBloodSugarToDayAPI;
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
 * 功能：血糖页面
 */

public class BloodSugarFrg extends BasicFrg implements GetBloodSugarToDayAPI.GetBloodSugarToDayListener,
        GetBloodSugarByDayAPI.GetBloodSugarByDayListener,GetBloodSugarByWeekAPI.GetBloodSugarByWeekListener,
        GetBloodSugarByMonthAPI.GetBloodSugarByMonthListener{

    @BindView(R.id.blood_suger_tv_title)
    TextView bloodSugerTvTitle;
    @BindView(R.id.blood_suger_tv_title_1)
    TextView bloodSugerTvTitle1;
    @BindView(R.id.blood_suge_ap)
    ArcProgress bloodSugerAp;
    @BindView(R.id.blood_suger_tv_time)
    TextView bloodSugerTvTime;
    @BindView(R.id.blood_suger_ll_all_date)
    LinearLayout bloodSugerLlAllDate;
    @BindView(R.id.blood_suger_tv_day)
    TextView bloodSugerTvDay;
    @BindView(R.id.blood_suger_tv_weeks)
    TextView bloodSugerTvWeeks;
    @BindView(R.id.blood_suger_tv_month)
    TextView bloodSugerTvMonth;
    @BindView(R.id.blood_suger_tv_error)
    TextView bloodSugerTvError;
    @BindView(R.id.blood_suger_lc)
    LineChart bloodSugerLc;
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

    public BloodSugarFrg() {
        super(R.layout.vp_frg_time_date_blood_sugar);
    }

    @Override
    public void initView(View view) {

        loadingDialog = LoadingDialog.showDialog(getContext());
        bloodSugerTvTime.setText(StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()));
        loadingDialog.show();
        new GetBloodSugarToDayAPI(this,getActivity().getIntent().getStringExtra("memberId"));
        new GetBloodSugarByDayAPI(this,StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()), getActivity().getIntent().getStringExtra("memberId"));
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
                bloodSugerTvTime.setText(sf.format(date));
                GetBloodSugarByDay();
            }
        })
                .setLabel("","","","","","")//默认设置为年月日
                .build();
    }

    /**
     * 获取成员每天的血压集合
     */
    @SuppressLint("ResourceAsColor")
    public void GetBloodSugarByDay(){
        loadingDialog.show();
        new GetBloodSugarByDayAPI(this,bloodSugerTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
        bloodSugerTvDay.setBackgroundResource(R.drawable.tv_background_style_10);
        bloodSugerTvDay.setTextColor(Color.argb(255,255,255,255));
        bloodSugerTvWeeks.setBackgroundResource(R.drawable.tv_background_style_9);
        bloodSugerTvWeeks.setTextColor(Color.argb(255,233,206,136));
        bloodSugerTvMonth.setBackgroundResource(R.drawable.tv_background_style_9);
        bloodSugerTvMonth.setTextColor(Color.argb(255,233,206,136));
    }


    /**
     * 初始化环形进度条
     */
    private void initArcProgress() {
        bloodSugerAp.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
            @Override
            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                String progressStr = String.valueOf("");
                float textX = x - (textPaint.measureText(progressStr) / 2);
                float textY = y - ((textPaint.descent() + textPaint.ascent()) / 2);
                canvas.drawText(progressStr, textX, textY, textPaint);
            }
        });
//        addProrgress(bloodSugerAp);
    }

    /**
     * 初始化柱状图表
     */
    private void initLineChart() {

        lineChartManager = new LineChartManager(bloodSugerLc);
        //颜色集合
        colours.add(Color.argb(255,233,206,136));
        //线的名字集合
        names.add("血糖");

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
    @OnClick({R.id.blood_suger_ll_all_date, R.id.blood_suger_tv_day, R.id.blood_suger_tv_weeks, R.id.blood_suger_tv_month,R.id.blood_suger_tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.blood_suger_ll_all_date:
                BloodSugarDataAct.startActivity(getContext(),getActivity().getIntent().getStringExtra("memberId"));
                break;
            case R.id.blood_suger_tv_day:
                GetBloodSugarByDay();
                break;
            case R.id.blood_suger_tv_weeks:
                loadingDialog.show();
                new GetBloodSugarByWeekAPI(this,bloodSugerTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
                bloodSugerTvDay.setBackgroundResource(R.drawable.tv_background_style_9);
                bloodSugerTvDay.setTextColor(Color.argb(255,233,206,136));
                bloodSugerTvWeeks.setBackgroundResource(R.drawable.tv_background_style_10);
                bloodSugerTvWeeks.setTextColor(Color.argb(255,255,255,255));
                bloodSugerTvMonth.setBackgroundResource(R.drawable.tv_background_style_9);
                bloodSugerTvMonth.setTextColor(Color.argb(255,255,255,255));
                break;
            case R.id.blood_suger_tv_month:
                loadingDialog.show();
                new GetBloodSugarByMonthAPI(this,bloodSugerTvTime.getText().toString().substring(0,7), getActivity().getIntent().getStringExtra("memberId"));
                bloodSugerTvDay.setBackgroundResource(R.drawable.tv_background_style_9);
                bloodSugerTvDay.setTextColor(Color.argb(255,233,206,136));
                bloodSugerTvWeeks.setBackgroundResource(R.drawable.tv_background_style_9);
                bloodSugerTvWeeks.setTextColor(Color.argb(255,233,206,136));
                bloodSugerTvMonth.setBackgroundResource(R.drawable.tv_background_style_10);
                bloodSugerTvMonth.setTextColor(Color.argb(255,255,255,255));
                break;
            case R.id.blood_suger_tv_time:
                pvTime.show();
                break;
        }
    }

    /**
     * 获取成员当天血糖
     * @param content
     */
    @Override
    public void getBloodSugarToDaySuccess(JSONObject content) {

        int gi = JSONUtils.getInt(content,"gi");//获取血糖
        bloodSugerTvTitle.setText("血糖：" + gi + "mmol/L");
        bloodSugerTvTitle1.setVisibility(View.GONE);
        progressBarIndex = (int)((float) gi / 7 * 100);
        addProrgress(bloodSugerAp);
        loadingDialog.dismiss();
    }

    @Override
    public void getBloodSugarToDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
    /**
     * 获取成员每天血糖
     * @param content
     */
    @Override
    public void getBloodSugarByDaySuccess(JSONArray content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            for (int i = 0; i < content.length(); i++){
                JSONObject jsonObject = content.getJSONObject(i);
                String hour = JSONUtils.getString(jsonObject,"hour");
                int gi = JSONUtils.getInt(jsonObject,"gi");
                list.add(hour);
                xValues.add((float)i);
                yValue.add((float)gi);
            }
            yValues.add(yValue);
            initLineChart();
            lineChartManager.showLineChart1(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(10, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBloodSugarByDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每周血糖
     * @param content
     */
    @Override
    public void getBloodSugarByWeekSuccess(JSONArray content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            for (int i = 0; i < content.length(); i++){
                JSONObject jsonObject = content.getJSONObject(i);
                String day = JSONUtils.getString(jsonObject,"day");
                int gi = JSONUtils.getInt(jsonObject,"gi");
                list.add(i + 1 +"");
                xValues.add((float)i);
                yValue.add((float)gi);
            }
            yValues.add(yValue);
            initLineChart();
            lineChartManager.showLineChart1(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(10, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBloodSugarByWeekError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每月血糖
     * @param content
     */
    @Override
    public void getBloodSugarByMonthSuccess(JSONArray content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            for (int i = 0; i < content.length(); i++){
                JSONObject jsonObject = content.getJSONObject(i);
                String day = JSONUtils.getString(jsonObject,"day");
                int gi = JSONUtils.getInt(jsonObject,"gi");
                list.add(day);
                xValues.add((float)i);
                yValue.add((float)gi);
            }
            yValues.add(yValue);
            initLineChart();
            lineChartManager.showLineChart1(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(10, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBloodSugarByMonthError(long code, String msg) {
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
