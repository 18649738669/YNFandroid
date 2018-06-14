package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.yinaf.dragon.Content.Adapter.MedicalReportListAdapter;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.Content.CustomUi.CustomLoadMoreListView;
import com.yinaf.dragon.Content.CustomUi.CustomRefreshAndLoadMoreView;
import com.yinaf.dragon.Content.Net.FindDeviceReportListAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/05/17.
 * 功能：体检报告列表页面
 */

public class MedicalReportListAct extends BasicAct implements FindDeviceReportListAPI.FindDeviceReportListListener{


    @BindView(R.id.medical_report_lv)
    CustomLoadMoreListView medicalReportLv;
    @BindView(R.id.medical_report_refresh_and_load_more)
    CustomRefreshAndLoadMoreView medicalReportRefreshAndLoadMore;

    MedicalReportListAdapter medicalReportListAdapter;//体检报告列表适配器
    List<TwoTextData> twoTextDataList = new ArrayList<TwoTextData>();//体检报告列表
    private int pageIndex = 1;//当前页码
    private int totalPages = 1;//总页码
    public String memberId;

    public MedicalReportListAct() {
        super(R.layout.act_medical_report_list, R.string.health_records_ll_medical_report, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);

    }

    public static void startActivity(Context context, String memberId) {
        Intent intent = new Intent(context, MedicalReportListAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        memberId = getIntent().getStringExtra("memberId");
        initListView();
    }

    /**
     * 初始化体检报告数据列表
     */
    private void initListView() {

        loadData(pageIndex);
        medicalReportListAdapter = new MedicalReportListAdapter(this,twoTextDataList);
        medicalReportLv.setAdapter(medicalReportListAdapter);
        medicalReportLv.setRefreshAndLoadMoreView(medicalReportRefreshAndLoadMore);
        medicalReportRefreshAndLoadMore.setLoadMoreListView(medicalReportLv);
        //设置下拉刷新监听
        medicalReportRefreshAndLoadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
        //设置加载监听
        medicalReportLv.setOnLoadMoreListener(new CustomLoadMoreListView.OnLoadMoreListener() {
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
                    medicalReportListAdapter.notifyDataSetChanged();
                }
                pageIndex = tempPageIndex;
                new FindDeviceReportListAPI(MedicalReportListAct.this,memberId,tempPageIndex);

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
     * 分页获取体检报告列表
     * @param content
     */
    @Override
    public void findDeviceReportListSuccess(JSONObject content) {

        JSONObject pager = JSONUtils.getJSONObject(content,"pager");
        pageIndex = JSONUtils.getInt(pager,"currentPage");
        totalPages = JSONUtils.getInt(pager,"totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content,"rows");
        try {
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jsonObject = rows.getJSONObject(i);
                TwoTextData twoTextData1 = new TwoTextData();
                twoTextData1.setText(JSONUtils.getString(jsonObject,"reportNo"));
                twoTextData1.setTime(JSONUtils.getString(jsonObject,"time"));
                twoTextData1.setType(JSONUtils.getInt(jsonObject,"drId"));
                twoTextDataList.add(twoTextData1);
            }
            medicalReportListAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            medicalReportRefreshAndLoadMore.setRefreshing(false);
            medicalReportLv.onLoadComplete();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                medicalReportLv.setHaveMoreData(false);
            } else {
                medicalReportLv.setHaveMoreData(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findDeviceReportListError(long code, String msg) {
        TipUtils.showTip(msg);
    }
}
