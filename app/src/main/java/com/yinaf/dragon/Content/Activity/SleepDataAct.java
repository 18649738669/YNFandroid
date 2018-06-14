package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Adapter.SleepDataAdapter;
import com.yinaf.dragon.Content.Bean.ThreeTextData;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.Content.CustomUi.CustomLoadMoreListView;
import com.yinaf.dragon.Content.CustomUi.CustomRefreshAndLoadMoreView;
import com.yinaf.dragon.Content.Net.FindSleepListAPI;
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
 * 功能：睡眠数据页面
 */

public class SleepDataAct extends BasicAct implements FindSleepListAPI.FindSleepListListener {

    @BindView(R.id.sleep_data_lv)
    CustomLoadMoreListView sleepDataLv;
    @BindView(R.id.sleep_data_refresh_and_load_more)
    CustomRefreshAndLoadMoreView sleepDataRefreshAndLoadMore;
    @BindView(R.id.tool_bar_btn_back)
    RelativeLayout toolBarBtnBack;
    @BindView(R.id.tool_bar_title)
    TextView toolBarTitle;


    SleepDataAdapter sleepDataAdapter;//睡眠数据适配器
    List<ThreeTextData> threeTextDataList = new ArrayList<ThreeTextData>();//睡眠数据列表

    private int pageIndex = 1;//当前页码
    private int totalPages = 1;//总页码
    public String memberId;

    public SleepDataAct() {
        super(R.layout.act_sleep_data, R.string.title_activity_sleep_date, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_purle);
    }

    public static void startActivity(Context context,String memberId) {
        Intent intent = new Intent(context, SleepDataAct.class);
        intent.putExtra("memberId",memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        toolBarTitle.setText(R.string.title_activity_sleep_date);
        memberId = getIntent().getStringExtra("memberId");
        initListView();

    }

    /**
     * 初始化睡眠数据列表
     */
    private void initListView() {

        loadData(pageIndex);
        sleepDataAdapter = new SleepDataAdapter(threeTextDataList);
        sleepDataLv.setAdapter(sleepDataAdapter);
        sleepDataLv.setRefreshAndLoadMoreView(sleepDataRefreshAndLoadMore);
        sleepDataRefreshAndLoadMore.setLoadMoreListView(sleepDataLv);
        //设置下拉刷新监听
        sleepDataRefreshAndLoadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
        //设置加载监听
        sleepDataLv.setOnLoadMoreListener(new CustomLoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData(pageIndex + 1);
            }
        });

    }

    /**
     * 加载数据
     */
    private void loadData(final int tempPageIndex) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tempPageIndex == 1) {
                    threeTextDataList.clear();
                    sleepDataAdapter.notifyDataSetChanged();
                }
                pageIndex = tempPageIndex;
                new FindSleepListAPI(SleepDataAct.this,memberId,tempPageIndex);

            }
        }, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tool_bar_btn_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 分页获取计睡眠时间据集合
     * @param content
     */
    @Override
    public void findSleepListSuccess(JSONObject content) {


        JSONObject pager = JSONUtils.getJSONObject(content,"pager");
        pageIndex = JSONUtils.getInt(pager,"currentPage");
        totalPages = JSONUtils.getInt(pager,"totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content,"rows");
        ThreeTextData threeTextData1 = new ThreeTextData();
        threeTextData1.setTime("时间");
        threeTextData1.setContent("深度睡眠");
        threeTextData1.setContent1("浅度睡眠");
        threeTextData1.setType(0);
        threeTextDataList.add(threeTextData1);
        try {
            for (int i = 0; i < rows.length(); i++) {
                ThreeTextData threeTextData = new ThreeTextData();
                JSONObject jsonObject = rows.getJSONObject(i);
                threeTextData.setTime(JSONUtils.getString(jsonObject,"time"));
                threeTextData.setContent(JSONUtils.getString(jsonObject,"heartDuration"));
                threeTextData.setContent1(JSONUtils.getString(jsonObject,"quietDuration"));
                if (i % 2 == 0 ){
                    threeTextData.setType(1);
                }else {
                    threeTextData.setType(2);
                }
                threeTextDataList.add(threeTextData);
            }
            sleepDataAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            sleepDataRefreshAndLoadMore.setRefreshing(false);
            sleepDataLv.onLoadComplete();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                sleepDataLv.setHaveMoreData(false);
            } else {
                sleepDataLv.setHaveMoreData(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findSleepListError(long code, String msg) {
        TipUtils.showTip(msg);
    }
}
