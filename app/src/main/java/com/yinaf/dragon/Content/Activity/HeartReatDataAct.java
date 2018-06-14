package com.yinaf.dragon.Content.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Adapter.HeartRetaDataAdapter;
import com.yinaf.dragon.Content.Adapter.StepGuageDataAdapter;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.Content.CustomUi.CustomLoadMoreListView;
import com.yinaf.dragon.Content.CustomUi.CustomRefreshAndLoadMoreView;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.FindAlertHeartRateListAPI;
import com.yinaf.dragon.Content.Net.FindHeartRateListAPI;
import com.yinaf.dragon.Content.Net.FindStepListAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/04/26.
 * 功能：心率数据页面
 */

public class HeartReatDataAct extends BasicAct implements FindHeartRateListAPI.FindHeartRateListListener,
        FindAlertHeartRateListAPI.FindAlertHeartRateListListener{
    @BindView(R.id.tool_bar_btn_back)
    RelativeLayout toolBarBtnBack;
    @BindView(R.id.tool_bar_title)
    TextView toolBarTitle;
    @BindView(R.id.heart_reta_data_tv_all_data)
    TextView heartRetaDataTvAllData;
    @BindView(R.id.heart_reta_data_tv_error_data)
    TextView heartRetaDataTvErrorData;
    @BindView(R.id.heart_reta_data_lv)
    CustomLoadMoreListView heartRetaDataLv;
    @BindView(R.id.heart_reta_data_refresh_and_load_more)
    CustomRefreshAndLoadMoreView heartRetaDataRefreshAndLoadMore;

    HeartRetaDataAdapter heartRetaDataAdapter;//心率数据适配器
    List<TwoTextData> twoTextDataList = new ArrayList<TwoTextData>();//心率数据列表

    LoadingDialog loadingDialog;
    String memberId;
    private int pageIndex = 1;//当前页码
    private int totalPages = 1;//总页码
    private int index = 0;//异常数据还是全部数据（0全部1异常）

    public HeartReatDataAct() {
        super(R.layout.act_heart_reta_data, R.string.title_activity_heart_reta_date, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_pink);
    }

    public static void startActivity(Context context,String memberId) {
        Intent intent = new Intent(context, HeartReatDataAct.class);
        intent.putExtra("memberId",memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        toolBarTitle.setText(R.string.title_activity_heart_reta_date);

        memberId = getIntent().getStringExtra("memberId");
        initListView();

    }

    /**
     * 初始化心率数据列表
     */
    private void initListView() {

        loadData(pageIndex,index);
        heartRetaDataAdapter = new HeartRetaDataAdapter(twoTextDataList);
        heartRetaDataLv.setAdapter(heartRetaDataAdapter);
        heartRetaDataLv.setRefreshAndLoadMoreView(heartRetaDataRefreshAndLoadMore);
        heartRetaDataRefreshAndLoadMore.setLoadMoreListView(heartRetaDataLv);
        //设置下拉刷新监听
        heartRetaDataRefreshAndLoadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1,index);
            }
        });
        //设置加载监听
        heartRetaDataLv.setOnLoadMoreListener(new CustomLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(pageIndex + 1,index);
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData(final int tempPageIndex, final int index) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tempPageIndex == 1) {
                    twoTextDataList.clear();
                    heartRetaDataAdapter.notifyDataSetChanged();

                }
                pageIndex = tempPageIndex;
                if (index == 0){
                    new FindHeartRateListAPI(HeartReatDataAct.this,memberId,tempPageIndex);
                }else {
                    new FindAlertHeartRateListAPI(HeartReatDataAct.this,memberId,tempPageIndex);
                }
            }
        }, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.tool_bar_btn_back, R.id.heart_reta_data_tv_all_data, R.id.heart_reta_data_tv_error_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_btn_back:
                finish();
                break;
            case R.id.heart_reta_data_tv_all_data:
                heartRetaDataTvAllData.setBackgroundResource(R.drawable.tv_background_style_6);
                heartRetaDataTvErrorData.setBackgroundResource(R.drawable.tv_background_style_5);
                heartRetaDataTvAllData.setTextColor(R.color.common_white);
                heartRetaDataTvErrorData.setTextColor(R.color.common_pink);
                twoTextDataList.clear();
                heartRetaDataAdapter.notifyDataSetChanged();
                index = 0;
                pageIndex = 1;
                loadData(pageIndex,index);
                break;
            case R.id.heart_reta_data_tv_error_data:
                heartRetaDataTvAllData.setBackgroundResource(R.drawable.tv_background_style_5);
                heartRetaDataTvErrorData.setBackgroundResource(R.drawable.tv_background_style_6);
                heartRetaDataTvAllData.setTextColor(R.color.common_pink);
                heartRetaDataTvErrorData.setTextColor(R.color.common_white);
                twoTextDataList.clear();
                heartRetaDataAdapter.notifyDataSetChanged();
                index = 1;
                pageIndex = 1;
                loadData(pageIndex,index);

                break;
        }
    }

    /**
     * 分页获取心率数据集合
     * @param content
     */
    @Override
    public void findHeartRateListSuccess(JSONObject content) {

        JSONObject pager = JSONUtils.getJSONObject(content, "pager");
        pageIndex = JSONUtils.getInt(pager, "currentPage");
        totalPages = JSONUtils.getInt(pager, "totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content, "rows");
        try {
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jsonObject = rows.getJSONObject(i);
                JSONArray obj = JSONUtils.getJSONArray(jsonObject, "obj");
                TwoTextData twoTextData = new TwoTextData();
                twoTextData.setTime(jsonObject.getString("date"));
                twoTextData.setText("心率(bpm)");
                twoTextData.setType(0);
                twoTextDataList.add(twoTextData);
                for (int j = 0; j < obj.length(); j++) {
                    JSONObject jsonObject1 = obj.getJSONObject(j);
                    TwoTextData twoTextData1 = new TwoTextData();
                    twoTextData1.setText(JSONUtils.getString(jsonObject1, "bpm"));
                    twoTextData1.setTime(JSONUtils.getString(jsonObject1, "time"));
                    if (j % 2 == 0) {
                        twoTextData1.setType(1);
                    } else {
                        twoTextData1.setType(2);
                    }
                    twoTextDataList.add(twoTextData1);
                }
            }
            heartRetaDataAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            heartRetaDataRefreshAndLoadMore.setRefreshing(false);
            heartRetaDataLv.onLoadComplete();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                heartRetaDataLv.setHaveMoreData(false);
            } else {
                heartRetaDataLv.setHaveMoreData(true);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findHeartRateListError(long code, String msg) {

        TipUtils.showTip(msg);

    }

    /**
     * 分页获取异常心率数据集合
     * @param content
     */
    @Override
    public void findAlertHeartRateListSuccess(JSONObject content) {
        JSONObject pager = JSONUtils.getJSONObject(content, "pager");
        pageIndex = JSONUtils.getInt(pager, "currentPage");
        totalPages = JSONUtils.getInt(pager, "totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content, "rows");
        try {
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jsonObject = rows.getJSONObject(i);
                JSONArray obj = JSONUtils.getJSONArray(jsonObject, "obj");
                TwoTextData twoTextData = new TwoTextData();
                twoTextData.setTime(jsonObject.getString("date"));
                twoTextData.setText("心率(bpm)");
                twoTextData.setType(0);
                twoTextDataList.add(twoTextData);
                for (int j = 0; j < obj.length(); j++) {
                    JSONObject jsonObject1 = obj.getJSONObject(j);
                    TwoTextData twoTextData1 = new TwoTextData();
                    twoTextData1.setText(JSONUtils.getString(jsonObject1, "bpm"));
                    twoTextData1.setTime(JSONUtils.getString(jsonObject1, "time"));
                    if (j % 2 == 0) {
                        twoTextData1.setType(1);
                    } else {
                        twoTextData1.setType(2);
                    }
                    twoTextDataList.add(twoTextData1);
                }
            }
            heartRetaDataAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            heartRetaDataRefreshAndLoadMore.setRefreshing(false);
            heartRetaDataLv.onLoadComplete();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                heartRetaDataLv.setHaveMoreData(false);
            } else {
                heartRetaDataLv.setHaveMoreData(true);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findAlertHeartRateListError(long code, String msg) {

        TipUtils.showTip(msg);
    }
}
