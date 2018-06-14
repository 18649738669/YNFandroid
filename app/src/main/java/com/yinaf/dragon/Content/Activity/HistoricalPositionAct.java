package com.yinaf.dragon.Content.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.yinaf.dragon.Content.Adapter.HistoricalPositionAdapter;
import com.yinaf.dragon.Content.Adapter.StepGuageDataAdapter;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetLocusCountAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/03.
 * 功能：历史定位页面
 */

public class HistoricalPositionAct extends BasicAct implements GetLocusCountAPI.GetLocusCountListener{


    @BindView(R.id.historical_position_tv_time)
    TextView historicalPositionTvTime;
    @BindView(R.id.historical_position_lv)
    ListView historicalPositionLv;
    @BindView(R.id.prompt)
    TextView prompt;

    HistoricalPositionAdapter historicalPositionAdapter;//计步数据适配器
    List<TwoTextData> twoTextDataList = new ArrayList<TwoTextData>();//计步数据列表
    public String memberId;
    //时间选择器
    TimePickerView pvTime;
    LoadingDialog loadingDialog;

    public HistoricalPositionAct() {
        super(R.layout.act_historical_position, R.string.title_activity_historical_position, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context,String memberId) {
        Intent intent = new Intent(context, HistoricalPositionAct.class);
        intent.putExtra("memberId",memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        memberId = getIntent().getStringExtra("memberId");
        loadingDialog = LoadingDialog.showDialog(this);
        historicalPositionTvTime.setText(StringUtils.getYearMonth(StringUtils.getCurrentTimeStamp()));
        initPickerView();
        initListView();
        GetLocusCount();

    }

    private void initListView() {
        historicalPositionAdapter = new HistoricalPositionAdapter(twoTextDataList);
        historicalPositionLv.setAdapter(historicalPositionAdapter);
        historicalPositionLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String day = twoTextDataList.get(position).getText();
                NextActivity(day);
            }
        });
    }

    public void NextActivity(String day){
        HistoricalMapAct.startActivity(this,memberId,day);
    }

    public void GetLocusCount(){
        loadingDialog.show();
        new GetLocusCountAPI(this,historicalPositionTvTime.getText().toString(),memberId);
    }

    /**
     * 时间选择器控件
     */
    public void initPickerView(){
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
                historicalPositionTvTime.setText(sf.format(date));
                GetLocusCount();
            }
        })
                .setType(new boolean[]{true,true,false,false,false,false})
                .setLabel("","","","","","")//默认设置为年月日
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.historical_position_tv_time)
    public void onViewClicked() {
        pvTime.show();
    }

    /**
     * 成员历史定位统计
     * @param content
     */
    @Override
    public void getLocusCountSuccess(JSONArray content) {

        if (content.length() > 0){
            historicalPositionLv.setVisibility(View.VISIBLE);
            prompt.setVisibility(View.GONE);
            twoTextDataList.clear();
            for (int i = 0; i < content.length(); i++){
                try {
                    JSONObject jsonObject = content.getJSONObject(i);
                    TwoTextData twoTextData = new TwoTextData();
                    String str = JSONUtils.getString(jsonObject,"date");
                    twoTextData.setTime(str.substring(5,str.length()));
                    twoTextData.setText(JSONUtils.getString(jsonObject,"day"));
                    twoTextData.setType(JSONUtils.getInt(jsonObject,"count"));
                    twoTextDataList.add(twoTextData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            historicalPositionAdapter.notifyDataSetChanged();
        }else {
            historicalPositionLv.setVisibility(View.GONE);
            prompt.setVisibility(View.VISIBLE);
            twoTextDataList.clear();
        }
        loadingDialog.dismiss();

    }

    @Override
    public void getLocusCountError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
