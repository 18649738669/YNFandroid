package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.yinaf.dragon.Content.Adapter.AnAlarmAdapter;
import com.yinaf.dragon.Content.Bean.AnAlarm;
import com.yinaf.dragon.Content.Bean.ThreeTextData;
import com.yinaf.dragon.Content.CustomUi.CustomLoadMoreListView;
import com.yinaf.dragon.Content.CustomUi.CustomRefreshAndLoadMoreView;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.FindAllAlertListAPI;
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
 * Created by long on 2018/05/08.
 * 功能:警报提醒页面
 */

public class AnAlarmAct extends BasicAct implements FindAllAlertListAPI.FindAllAlertListListener{


    @BindView(R.id.an_alarm_lv)
    CustomLoadMoreListView anAlarmLv;
    @BindView(R.id.an_alarm_refresh_and_load_more)
    CustomRefreshAndLoadMoreView anAlarmRefreshAndLoadMore;

    AnAlarmAdapter anAlarmAdapter;//健康报警适配器
    List<AnAlarm> anAlarmArrayList = new ArrayList<AnAlarm>();//健康报警列表

    private int pageIndex = 1;//当前页码
    private int totalPages = 1;//总页码
    public String memberId;

    public AnAlarmAct() {
        super(R.layout.act_an_alarm, R.string.title_activity_an_alarm, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId) {
        Intent intent = new Intent(context, AnAlarmAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        memberId = getIntent().getStringExtra("memberId");
        initListView();
    }

    /**
     * 初始化睡眠数据列表
     */
    private void initListView() {

        loadData(pageIndex);
        anAlarmAdapter = new AnAlarmAdapter(anAlarmArrayList);
        anAlarmLv.setAdapter(anAlarmAdapter);
        anAlarmLv.setRefreshAndLoadMoreView(anAlarmRefreshAndLoadMore);
        anAlarmRefreshAndLoadMore.setLoadMoreListView(anAlarmLv);
        //设置下拉刷新监听
        anAlarmRefreshAndLoadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(1);
            }
        });
        //设置加载监听
        anAlarmLv.setOnLoadMoreListener(new CustomLoadMoreListView.OnLoadMoreListener() {
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
                    anAlarmArrayList.clear();
                    anAlarmAdapter.notifyDataSetChanged();
                }
                pageIndex = tempPageIndex;
                new FindAllAlertListAPI(AnAlarmAct.this,memberId,tempPageIndex);

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
     * 分页获取健康警报列表
     * @param content
     */
    @Override
    public void findAllAlertListSuccess(JSONObject content) {
        JSONObject pager = JSONUtils.getJSONObject(content,"pager");
        pageIndex = JSONUtils.getInt(pager,"currentPage");
        totalPages = JSONUtils.getInt(pager,"totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content,"rows");
        try {
            for (int i = 0; i < rows.length(); i++) {
                AnAlarm anAlarm = new AnAlarm();
                JSONObject jsonObject = rows.getJSONObject(i);
                anAlarm.setTime(JSONUtils.getString(jsonObject,"time"));
                anAlarm.setTitle(JSONUtils.getString(jsonObject,"title"));
                anAlarm.setName(JSONUtils.getString(jsonObject,"name"));
                anAlarm.setImage(R.drawable.fb_msg_tip);
                anAlarmArrayList.add(anAlarm);
            }
            anAlarmAdapter.notifyDataSetChanged();
            //当加载完成之后设置此时不在刷新状态
            anAlarmRefreshAndLoadMore.setRefreshing(false);
            anAlarmLv.onLoadComplete();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex >= totalPages) {
                anAlarmLv.setHaveMoreData(false);
            } else {
                anAlarmLv.setHaveMoreData(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findAllAlertListError(long code, String msg) {

        TipUtils.showTip(msg);
    }
}
