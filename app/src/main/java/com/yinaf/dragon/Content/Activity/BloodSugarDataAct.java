package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Adapter.BloodSugarDataAdapter;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.Content.CustomUi.CustomLoadMoreListView;
import com.yinaf.dragon.Content.CustomUi.CustomRefreshAndLoadMoreView;
import com.yinaf.dragon.Content.Net.FindBloodSugarListAPI;
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
 * Created by long on 2018/04/27.
 * 功能：血糖数据页面
 */

public class BloodSugarDataAct extends BasicAct implements FindBloodSugarListAPI.FindBloodSugarListListener{

    @BindView(R.id.tool_bar_btn_back)
    RelativeLayout toolBarBtnBack;
    @BindView(R.id.tool_bar_title)
    TextView toolBarTitle;
    @BindView(R.id.blood_sugar_data_lv)
    CustomLoadMoreListView bloodSugarDataLv;
    @BindView(R.id.blood_sugar_data_refresh_and_load_more)
    CustomRefreshAndLoadMoreView bloodSugarDataRefreshAndLoadMore;

    BloodSugarDataAdapter bloodSugarDataAdapter;//血糖数据适配器
    List<TwoTextData> twoTextDataList = new ArrayList<TwoTextData>();//血糖数据列表
    private int pageIndex = 1;//当前页码
    private int totalPages = 1;//总页码
    public String memberId;

    public BloodSugarDataAct() {
        super(R.layout.act_blood_sugar_data, R.string.title_activity_blood_sugar_date, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_yellow);
    }

    public static void startActivity(Context context,String memberId) {
        Intent intent = new Intent(context, BloodSugarDataAct.class);
        intent.putExtra("memberId",memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        toolBarTitle.setText(R.string.title_activity_blood_sugar_date);
        memberId = getIntent().getStringExtra("memberId");
        initListView();

    }

    /**
     * 初始化计步数据列表
     */
    private void initListView() {

        loadData(pageIndex);
        bloodSugarDataAdapter = new BloodSugarDataAdapter(twoTextDataList);
        bloodSugarDataLv.setAdapter(bloodSugarDataAdapter);
        bloodSugarDataRefreshAndLoadMore.setLoadMoreListView(bloodSugarDataLv);
        bloodSugarDataLv.setRefreshAndLoadMoreView(bloodSugarDataRefreshAndLoadMore);
        //设置下拉刷新监听
        bloodSugarDataRefreshAndLoadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
        //设置加载监听
        bloodSugarDataLv.setOnLoadMoreListener(new CustomLoadMoreListView.OnLoadMoreListener() {
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
                    bloodSugarDataAdapter.notifyDataSetChanged();
                }
                pageIndex = tempPageIndex;
                new FindBloodSugarListAPI(BloodSugarDataAct.this,memberId,tempPageIndex);

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
     * 分页获取血糖集合
     * @param content
     */
    @Override
    public void findBloodSugarListSuccess(JSONObject content) {
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
                twoTextData.setText("血糖(mmol/L)");
                twoTextData.setType(0);
                twoTextDataList.add(twoTextData);
                for (int j = 0; j < obj.length(); j++) {
                    JSONObject jsonObject1 = obj.getJSONObject(j);
                    TwoTextData twoTextData1 = new TwoTextData();
                    twoTextData1.setText(JSONUtils.getString(jsonObject1,"gi"));
                    String time = JSONUtils.getString(jsonObject1,"time");
                    twoTextData1.setTime(time.substring(time.length() - 6));
                    if (j % 2 == 0) {
                        twoTextData1.setType(1);
                    } else {
                        twoTextData1.setType(2);
                    }
                    twoTextDataList.add(twoTextData1);
                }
            }
            bloodSugarDataAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            bloodSugarDataRefreshAndLoadMore.setRefreshing(false);
            bloodSugarDataLv.onLoadComplete();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                bloodSugarDataLv.setHaveMoreData(false);
            } else {
                bloodSugarDataLv.setHaveMoreData(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void findBloodSugarListError(long code, String msg) {
        TipUtils.showTip(msg);

    }
}
