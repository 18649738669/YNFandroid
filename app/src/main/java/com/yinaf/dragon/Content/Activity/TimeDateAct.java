package com.yinaf.dragon.Content.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yinaf.dragon.Content.Fragment.BloodPressureFrg;
import com.yinaf.dragon.Content.Fragment.BloodSugarFrg;
import com.yinaf.dragon.Content.Fragment.HeartRetaFrg;
import com.yinaf.dragon.Content.Fragment.SleepFrg;
import com.yinaf.dragon.Content.Fragment.StepGaugeFrg;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/04/25.
 * 功能：实时数据页面
 */

public class TimeDateAct extends BasicAct {


    @BindView(R.id.tool_bar_time_date_btn_back)
    RelativeLayout toolBarTimeDateBtnBack;
    @BindView(R.id.tool_bar_time_date_title_step_gauge)
    TextView toolBarTimeDateTitleStepGauge;
    @BindView(R.id.tool_bar_time_date_title_sleep)
    TextView toolBarTimeDateTitleSleep;
    @BindView(R.id.tool_bar_time_date_title_heart_rate)
    TextView toolBarTimeDateTitleHeartRate;
    @BindView(R.id.tool_bar_time_date_title_blood_pressure)
    TextView toolBarTimeDateTitleBloodPressure;
    @BindView(R.id.tool_bar_time_date_title_blood_sugar)
    TextView toolBarTimeDateTitleBloodSugar;
    @BindView(R.id.tb_time_date_toolbar)
    Toolbar tbTimeDateToolbar;
    @BindView(R.id.time_date_vp)
    ViewPager timeDateVp;
    @BindView(R.id.tool_bar_time_date)
    RelativeLayout toolBarTimeDate;

    StepGaugeFrg stepGaugeFrg;//计步页面
    SleepFrg sleepFrg;//睡眠页面
    HeartRetaFrg heartRetaFrg;//心率页面
    BloodPressureFrg bloodPressureFrg;//血压页面
    BloodSugarFrg bloodSugarFrg;//血糖页面

    List<Fragment> pagerViewList = new ArrayList<Fragment>(); //所需展示的页面集合
    int vp_index = 0;//当前加载的页面下标
    public String memberId;
    SystemBarTintManager tintManager;


