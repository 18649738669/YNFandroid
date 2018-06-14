package com.yinaf.dragon.Content.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Activity.LeisureActivitiesAct;
import com.yinaf.dragon.Content.Activity.NewsDetailsAct;
import com.yinaf.dragon.Content.Activity.SmartDevicesAct;
import com.yinaf.dragon.Content.Activity.WebViewAct;
import com.yinaf.dragon.Content.Bean.HomeFrgModel;
import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Utils.ImageUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yinaf.dragon.Tool.APP.App.getContext;

/**
 * Created by long on 2018/06/01.
 * 首页列表适配器
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    public static enum ITEM_TYPE {
        ITEM_TYPE_VIEWPAGER,
        ITEM_TYPE_ONE_IMG,
        ITEM_TYPE_GRIDVIEW,
        ITEM_TYPE_LISTVIEW
    }

    private final LayoutInflater mLayoutInflater;
    private final Context context;
    private List<HomeFrgModel.ObjBean.ApplicationListBean> applicationListBeans;
    private List<HomeFrgModel.ObjBean.BannerListBean> bannerListBeans;
    private List<HomeFrgModel.ObjBean.NewsListBean> newsListBeans;
    private List<TwoTextData> twoTextData;

    //存放viewpager图片的数组
    List<ImageView> mList = new ArrayList<ImageView>();
    //当前索引位置以及上一个索引位置
    int index = 0, preIndex = 0;
    //是否需要轮播标志
    boolean isContinue = true;
    //定时器，用于实现轮播
    Timer timer;
    pageAdapter pageAdapter1;
    gridAdapter gridAdapter1;
    RadioGroup homeGroup;
    ViewPager homeViewpager;
    ImageLoader imageLoader;

    final Handler mHandler = new Handler() {
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


    public HomeAdapter(Context context, List<HomeFrgModel.ObjBean.ApplicationListBean> applicationListBeans,
                       List<HomeFrgModel.ObjBean.BannerListBean> bannerListBeans,
                       List<HomeFrgModel.ObjBean.NewsListBean> newsListBeans,
                       List<TwoTextData> twoTextData) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.applicationListBeans = applicationListBeans;
        this.bannerListBeans = bannerListBeans;
        this.newsListBeans = newsListBeans;
        this.twoTextData = twoTextData;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HomeAdapter.ITEM_TYPE.ITEM_TYPE_VIEWPAGER.ordinal()) {
            return new HomeAdapter.ViewPagerHolder(mLayoutInflater.inflate(R.layout.item_home_viewpagr, parent, false));
        }else if (viewType == ITEM_TYPE.ITEM_TYPE_ONE_IMG.ordinal()) {
            return new HomeAdapter.OneImgHolder(mLayoutInflater.inflate(R.layout.item_one_img, parent, false));
        }else if (viewType == ITEM_TYPE.ITEM_TYPE_GRIDVIEW.ordinal()) {
            return new HomeAdapter.GridViewHolder(mLayoutInflater.inflate(R.layout.item_home_gridview, parent, false));
        }else {
            return new HomeAdapter.ListViewHolder(mLayoutInflater.inflate(R.layout.item_home_lv_information, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ViewPagerHolder) {

            homeViewpager = ((ViewPagerHolder) holder).homeViewpager;
            homeGroup = ((ViewPagerHolder) holder).homeGroup;


            if (pageAdapter1==null){
                pageAdapter1 = new HomeAdapter.pageAdapter(bannerListBeans);
                homeViewpager.setAdapter(pageAdapter1);
                homeViewpager.addOnPageChangeListener(onPageChangeListener);
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
            }else {
                if (pageAdapter1.beans.size() == 0) {
                    pageAdapter1.beans.addAll(bannerListBeans);
                }
            }
            homeViewpager.setOnTouchListener(onTouchListener);
            if (homeGroup!=null){
                homeGroup.removeAllViews();
            }
            for (int i = 0; i < bannerListBeans.size(); i++) {
                ImageView imageview = new ImageView(getContext());
                imageview.setImageResource(R.drawable.vp_selector);//设置背景选择器
                imageview.setPadding(20, 0, 0, 0);//设置每个按钮之间的间距
                //将按钮依次添加到RadioGroup中
                homeGroup.addView(imageview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //默认选中第一个按钮，因为默认显示第一张图片
                homeGroup.getChildAt(0).setEnabled(false);
            }

        }else if (holder instanceof OneImgHolder){
            if (position == 1) {
                ((OneImgHolder) holder).itemOneText.setText(twoTextData.get(0).getText());
                ((OneImgHolder) holder).itemOneImg.setImageResource(R.drawable.user2);
            }else {
                ((OneImgHolder) holder).itemOneText.setText(twoTextData.get(1).getText());
                ((OneImgHolder) holder).itemOneImg.setImageResource(R.drawable.information);
            }

        }else if (holder instanceof GridViewHolder){

            gridAdapter1 = new gridAdapter(applicationListBeans,context);
            ((GridViewHolder) holder).homeGroupView.setAdapter(gridAdapter1); // 将适配器与GridView关联

        }else if (holder instanceof ListViewHolder){
            final HomeFrgModel.ObjBean.NewsListBean information = newsListBeans.get(position-4);
            ((ListViewHolder) holder).homeTitle.setText(information.getNew_title());
            ((ListViewHolder) holder).homeTime.setText(StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()));
            Glide.with(context).load(information.getNew_pic()).into(((ListViewHolder) holder).homeImage);
            ((ListViewHolder) holder).homeLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), NewsDetailsAct.class);
                    intent.putExtra("id", information.getNew_id() + "");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
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
            int pos = 0;
            if(bannerListBeans.size() != 0){
                pos = index % bannerListBeans.size();
            }

            /**
             * 设置对应位置按钮的状态
             *
             * @param i 当前位置
             */
            if (homeGroup.getChildAt(pos) != null) {
                homeGroup.getChildAt(pos).setEnabled(false);//当前按钮选中
            }
            if (homeGroup.getChildAt(preIndex) != null) {
                homeGroup.getChildAt(preIndex).setEnabled(true);//上一个取消选中
                preIndex = pos;//当前位置变为上一个，继续下次轮播
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return ITEM_TYPE.ITEM_TYPE_VIEWPAGER.ordinal();
            case 1:
            case 3:
                return ITEM_TYPE.ITEM_TYPE_ONE_IMG.ordinal();
            case 2:
                return ITEM_TYPE.ITEM_TYPE_GRIDVIEW.ordinal();
            default:
                return ITEM_TYPE.ITEM_TYPE_LISTVIEW.ordinal();
        }
    }

    @Override
    public int getItemCount() {
        return newsListBeans.size() + 4;
    }

    public static class ViewPagerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_viewpager)
        ViewPager homeViewpager;
        @BindView(R.id.home_group)
        RadioGroup homeGroup;

        ViewPagerHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


    public static class GridViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_home_grid)
        GridView homeGroupView;

        GridViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class OneImgHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_one_text)
        TextView itemOneText;
        @BindView(R.id.item_one_img)
        ImageView itemOneImg;

        OneImgHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_lv_item_title)
        TextView homeTitle;
        @BindView(R.id.home_lv_item_time)
        TextView homeTime;
        @BindView(R.id.home_lv_item_image)
        ImageView homeImage;
        @BindView(R.id.home_ll_item_information)
        LinearLayout homeLinear;


        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
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
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("title", bannerListBean.getBanner_title());
                    intent.putExtra("url", bannerListBean.getBanner_jump_url());
                    context.startActivity(intent);
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

    /**
     * GridView的适配器
     */
    private class gridAdapter extends BaseAdapter {
        private List<HomeFrgModel.ObjBean.ApplicationListBean> gridData;
        private LayoutInflater inflater;

        public gridAdapter(List<HomeFrgModel.ObjBean.ApplicationListBean> gridData, Context context) {
            this.gridData = gridData;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return gridData.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final HomeFrgModel.ObjBean.ApplicationListBean applicationListBean =gridData.get(position);
            imageLoader = ImageLoader.getInstance();
            ViewHolderGrid holer = null;
            View view = inflater.inflate(R.layout.homes_app_item, null);
            if (holer == null) {
                holer = new ViewHolderGrid();
                holer.name = (TextView) view.findViewById(R.id.tv_title);
                holer.item_know_grid_img = (ImageView) view.findViewById(R.id.iv_ima);
                holer.linearLayout = view.findViewById(R.id.grid_item_ll);
                view.setTag(holer);

            } else {
                holer = (ViewHolderGrid) view.getTag();
            }

            holer.name.setText(applicationListBean.getApp_title());
//            //          加载图片
            imageLoader.displayImage(applicationListBean.getApp_pic(), holer.item_know_grid_img);
            holer.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (applicationListBean.getApp_state() == 0) {
                        if (applicationListBean.getApp_title().equals("休闲活动")) {
                            Intent intent = new Intent(getContext(), LeisureActivitiesAct.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
//                            context.startActivity(new Intent(getContext(), LeisureActivitiesAct.class));
                        }
                        if (applicationListBean.getApp_title().equals("智能设备")) {
                            Intent intent = new Intent(getContext(), SmartDevicesAct.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
//                            SmartDevicesAct.startActivity(getContext());
                        }
                    } else {
                        Intent intent = new Intent(getContext(), WebViewAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("title", applicationListBean.getApp_title());
                        intent.putExtra("url", applicationListBean.getApp_jump_url());
                        context.startActivity(intent);
                    }
                }
            });
            return view;
        }
        //  适配器中的GridView缓存类
        class ViewHolderGrid {
            TextView name;
            ImageView item_know_grid_img;
            LinearLayout linearLayout;

        }
    }

}
