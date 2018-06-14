package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amap.api.services.help.Tip;
import com.nestrefreshlib.RefreshViews.RefreshLayout;
import com.nestrefreshlib.RefreshViews.RefreshListener;
import com.yinaf.dragon.Content.Activity.family_set.DataEventBus;
import com.yinaf.dragon.Content.Adapter.MyFeedbackAdapter;
import com.yinaf.dragon.Content.Bean.ThreeTextData;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.DelFeedbackAPI;
import com.yinaf.dragon.Content.Net.FindFeedbackListAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/05/30.
 * 功能：我的反馈页面
 */

public class MyFeedbackAct extends BasicAct implements FindFeedbackListAPI.FindFeedbackListListener,
        DelFeedbackAPI.DelFeedbackListener{

    @BindView(R.id.my_feedback_rl)
    RefreshLayout myFeedbackRl;

    MyFeedbackAdapter myFeedbackAdapter;//我的反馈列表适配器
    List<ThreeTextData> threeTextDataList = new ArrayList<ThreeTextData>();//我的反馈列表

    private int pageIndex = 1;//当前页码
    private int totalPages = 0;//总页码
    public boolean isLoad = true;//是否允许上拉加载
    Handler handler = new Handler();
    LoadingDialog loadingDialog;


    public MyFeedbackAct() {
        super(R.layout.act_my_feedback, R.string.title_activity_my_feedback, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyFeedbackAct.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        EventBus.getDefault().register(this);

        loadingDialog = LoadingDialog.showDialog(this);
        loadingDialog.show();
        new FindFeedbackListAPI(this,pageIndex);

        initListView();
    }

    /**
     * 初始化列表
     */
    private void initListView() {

        loadData(pageIndex);
        myFeedbackAdapter = new MyFeedbackAdapter(this,threeTextDataList);
        myFeedbackRl.setListener(new RefreshListener() {
            @Override
            public void Refreshing() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadData(1);
                        myFeedbackRl.NotifyCompleteRefresh0();

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
                            myFeedbackRl.NotifyCompleteRefresh0();
                        }else {
                            TipUtils.showTip("没有更多数据");
                            myFeedbackRl.NotifyCompleteRefresh0();

                        }
                    }
                }, 500);
            }
        });
        myFeedbackAdapter.setOnDelListener(new MyFeedbackAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < threeTextDataList.size()) {
                    loadingDialog.show();
                    new DelFeedbackAPI(MyFeedbackAct.this,threeTextDataList.get(pos).getType());
                    threeTextDataList.remove(pos);
                    myFeedbackAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

        });
        RecyclerView recyclerView = myFeedbackRl.getmScroll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myFeedbackAdapter);
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
                }
                pageIndex = tempPageIndex;
                new FindFeedbackListAPI(MyFeedbackAct.this,pageIndex);
                //在这里我设置当加载到第三页时设置已经加载完成
                if (tempPageIndex == totalPages) {
                    isLoad = false;
                } else {
                    isLoad = true;
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

    /**
     * 我的反馈列表
     * @param content
     */
    @Override
    public void findFeedbackListSuccess(JSONObject content) {
        JSONObject pager = JSONUtils.getJSONObject(content,"pager");
        pageIndex = JSONUtils.getInt(pager,"currentPage");
        totalPages = JSONUtils.getInt(pager,"totalPages");
        JSONArray rows = JSONUtils.getJSONArray(content,"rows");
        try {
            for (int i = 0; i < rows.length(); i++) {
                ThreeTextData threeTextData = new ThreeTextData();
                JSONObject jsonObject = rows.getJSONObject(i);
                threeTextData.setTime(JSONUtils.getString(jsonObject,"createTime"));
                threeTextData.setContent(JSONUtils.getString(jsonObject,"feedbackContent"));
                threeTextData.setContent1(JSONUtils.getString(jsonObject,"state"));
                threeTextData.setType(JSONUtils.getInt(jsonObject,"feedbackId"));
                threeTextDataList.add(threeTextData);
            }
            myFeedbackAdapter.notifyDataSetChanged();
            myFeedbackRl.NotifyCompleteRefresh0();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadingDialog.dismiss();
    }

    @Override
    public void findFeedbackListError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataEventBus eventBus) {
        loadData(1);
        myFeedbackRl.NotifyCompleteRefresh0();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 删除反馈
     */
    @Override
    public void delFeedbackSuccess() {
        loadingDialog.dismiss();
        loadData(1);
        myFeedbackRl.NotifyCompleteRefresh0();
    }

    @Override
    public void delFeedbackError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
