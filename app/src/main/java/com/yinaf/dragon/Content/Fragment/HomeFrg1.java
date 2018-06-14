package com.yinaf.dragon.Content.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.yinaf.dragon.Content.Activity.LeisureActivitiesAct;
import com.yinaf.dragon.Content.Activity.NewsDetailsAct;
import com.yinaf.dragon.Content.Activity.SmartDevicesAct;
import com.yinaf.dragon.Content.Activity.WebViewAct;
import com.yinaf.dragon.Content.Adapter.HomeAdapter;
import com.yinaf.dragon.Content.Adapter.HomesAppAdapter;
import com.yinaf.dragon.Content.Adapter.InformationAdapter;
import com.yinaf.dragon.Content.Bean.HomeFrgModel;
import com.yinaf.dragon.Content.Bean.Information;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.HomeFrgAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicFrg;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by long on 2018-4-13.
 * 功能：首页的fragment
 */

public class HomeFrg1 extends BasicFrg implements HomeFrgAPI.HomeFrgListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.home_gl_my_application)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private List<HomeFrgModel.ObjBean.BannerListBean> bannerList;
    private List<HomeFrgModel.ObjBean.ApplicationListBean> applicationList;
    private List<HomeFrgModel.ObjBean.NewsListBean> newsList;
    private List<TwoTextData> twoTextData;
    private SwipeRefreshLayout mSwipeLayout;
    private HomeAdapter homeAdapter;
    LoadingDialog loadingDialog;

    public HomeFrg1() {
        super(R.layout.frg_home1);
    }

    @Override
    public void initView(View view) {
        loadingDialog = LoadingDialog.showDialog(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadingDialog.show();
        new HomeFrgAPI(this);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        //绑定刷新时间
        mSwipeLayout.setOnRefreshListener(this);
        //设置颜色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        twoTextData = new ArrayList<TwoTextData>();
        TwoTextData twoTextData1 = new TwoTextData();
        twoTextData1.setText("我的应用");
        twoTextData1.setType(R.drawable.user2);
        twoTextData.add(twoTextData1);
        TwoTextData twoTextData2 = new TwoTextData();
        twoTextData2.setText("实时资讯");
        twoTextData2.setType(R.drawable.information);
        twoTextData.add(twoTextData2);


    }

    private void initListView() {

        homeAdapter = new HomeAdapter(getContext(),applicationList,bannerList,newsList,twoTextData);
        recyclerView.setAdapter(homeAdapter);
        homeAdapter.notifyDataSetChanged();
        loadingDialog.dismiss();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        new HomeFrgAPI(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //停止刷新
                mSwipeLayout.setRefreshing(false);

            }
        }, 1000);
    }


    @Override
    public void homeFrgSuccess(HomeFrgModel drugSetModel) {

        if (drugSetModel != null && drugSetModel.getObj() != null) {
            HomeFrgModel.ObjBean obj = drugSetModel.getObj();
            //轮播
            if (obj.getBannerList() != null && obj.getBannerList().size() > 0) {
                if (bannerList != null && bannerList.size() > 0) {
                    bannerList.clear();
                }
                bannerList = obj.getBannerList();
            }
            //应用
            if (obj.getApplicationList() != null && obj.getApplicationList().size() > 0) {
                if (applicationList != null && applicationList.size() > 0) {
                    applicationList.clear();
                }
                applicationList = obj.getApplicationList();
            }

            //资讯
            if (obj.getNewsList() != null && obj.getNewsList().size() > 0) {
                if (newsList != null && newsList.size() > 0) {
                    newsList.clear();
                }
                newsList = obj.getNewsList();
            }
        }
        initListView();


    }

    @Override
    public void homeFrgError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
