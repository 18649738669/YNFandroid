package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.yinaf.dragon.Content.Adapter.StepGuageDataAdapter;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.Content.CustomUi.CustomLoadMoreListView;
import com.yinaf.dragon.Content.CustomUi.CustomRefreshAndLoadMoreView;
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

/**
 * Created by long on 2018/04/26.
 * 功能：计步数据页面
 */

public class StepGaugeDataAct extends BasicAct implements FindStepListAPI.FindStepListListener{

    @BindView(R.id.step_gauge_data_lv)
    CustomLoadMoreListView stepGaugeDataLv;
    @BindView(R.id.step_gauge_data_refresh_and_load_more)
    CustomRefreshAndLoadMoreView stepGaugeDataRefreshAndLoadMore;

    StepGuageDataAdapter stepGuageDataAdapter;//计步数据适配器
    List<TwoTextData> twoTextDataList = new ArrayList<TwoTextData>();//计步数据列表
    private int pageIndex = 1;//当前页码
    private int totalPages = 1;//总页码
    public String memberId;

    public StepGaugeDataAct() {
        super(R.layout.act_step_gauge_data, R.string.title_activity_step_gauge_date, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context,String memberId) {
        Intent intent = new Intent(context, StepGaugeDataAct.class);
        intent.putExtra("memberId",memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        memberId = getIntent().getStringExtra("memberId");
        initListView();

    }

    /**
     * 初始化计步数据列表
     */
    private void initListView() {

        loadData(pageIndex);
        stepGuageDataAdapter = new StepGuageDataAdapter(twoTextDataList);
        stepGaugeDataLv.setAdapter(stepGuageDataAdapter);
        stepGaugeDataLv.setRefreshAndLoadMoreView(stepGaugeDataRefreshAndLoadMore);
        stepGaugeDataRefreshAndLoadMore.setLoadMoreListView(stepGaugeDataLv);
        //设置下拉刷新监听
        stepGaugeDataRefreshAndLoadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
        //设置加载监听
        stepGaugeDataLv.setOnLoadMoreListener(new CustomLoadMoreListView.OnLoadMoreListener() {
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
                    twoTextDataList.clear();
                    stepGuageDataAdapter.notifyDataSetChanged();
                }
                pageIndex = tempPageIndex;
                new FindStepListAPI(StepGaugeDataAct.this,memberId,tempPageIndex);
            }
        }, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 分页获取计步数据集合
     * @param content
     */
    @Override
    public void findStepListSuccess(JSONObject content) {

        JSONObject pager = JSONUtils.getJSONObject(content,"pager");
        pageIndex = JSONUtils.getInt(pager,"currentPage");
        totalPages = JSONUtils.getInt(pager,"totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content,"rows");
        try {
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jsonObject = rows.getJSONObject(i);
                JSONArray obj = JSONUtils.getJSONArray(jsonObject,"obj");
                TwoTextData twoTextData = new TwoTextData();
                twoTextData.setTime(jsonObject.getString("date"));
                twoTextData.setText("步数");
                twoTextData.setType(0);
                twoTextDataList.add(twoTextData);
                for (int j = 0; j < obj.length(); j++) {
                    JSONObject jsonObject1 = obj.getJSONObject(j);
                    TwoTextData twoTextData1 = new TwoTextData();
                    twoTextData1.setText(JSONUtils.getString(jsonObject1,"step"));
                    twoTextData1.setTime(JSONUtils.getString(jsonObject1,"time"));
                    if (j % 2 == 0) {
                        twoTextData1.setType(1);
                    } else {
                        twoTextData1.setType(2);
                    }
                    twoTextDataList.add(twoTextData1);
                }
            }
            stepGuageDataAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            stepGaugeDataRefreshAndLoadMore.setRefreshing(false);
            stepGaugeDataLv.onLoadComplete();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                stepGaugeDataLv.setHaveMoreData(false);
            } else {
                stepGaugeDataLv.setHaveMoreData(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void findStepListError(long code, String msg) {

        TipUtils.showTip(msg);

    }
}
