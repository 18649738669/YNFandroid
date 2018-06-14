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
import com.yinaf.dragon.Content.Activity.HeartReatDataAct;
import com.yinaf.dragon.Content.Bean.LineChartManager;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetCurrentHeartRateAPI;
import com.yinaf.dragon.Content.Net.GetHeartRateByDayAPI;
import com.yinaf.dragon.Content.Net.GetHeartRateByMonthAPI;
import com.yinaf.dragon.Content.Net.GetHeartRateByWeekAPI;
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
 * 功能：心率页面
 */

public class HeartRetaFrg extends BasicFrg implements GetCurrentHeartRateAPI.GetCurrentHeartRateListener,
        GetHeartRateByDayAPI.GetHeartRateByDayListener,GetHeartRateByWeekAPI.GetHeartRateByWeekListener,
        GetHeartRateByMonthAPI.GetHeartRateByMonthListener{
    @BindView(R.id.heart_reta_tv_title)
    TextView heartRetaTvTitle;
    @BindView(R.id.heart_reta_tv_content)
    TextView heartRetaTvContent;
    @BindView(R.id.heart_reta_ap)
    ArcProgress heartRetaAp;
    @BindView(R.id.heart_reta_tv_time)
    TextView heartRetaTvTime;
    @BindView(R.id.heart_reta_ll_all_date)
    LinearLayout heartRetaLlAllDate;
    @BindView(R.id.heart_reta_tv_day)
    TextView heartRetaTvDay;
    @BindView(R.id.heart_reta_tv_weeks)
    TextView heartRetaTvWeeks;
    @BindView(R.id.heart_reta_tv_month)
    TextView heartRetaTvMonth;
    @BindView(R.id.heart_reta_tv_error)
    TextView heartRetaTvError;
    @BindView(R.id.heart_reta_tv_max)
    TextView heartRetaTvMax;
    @BindView(R.id.heart_reta_tv_min)
    TextView heartRetaTvMin;
    @BindView(R.id.heart_reta_lc)
    LineChart heartRetaLc;
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


    public HeartRetaFrg() {
        super(R.layout.vp_frg_time_date_heart_reta);
    }

    @Override
    public void initView(View view) {
        loadingDialog = LoadingDialog.showDialog(getContext());
        heartRetaTvTime.setText(StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()));
        new GetCurrentHeartRateAPI(this,getActivity().getIntent().getStringExtra("memberId"));
        new GetHeartRateByDayAPI(this,StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()), getActivity().getIntent().getStringExtra("memberId"));
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
                heartRetaTvTime.setText(sf.format(date));
                GetHeartRateByDay();
            }
        })
                .setLabel("","","","","","")//默认设置为年月日
                .build();
    }

    /**
     * 获取成员每天的心率集合
     */
    @SuppressLint("ResourceAsColor")
    public void GetHeartRateByDay(){
        loadingDialog.show();
        new GetHeartRateByDayAPI(this,heartRetaTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
        heartRetaTvDay.setBackgroundResource(R.drawable.tv_background_style_6);
        heartRetaTvDay.setTextColor(Color.argb(255,255,255,255));
        heartRetaTvWeeks.setBackgroundResource(R.drawable.tv_background_style_5);
        heartRetaTvWeeks.setTextColor(Color.argb(255,233,131,127));
        heartRetaTvMonth.setBackgroundResource(R.drawable.tv_background_style_5);
        heartRetaTvMonth.setTextColor(Color.argb(255,233,131,127));
    }


    /**
     * 初始化环形进度条
     */
    private void initArcProgress() {
        heartRetaAp.setOnCenterDraw(new ArcProgress.OnCenterDraw() {
            @Override
            public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {
                Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                String progressStr = String.valueOf("");
                float textX = x - (textPaint.measureText(progressStr) / 2);
                float textY = y - ((textPaint.descent() + textPaint.ascent()) / 2);
                canvas.drawText(progressStr, textX, textY, textPaint);
            }
        });
//        addProrgress(heartRetaAp);
    }

    /**
     * 初始化柱状图表
     */
    private void initLineChart() {
        lineChartManager = new LineChartManager(heartRetaLc);
        //颜色集合
        colours.add(Color.argb(255,233,131,127));
        //线的名字集合
        names.add("心率");

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
    @OnClick({R.id.heart_reta_ll_all_date, R.id.heart_reta_tv_day, R.id.heart_reta_tv_weeks, R.id.heart_reta_tv_month,R.id.heart_reta_tv_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.heart_reta_ll_all_date:
                HeartReatDataAct.startActivity(getContext(),getActivity().getIntent().getStringExtra("memberId"));
                break;
            case R.id.heart_reta_tv_day:
                GetHeartRateByDay();
                break;
            case R.id.heart_reta_tv_weeks:
                loadingDialog.show();
                new GetHeartRateByWeekAPI(this,heartRetaTvTime.getText().toString(), getActivity().getIntent().getStringExtra("memberId"));
                heartRetaTvDay.setBackgroundResource(R.drawable.tv_background_style_5);
                heartRetaTvDay.setTextColor(Color.argb(255,233,131,127));
                heartRetaTvWeeks.setBackgroundResource(R.drawable.tv_background_style_6);
                heartRetaTvWeeks.setTextColor(Color.argb(255,255,255,255));
                heartRetaTvMonth.setBackgroundResource(R.drawable.tv_background_style_5);
                heartRetaTvMonth.setTextColor(Color.argb(255,233,131,127));
                break;
            case R.id.heart_reta_tv_month:
                loadingDialog.show();
                new GetHeartRateByMonthAPI(this, heartRetaTvTime.getText().toString().substring(0,7), getActivity().getIntent().getStringExtra("memberId"));
                heartRetaTvDay.setBackgroundResource(R.drawable.tv_background_style_5);
                heartRetaTvDay.setTextColor(Color.argb(255,233,131,127));
                heartRetaTvWeeks.setBackgroundResource(R.drawable.tv_background_style_5);
                heartRetaTvWeeks.setTextColor(Color.argb(255,233,131,127));
                heartRetaTvMonth.setBackgroundResource(R.drawable.tv_background_style_6);
                heartRetaTvMonth.setTextColor(Color.argb(255,255,255,255));
                break;
            case R.id.heart_reta_tv_time:
                pvTime.show();
                break;
        }
    }

    /**
     * 获取成员最新心率及异常心率数
     * @param content
     */
    @Override
    public void getCurrentHeartRateSuccess(JSONObject content) {

        int count = JSONUtils.getInt(content,"count");//获取卡路里
        int bpm = JSONUtils.getInt(content,"bpm");//获取步数
        heartRetaTvTitle.setText(bpm + "bpm");
        progressBarIndex = (int)((float) bpm / 150 * 100);
        addProrgress(heartRetaAp);
        loadingDialog.dismiss();

    }

    @Override
    public void getCurrentHeartRateError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每天心率变化
     * @param content
     */
    @Override
    public void getHeartRateByDaySuccess(JSONObject content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            heartRetaTvError.setText("总计 "+JSONUtils.getInt(content,"alert")+" 次异常");
            heartRetaTvMax.setText("最高值 "+JSONUtils.getInt(content,"max")+" ");
            heartRetaTvMin.setText("最低值 "+JSONUtils.getInt(content,"min")+" ");
            JSONArray data = JSONUtils.getJSONArray(content,"data");
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                String hour = JSONUtils.getString(jsonObject1,"day");
                int bpm = JSONUtils.getInt(jsonObject1,"bpm");
                list.add(i + 1 + "");
                xValues.add((float)i);
                yValue.add((float)bpm);
            }
            yValues.add(yValue);
            initLineChart();
            lineChartManager.showLineChart(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(150, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getHeartRateByDayError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每周心率变化
     * @param content
     */
    @Override
    public void getHeartRateByWeekSuccess(JSONObject content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            heartRetaTvError.setText("总计 "+JSONUtils.getInt(content,"alert")+" 次异常");
            heartRetaTvMax.setText("最高值 "+JSONUtils.getInt(content,"max")+" ");
            heartRetaTvMin.setText("最低值 "+JSONUtils.getInt(content,"min")+" ");
            JSONArray data = JSONUtils.getJSONArray(content,"data");
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                String day = JSONUtils.getString(jsonObject1,"day");
                int bpm = JSONUtils.getInt(jsonObject1,"bpm");
                list.add(i + 1 + "");
                xValues.add((float)i);
                yValue.add((float)bpm);
            }
            yValues.add(yValue);
            initLineChart();
            lineChartManager.showLineChart(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(150, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getHeartRateByWeekError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取成员每月心率变化
     * @param content
     */
    @Override
    public void getHeartRateByMonthSuccess(JSONObject content) {
        yValues.clear();
        xValues.clear();
        yValue.clear();
        list.clear();
        try {
            heartRetaTvError.setText("总计 "+JSONUtils.getInt(content,"alert")+" 次异常");
            heartRetaTvMax.setText("最高值 "+JSONUtils.getInt(content,"max")+" ");
            heartRetaTvMin.setText("最低值 "+JSONUtils.getInt(content,"min")+" ");
            JSONArray data = JSONUtils.getJSONArray(content,"data");
            for (int i = 0; i < data.length(); i++){
                JSONObject jsonObject1 = data.getJSONObject(i);
                String day = JSONUtils.getString(jsonObject1,"day");
                int bpm = JSONUtils.getInt(jsonObject1,"bpm");
                list.add(day);
                xValues.add((float)i);
                yValue.add((float)bpm);
            }
            yValues.add(yValue);
            initLineChart();
            lineChartManager.showLineChart(getContext(),xValues,yValues.get(0),names.get(0),colours.get(0),list);
            lineChartManager.setDescription("");
            lineChartManager.setYAxis(150, 0, list.size());
            loadingDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getHeartRateByMonthError(long code, String msg) {
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
