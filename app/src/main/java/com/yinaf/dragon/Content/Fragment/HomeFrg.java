package com.yinaf.dragon.Content.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.yalantis.phoenix.PullToRefreshView;
import com.yinaf.dragon.Content.Activity.LeisureActivitiesAct;
import com.yinaf.dragon.Content.Activity.NewsDetailsAct;
import com.yinaf.dragon.Content.Activity.SmartDevicesAct;
import com.yinaf.dragon.Content.Activity.WebViewAct;
import com.yinaf.dragon.Content.Adapter.HomesAppAdapter;
import com.yinaf.dragon.Content.Adapter.InformationAdapter;
import com.yinaf.dragon.Content.Bean.HomeFrgModel;
import com.yinaf.dragon.Content.Bean.Information;
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

public class HomeFrg extends BasicFrg implements HomeFrgAPI.HomeFrgListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.home_viewpager)
    ViewPager homeViewpager;
    @BindView(R.id.home_gl_my_application)
    RecyclerView homeGlMyApplication;
    @BindView(R.id.home_group)
    RadioGroup homeGroup;
    @BindView(R.id.home_lv_information)
    ListView homeLvInformation;
    Unbinder unbinder;
    List<Information> informationList = new ArrayList<Information>(); //实时资讯列表
    InformationAdapter informationAdapter; //实时资讯列表适配器
    //存放viewpager图片的数组
    List<ImageView> mList = new ArrayList<ImageView>();
    //当前索引位置以及上一个索引位置
    int index = 0, preIndex = 0;
    //是否需要轮播标志
    boolean isContinue = true;
    //定时器，用于实现轮播
    Timer timer;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    index++;
                    if (homeViewpager != null)
                        homeViewpager.setCurrentItem(index);
            }
        }
    };
    private List<HomeFrgModel.ObjBean.BannerListBean> bannerList;
    private List<HomeFrgModel.ObjBean.ApplicationListBean> applicationList;
    private List<HomeFrgModel.ObjBean.NewsListBean> newsList;
    private SwipeRefreshLayout mSwipeLayout;
    private HomesAppAdapter homesAppAdapter;
    private pageAdapter pageAdapter1;


    public HomeFrg() {
        super(R.layout.frg_home);
    }

    @Override
    public void initView(View view) {
        homeGlMyApplication.setLayoutManager(new GridLayoutManager(getContext(), 4));

        new HomeFrgAPI(this);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        //绑定刷新时间
        mSwipeLayout.setOnRefreshListener(this);
        //设置颜色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    /**
     * 初始化实时资讯列表
     */
    private void initListView() {
        if (informationAdapter==null){
            informationAdapter = new InformationAdapter(getContext(), newsList);
            homeLvInformation.setAdapter(informationAdapter);
        }else {
            informationAdapter.setData(newsList);
        }

        informationAdapter.notifyDataSetChanged();

        homeLvInformation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeFrgModel.ObjBean.NewsListBean newsListBean = newsList.get(position);
                Intent intent = new Intent(getContext(), NewsDetailsAct.class);
                intent.putExtra("id", newsListBean.getNew_id() + "");
                startActivity(intent);
            }
        });


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


    /**
     * 初始化顶部轮播图
     */
    private void initViewPager() {
        if (pageAdapter1==null){
            pageAdapter1 = new pageAdapter(bannerList);
            homeViewpager.setAdapter(pageAdapter1);
            homeViewpager.addOnPageChangeListener(onPageChangeListener);
        }else {
            pageAdapter1.beans.addAll(bannerList);
        }
        homeViewpager.setOnTouchListener(onTouchListener);

        timer = new Timer();//创建Timer对象
        //执行定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //首先判断是否需要轮播，是的话我们才发消息
                if (isContinue) {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }, 5000, 5000);//延迟5秒，每隔5秒发一次消息
        initRadioButton(bannerList.size());
    }

    /**
     * 根据图片个数初始化按钮
     *
     * @param length
     */
    private void initRadioButton(int length) {
        if (homeGroup!=null){
            homeGroup.removeAllViews();
        }
        for (int i = 0; i < length; i++) {
            ImageView imageview = new ImageView(getContext());
            imageview.setImageResource(R.drawable.vp_selector);//设置背景选择器
            imageview.setPadding(20, 0, 0, 0);//设置每个按钮之间的间距
            //将按钮依次添加到RadioGroup中
            homeGroup.addView(imageview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //默认选中第一个按钮，因为默认显示第一张图片
            homeGroup.getChildAt(0).setEnabled(false);
        }
    }

    /**
     * 根据当前触摸事件判断是否要轮播
     */
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                //手指按下和划动的时候停止图片的轮播
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue = false;
                    break;
                default:
                    isContinue = true;
            }
            return false;
        }
    };


    /**
     * 根据当前选中的页面设置按钮的选中
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            index = position;//当前位置赋值给索引
            setCurrentDot(index % bannerList.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 设置对应位置按钮的状态
     *
     * @param i 当前位置
     */
    private void setCurrentDot(int i) {
        if (homeGroup.getChildAt(i) != null) {
            homeGroup.getChildAt(i).setEnabled(false);//当前按钮选中
        }
        if (homeGroup.getChildAt(preIndex) != null) {
            homeGroup.getChildAt(preIndex).setEnabled(true);//上一个取消选中
            preIndex = i;//当前位置变为上一个，继续下次轮播
        }
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

    /**
     * viewpager 的适配器
     */
    class pageAdapter extends PagerAdapter {

        private List<HomeFrgModel.ObjBean.BannerListBean> beans;

        public pageAdapter(List<HomeFrgModel.ObjBean.BannerListBean> bannerList) {
            this.beans = bannerList;
        }

        @Override
        public int getCount() {
            //返回一个比较大的值，目的是为了实现无限轮播
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % beans.size();

            ImageView imageView = new ImageView(getContext());

            Glide.with(getContext()).load(beans.get(position).getBanner_pic())
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            final int finalPosition = position;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeFrgModel.ObjBean.BannerListBean bannerListBean = beans.get(finalPosition);
                    Intent intent = new Intent(getContext(), WebViewAct.class);
                    intent.putExtra("title", bannerListBean.getBanner_title());
                    intent.putExtra("url", bannerListBean.getBanner_jump_url());
                    startActivity(intent);
                }
            });
            container.addView(imageView);
            mList.add(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position < mList.size()) {
                container.removeView(mList.get(position));
            }
        }
    }

    @Override
    public void homeFrgSuccess(HomeFrgModel drugSetModel) {

        if (drugSetModel != null && drugSetModel.getObj() != null) {
            HomeFrgModel.ObjBean obj = drugSetModel.getObj();

            //轮播
            if (obj.getBannerList() != null && obj.getBannerList().size() > 0) {
                if (bannerList != null && bannerList.size() > 0) {
                    bannerList.clear();
                    mList.clear();
                    pageAdapter1.beans.clear();
                    pageAdapter1.notifyDataSetChanged();
                }
                bannerList = obj.getBannerList();
                initViewPager();
            }
            //应用
            if (obj.getApplicationList() != null && obj.getApplicationList().size() > 0) {

                applicationList = obj.getApplicationList();
                homesAppAdapter = new HomesAppAdapter(getContext(), 0, applicationList);

                homeGlMyApplication.setAdapter(homesAppAdapter);

                homesAppAdapter.notifyDataSetChanged();


                homesAppAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        HomeFrgModel.ObjBean.ApplicationListBean applicationListBean =
                                homesAppAdapter.getDatas().get(position);
                        if (applicationListBean.getApp_state() == 0) {
                            if (applicationListBean.getApp_title().equals("休闲活动")) {
                                startActivity(new Intent(getContext(), LeisureActivitiesAct.class));
                            }
                            if (applicationListBean.getApp_title().equals("智能设备")) {
                                SmartDevicesAct.startActivity(getContext());
                            }
                        } else {
                            Intent intent = new Intent(getContext(), WebViewAct.class);
                            intent.putExtra("title", applicationListBean.getApp_title());
                            intent.putExtra("url", applicationListBean.getApp_jump_url());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
            }

            //资讯
            if (obj.getNewsList() != null && obj.getNewsList().size() > 0) {

                newsList = obj.getNewsList();
                initListView();
            }
        }
    }

    @Override
    public void homeFrgError(long code, String msg) {
        TipUtils.showTip(msg);
    }
}
