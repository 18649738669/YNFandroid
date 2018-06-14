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
 * 功能：心率数据的listview的适配器
 */

public class HeartRetaDataAdapter extends BasicAdapter {

    private List<TwoTextData> data;

    public HeartRetaDataAdapter(List<TwoTextData> data) {
        super(data, R.layout.item_step_gauge_data);
        this.data = data;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void bind(BasicViewHolder viewHolder, int position) {

        TwoTextData twoTextData = data.get(position);
        TextView time = viewHolder.getTextView(R.id.step_gauge_data_item_time);
        TextView text = viewHolder.getTextView(R.id.step_gauge_data_item_text);
        LinearLayout linearLayout = viewHolder.getLinearLayout(R.id.step_gauge_data_item_ll);
        time.setText(twoTextData.getTime());
        text.setText(twoTextData.getText());
        switch (twoTextData.getType()){
            case 0:
                linearLayout.setBackgroundResource(R.color.common_pink);
                text.setTextColor(R.color.common_white);
                time.setTextColor(R.color.common_white);
                break;
            case 1:
                linearLayout.setBackgroundResource(R.color.common_white);
                text.setTextColor(R.color.common_black);
                time.setTextColor(R.color.common_black);
                break;
            case 2:
                linearLayout.setBackgroundResource(R.color.tips_text);
                text.setTextColor(R.color.common_black);
                time.setTextColor(R.color.common_black);
                break;
        }
    }

}