    public TimeDateAct() {
        super(R.layout.act_time_date, R.string.title_activity_time_date, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId) {
        Intent intent = new Intent(context, TimeDateAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        memberId = getIntent().getStringExtra("memberId");
        //初始化ViewPager
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater = getLayoutInflater();

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);


        stepGaugeFrg = new StepGaugeFrg();
        sleepFrg = new SleepFrg();
        heartRetaFrg = new HeartRetaFrg();
        bloodPressureFrg = new BloodPressureFrg();
        bloodSugarFrg = new BloodSugarFrg();
        pagerViewList.add(stepGaugeFrg);
        pagerViewList.add(sleepFrg);
        pagerViewList.add(heartRetaFrg);
        pagerViewList.add(bloodPressureFrg);
        pagerViewList.add(bloodSugarFrg);
        timeDateVp.setAdapter(new PagerAdater(getSupportFragmentManager()));
        timeDateVp.setCurrentItem(vp_index);
        modifyTitleSize();
        timeDateVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vp_index = position;
                modifyTitleSize();
                if (position == 4) {
                    TipUtils.showTip("已是最后一页");
                } else if (position == 0) {
                    TipUtils.showTip("已是第一页");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == 1) {
                    modifyTitleSize();
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.tool_bar_time_date_btn_back, R.id.tool_bar_time_date_title_step_gauge,
            R.id.tool_bar_time_date_title_sleep, R.id.tool_bar_time_date_title_heart_rate,
            R.id.tool_bar_time_date_title_blood_pressure, R.id.tool_bar_time_date_title_blood_sugar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_time_date_btn_back:
                finish();
                break;
            case R.id.tool_bar_time_date_title_step_gauge://计步
                vp_index = 0;
                timeDateVp.setCurrentItem(vp_index);
                modifyTitleSize();
                break;
            case R.id.tool_bar_time_date_title_sleep://睡眠
                vp_index = 1;
                timeDateVp.setCurrentItem(vp_index);
                modifyTitleSize();
                break;
            case R.id.tool_bar_time_date_title_heart_rate://心率
                vp_index = 2;
                timeDateVp.setCurrentItem(vp_index);
                modifyTitleSize();
                break;
            case R.id.tool_bar_time_date_title_blood_pressure://血压
                vp_index = 3;
                timeDateVp.setCurrentItem(vp_index);
                modifyTitleSize();
                break;
            case R.id.tool_bar_time_date_title_blood_sugar://血糖
                vp_index = 4;
                timeDateVp.setCurrentItem(vp_index);
                modifyTitleSize();
                break;
        }
    }

    /**
     * 修改title的大小
     */
    @SuppressLint("ResourceAsColor")
    private void modifyTitleSize() {

        switch (vp_index) {
            case 0:
                toolBarTimeDateTitleStepGauge.setTextSize(22);
                toolBarTimeDateTitleSleep.setTextSize(18);
                toolBarTimeDateTitleHeartRate.setTextSize(18);
                toolBarTimeDateTitleBloodPressure.setTextSize(18);
                toolBarTimeDateTitleBloodSugar.setTextSize(18);
                toolBarTimeDate.setBackgroundResource(R.color.common_blue);
                tintManager.setStatusBarTintResource(R.color.common_blue);
                break;
            case 1:
                toolBarTimeDateTitleStepGauge.setTextSize(18);
                toolBarTimeDateTitleSleep.setTextSize(22);
                toolBarTimeDateTitleHeartRate.setTextSize(18);
                toolBarTimeDateTitleBloodPressure.setTextSize(18);
                toolBarTimeDateTitleBloodSugar.setTextSize(18);
                toolBarTimeDate.setBackgroundResource(R.color.common_purle);
                tintManager.setStatusBarTintResource(R.color.common_purle);
                break;
            case 2:
                toolBarTimeDateTitleStepGauge.setTextSize(18);
                toolBarTimeDateTitleSleep.setTextSize(18);
                toolBarTimeDateTitleHeartRate.setTextSize(22);
                toolBarTimeDateTitleBloodPressure.setTextSize(18);
                toolBarTimeDateTitleBloodSugar.setTextSize(18);
                toolBarTimeDate.setBackgroundResource(R.color.common_pink);
                tintManager.setStatusBarTintResource(R.color.common_pink);
                break;
            case 3:
                toolBarTimeDateTitleStepGauge.setTextSize(18);
                toolBarTimeDateTitleSleep.setTextSize(18);
                toolBarTimeDateTitleHeartRate.setTextSize(18);
                toolBarTimeDateTitleBloodPressure.setTextSize(22);
                toolBarTimeDateTitleBloodSugar.setTextSize(18);
                toolBarTimeDate.setBackgroundResource(R.color.common_orange);
                tintManager.setStatusBarTintResource(R.color.common_orange);
                break;
            case 4:
                toolBarTimeDateTitleStepGauge.setTextSize(18);
                toolBarTimeDateTitleSleep.setTextSize(18);
                toolBarTimeDateTitleHeartRate.setTextSize(18);
                toolBarTimeDateTitleBloodPressure.setTextSize(18);
                toolBarTimeDateTitleBloodSugar.setTextSize(22);
                toolBarTimeDate.setBackgroundResource(R.color.common_yellow);
                tintManager.setStatusBarTintResource(R.color.common_yellow);
                break;
            default:
                break;
        }
    }

    /**
     * viewpager 的适配器
     */
    class PagerAdater extends FragmentPagerAdapter {


        public PagerAdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return pagerViewList.get(position);
        }

        @Override
        public int getCount() {
            return pagerViewList.size();
        }
    }
}
