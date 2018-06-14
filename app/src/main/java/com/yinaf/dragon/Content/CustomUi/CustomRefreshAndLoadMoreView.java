package com.yinaf.dragon.Content.CustomUi;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yinaf.dragon.R;


/**
 * * Created by long on 2018-4-26.
 * 功能：配合CustomLoadMoreListView 完成下拉刷新、滑到底部自动加载更多
 */
public class CustomRefreshAndLoadMoreView extends SwipeRefreshLayout {
    private CustomLoadMoreListView mCustomLoadMoreListView;

    /**
     * 构造方法，用于在布局文件中用到这个自定义SwipeRefreshLayout控件
     * @param context
     * @param attrs
     */
    public CustomRefreshAndLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources res = getResources();
        //通过颜色资源文件设置进度动画的颜色资源
        setColorSchemeColors(res.getColor(R.color.common_blue),
                res.getColor(R.color.common_green),
                res.getColor(R.color.common_pink),
                res.getColor(R.color.common_orange));
    }
    public void setLoadMoreListView(CustomLoadMoreListView mCustomLoadMoreListView) {
        this.mCustomLoadMoreListView = mCustomLoadMoreListView;
    }

    /**
     * 触屏事件,如果ListView不为空且数据还在加载中，则继续加载直至完成加载才触摸此事件
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCustomLoadMoreListView != null && mCustomLoadMoreListView.isLoading()) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
