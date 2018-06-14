package com.yinaf.dragon.Content.Adapter;


import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Bean.TwoTextData;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAdapter;
import com.yinaf.dragon.Tool.Activity.BasicViewHolder;

import java.util.List;

/**
 * Created by long on 2018/04/26.
 * 功能：历史定位的listview的适配器
 */

public class HistoricalPositionAdapter extends BasicAdapter {

    private List<TwoTextData> data;

    public HistoricalPositionAdapter(List<TwoTextData> data) {
        super(data, R.layout.item_historical_position);
        this.data = data;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void bind(BasicViewHolder viewHolder, int position) {

        TwoTextData twoTextData = data.get(position);
        TextView time = viewHolder.getTextView(R.id.historical_position_item_tv_time);
        TextView text = viewHolder.getTextView(R.id.historical_position_item_tv_data);
        time.setText(twoTextData.getTime());
        text.setText(twoTextData.getType() + "条");
    }
}
