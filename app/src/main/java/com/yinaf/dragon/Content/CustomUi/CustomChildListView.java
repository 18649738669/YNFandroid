package com.yinaf.dragon.Content.CustomUi;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by long on 2018/04/26.
 * 功能：自定义内部用于嵌套的子listview
 */

public class CustomChildListView extends ListView {
    public CustomChildListView(Context context) {
        super(context);
    }

    public CustomChildListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomChildListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int intexpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
