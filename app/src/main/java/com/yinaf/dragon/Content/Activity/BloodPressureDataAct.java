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

import com.yinaf.dragon.Content.Adapter.BloodPressureDataAdapter;
import com.yinaf.dragon.Content.Bean.ThreeTextData;
import com.yinaf.dragon.Content.CustomUi.CustomLoadMoreListView;
import com.yinaf.dragon.Content.CustomUi.CustomRefreshAndLoadMoreView;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.FindAlertBloodPressureListAPI;
import com.yinaf.dragon.Content.Net.FindBloodPressureListAPI;
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
 * 功能：血压数据页面
 */

public class BloodPressureDataAct extends BasicAct implements FindAlertBloodPressureListAPI.FindAlertBloodPressureListListener,
        FindBloodPressureListAPI.FindBloodPressureListListener{


    @BindView(R.id.tool_bar_btn_back)
    RelativeLayout toolBarBtnBack;
    @BindView(R.id.tool_bar_title)
    TextView toolBarTitle;
    @BindView(R.id.blood_pressure_data_tv_all_data)
    TextView bloodPressureDataTvAllData;
    @BindView(R.id.blood_pressure_data_tv_error_data)
    TextView bloodPressureDataTvErrorData;
    @BindView(R.id.blood_pressure_data_lv)
    CustomLoadMoreListView bloodPressureDataLv;
    @BindView(R.id.blood_pressure_data_refresh_and_load_more)
    CustomRefreshAndLoadMoreView bloodPressureDataRefreshAndLoadMore;


    BloodPressureDataAdapter bloodPressureDataAdapter;//心率数据适配器
    List<ThreeTextData> threeTextDataList = new ArrayList<ThreeTextData>();//心率数据列表

    LoadingDialog loadingDialog;
    String memberId;
    private int pageIndex = 1;//当前页码
    private int totalPages = 1;//总页码
    private int index = 0;//总页码

    public BloodPressureDataAct() {
        super(R.layout.act_blood_pressure_data, R.string.title_activity_blood_pressure_date, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_orange);
    }

    public static void startActivity(Context context, String memberId) {
        Intent intent = new Intent(context, BloodPressureDataAct.class);
        intent.putExtra("memberId",memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        toolBarTitle.setText(R.string.title_activity_blood_pressure_date);
        initListView();

    }

    /**
     * 初始化血压数据列表
     */
    private void initListView() {

        loadData(pageIndex,index);
        bloodPressureDataAdapter = new BloodPressureDataAdapter(threeTextDataList);
        bloodPressureDataLv.setAdapter(bloodPressureDataAdapter);
        bloodPressureDataLv.setRefreshAndLoadMoreView(bloodPressureDataRefreshAndLoadMore);
        bloodPressureDataRefreshAndLoadMore.setLoadMoreListView(bloodPressureDataLv);
        //设置下拉刷新监听
        bloodPressureDataRefreshAndLoadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1,index);
            }
        });
        //设置加载监听
        bloodPressureDataLv.setOnLoadMoreListener(new CustomLoadMoreListView.OnLoadMoreListener() {
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
                    threeTextDataList.clear();
                    bloodPressureDataAdapter.notifyDataSetChanged();
                }
                pageIndex = tempPageIndex;
                if (index == 0){
                    new FindBloodPressureListAPI(BloodPressureDataAct.this,memberId,tempPageIndex);
                }else {
                    new FindAlertBloodPressureListAPI(BloodPressureDataAct.this,memberId,tempPageIndex);
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
    @OnClick({R.id.tool_bar_btn_back, R.id.blood_pressure_data_tv_all_data, R.id.blood_pressure_data_tv_error_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_btn_back:
                finish();
                break;
            case R.id.blood_pressure_data_tv_all_data:
                bloodPressureDataTvAllData.setBackgroundResource(R.drawable.tv_background_style_8);
                bloodPressureDataTvErrorData.setBackgroundResource(R.drawable.tv_background_style_7);
                bloodPressureDataTvAllData.setTextColor(R.color.common_white);
                bloodPressureDataTvErrorData.setTextColor(R.color.common_orange);
                threeTextDataList.clear();
                bloodPressureDataAdapter.notifyDataSetChanged();
                index = 0;
                pageIndex = 1;
                loadData(pageIndex,index);
                break;
            case R.id.blood_pressure_data_tv_error_data:
                bloodPressureDataTvAllData.setBackgroundResource(R.drawable.tv_background_style_7);
                bloodPressureDataTvErrorData.setBackgroundResource(R.drawable.tv_background_style_8);
                bloodPressureDataTvAllData.setTextColor(R.color.common_orange);
                bloodPressureDataTvErrorData.setTextColor(R.color.common_white);
                threeTextDataList.clear();
                bloodPressureDataAdapter.notifyDataSetChanged();
                index = 1;
                pageIndex = 1;
                loadData(pageIndex,index);
                break;
        }
    }

    /**
     * 分页获取血压集合
     * @param content
     */
    @Override
    public void findBloodPressureListSuccess(JSONObject content) {

        JSONObject pager = JSONUtils.getJSONObject(content, "pager");
        pageIndex = JSONUtils.getInt(pager, "currentPage");
        totalPages = JSONUtils.getInt(pager, "totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content, "rows");
        try {
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jsonObject = rows.getJSONObject(i);
                JSONArray obj = JSONUtils.getJSONArray(jsonObject, "obj");
                ThreeTextData threeTextData = new ThreeTextData();
                threeTextData.setTime(jsonObject.getString("date"));
                threeTextData.setContent("舒张压");
                threeTextData.setContent1("收缩压");
                threeTextData.setType(0);
                threeTextDataList.add(threeTextData);
                for (int j = 0; j < obj.length(); j++) {
                    JSONObject jsonObject1 = obj.getJSONObject(j);
                    ThreeTextData threeTextData1 = new ThreeTextData();
                    threeTextData1.setContent(JSONUtils.getString(jsonObject1, "dbp"));
                    threeTextData1.setContent1(JSONUtils.getString(jsonObject1, "sbp"));
                    threeTextData1.setTime(JSONUtils.getString(jsonObject1, "time"));
                    if (j % 2 == 0) {
                        threeTextData1.setType(1);
                    } else {
                        threeTextData1.setType(2);
                    }
                    threeTextDataList.add(threeTextData1);
                }
            }
            bloodPressureDataAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            bloodPressureDataRefreshAndLoadMore.setRefreshing(false);
            bloodPressureDataLv.onLoadComplete();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                bloodPressureDataLv.setHaveMoreData(false);
            } else {
                bloodPressureDataLv.setHaveMoreData(true);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void findBloodPressureListError(long code, String msg) {
        TipUtils.showTip(msg);
    }

    /**
     * 分页获取异常血压集合
     * @param content
     */
    @Override
    public void findAlertBloodPressureListSuccess(JSONObject content) {
        JSONObject pager = JSONUtils.getJSONObject(content, "pager");
        pageIndex = JSONUtils.getInt(pager, "currentPage");
        totalPages = JSONUtils.getInt(pager, "totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content, "rows");
        try {
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jsonObject = rows.getJSONObject(i);
                JSONArray obj = JSONUtils.getJSONArray(jsonObject, "obj");
                ThreeTextData threeTextData = new ThreeTextData();
                threeTextData.setTime(jsonObject.getString("date"));
                threeTextData.setContent("舒张压");
                threeTextData.setContent1("收缩压");
                threeTextData.setType(0);
                threeTextDataList.add(threeTextData);
                for (int j = 0; j < obj.length(); j++) {
                    JSONObject jsonObject1 = obj.getJSONObject(j);
                    ThreeTextData threeTextData1 = new ThreeTextData();
                    threeTextData1.setContent(JSONUtils.getString(jsonObject1, "dbp"));
                    threeTextData1.setContent1(JSONUtils.getString(jsonObject1, "sbp"));
                    threeTextData1.setTime(JSONUtils.getString(jsonObject1, "time"));
                    if (j % 2 == 0) {
                        threeTextData1.setType(1);
                    } else {
                        threeTextData1.setType(2);
                    }
                    threeTextDataList.add(threeTextData1);
                }
            }
            bloodPressureDataAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            bloodPressureDataRefreshAndLoadMore.setRefreshing(false);
            bloodPressureDataLv.onLoadComplete();

            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                bloodPressureDataLv.setHaveMoreData(false);
            } else {
                bloodPressureDataLv.setHaveMoreData(true);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void findAlertBloodPressureListError(long code, String msg) {
        TipUtils.showTip(msg);
    }


}
