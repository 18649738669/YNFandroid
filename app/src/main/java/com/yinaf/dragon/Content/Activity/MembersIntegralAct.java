package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amap.api.services.help.Tip;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.yinaf.dragon.Content.Adapter.MembersIntegralAdapter;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Bean.MembersIntegralLog;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.FindIntegralLogListAPI;
import com.yinaf.dragon.Content.Net.GetMemberIntegralAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
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
 * Created by long on 2018/05/09.
 * 功能：成员积分页面
 */

public class MembersIntegralAct extends BasicAct implements GetMemberIntegralAPI.GetMemberIntegralListener,
        FindIntegralLogListAPI.FindIntegralLogListListener{


    @BindView(R.id.members_integral_rl)
    RefreshLayout membersIntegralRl;

    MembersIntegralAdapter membersIntegralAdapter;//睡眠数据适配器
    List<MembersIntegralLog> membersIntegralLogList = new ArrayList<MembersIntegralLog>();//睡眠数据列表

    private int pageIndex = 1;//当前页码
    private int totalPages = 0;//总页码
    public String memberId;
    public int integralNum = -1;//成员总积分
    public String roundedImg;//成员头像
    public String memberName;//成员名称
    public boolean isLoad = true;//是否允许上拉加载
    public DatabaseHelper dbHelper;
    Handler handler = new Handler();
    LoadingDialog loadingDialog;

    public MembersIntegralAct() {
        super(R.layout.act_members_integral, R.string.family_tv_members_integral, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId) {
        Intent intent = new Intent(context, MembersIntegralAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        memberId = getIntent().getStringExtra("memberId");
        dbHelper = new DatabaseHelper(this, SPHelper.getString(Builds.SP_USER, "userName"));
        Member member = dbHelper.selectMemberByMemberId(Integer.parseInt(memberId));
        roundedImg = member.getImage();
        memberName = member.getRealName();
        loadingDialog.show();
        new GetMemberIntegralAPI(MembersIntegralAct.this,memberId);
        initListView();
    }

    /**
     * 初始化成员积分列表
     */
    private void initListView() {

        loadData(pageIndex);
        membersIntegralAdapter = new MembersIntegralAdapter(this,membersIntegralLogList);
        membersIntegralRl.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GetMemberIntegralAPI(MembersIntegralAct.this,memberId);
                        loadData(1);
                        membersIntegralRl.NotifyCompleteRefresh0();

                    }
                }, 500);
            }

            @Override
            public void Loading() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLoad) {
                            loadData(pageIndex + 1);
                            membersIntegralRl.NotifyCompleteRefresh0();
                        }else {
                            TipUtils.showTip("没有更多数据");
                            membersIntegralRl.NotifyCompleteRefresh0();

                        }
                    }
                }, 500);
            }
        });
        RecyclerView recyclerView = membersIntegralRl.getmScroll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(membersIntegralAdapter);
    }

    /**
     * 加载数据
     */
    private void loadData(final int tempPageIndex) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tempPageIndex == 1) {
                    membersIntegralLogList.clear();
                }
                pageIndex = tempPageIndex;
                new FindIntegralLogListAPI(MembersIntegralAct.this,memberId,tempPageIndex);

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
     * 获取成员总积分接口
     * @param content
     */
    @Override
    public void getMemberIntegralSuccess(JSONObject content) {
        integralNum = JSONUtils.getInt(content,"integralNum");
//        loadingDialog.dismiss();
    }

    @Override
    public void getMemberIntegralError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 积分明细(日志)列表
     * @param content
     */
    @Override
    public void findIntegralLogListSuccess(JSONObject content) {
        JSONObject pager = JSONUtils.getJSONObject(content,"pager");
        pageIndex = JSONUtils.getInt(pager,"currentPage");
        totalPages = JSONUtils.getInt(pager,"totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content,"rows");
        if (membersIntegralLogList.size() > 0 ){
            if ( membersIntegralLogList.get(0).getType() != 0) {
                MembersIntegralLog membersIntegralLog1 = new MembersIntegralLog();
                membersIntegralLog1.setMemIntegral(integralNum);
                membersIntegralLog1.setReminder(roundedImg);
                membersIntegralLog1.setCreateTime(memberName);
                membersIntegralLog1.setType(0);
                membersIntegralLogList.add(membersIntegralLog1);
            }
        }else {
            MembersIntegralLog membersIntegralLog1 = new MembersIntegralLog();
            membersIntegralLog1.setMemIntegral(integralNum);
            membersIntegralLog1.setReminder(roundedImg);
            membersIntegralLog1.setCreateTime(memberName);
            membersIntegralLog1.setType(0);
            membersIntegralLogList.add(membersIntegralLog1);
        }
        try {
            for (int i = 0; i < rows.length(); i++) {
                MembersIntegralLog membersIntegralLog = new MembersIntegralLog();
                JSONObject jsonObject = rows.getJSONObject(i);
                membersIntegralLog.setCreateTime(JSONUtils.getString(jsonObject,"createTime"));
                membersIntegralLog.setReminder(JSONUtils.getString(jsonObject,"reminder"));
                membersIntegralLog.setMemIntegral(JSONUtils.getInt(jsonObject,"memIntegral"));
                membersIntegralLog.setLogType(JSONUtils.getInt(jsonObject,"logType"));
                membersIntegralLog.setLogId(JSONUtils.getInt(jsonObject,"logId"));
                membersIntegralLog.setType(1);
                membersIntegralLogList.add(membersIntegralLog);
            }
            membersIntegralAdapter.notifyDataSetChanged();
            membersIntegralRl.NotifyCompleteRefresh0();
            //在这里我设置当加载到第三页时设置已经加载完成
            if (pageIndex == totalPages) {
                isLoad = false;
            } else {
                isLoad = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadingDialog.dismiss();
    }

    @Override
    public void findIntegralLogListError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
