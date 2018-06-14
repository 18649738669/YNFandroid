package com.yinaf.dragon.Content.Adapter;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yinaf.dragon.Content.Bean.ThreeTextData;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAdapter;
import com.yinaf.dragon.Tool.Activity.BasicViewHolder;

import java.util.List;

/**
 * Created by long on 2018-4-14.
 * 功能：睡眠数据列表的适配器
 */

public class SleepDataAdapter extends BasicAdapter{

    private List<ThreeTextData> date;

    public SleepDataAdapter(List<ThreeTextData> data) {
        super(data, R.layout.item_sleep_data);
        this.date = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void bind(BasicViewHolder viewHolder, int position) {

        ThreeTextData threeTextData = date.get(position);
        TextView time = viewHolder.getTextView(R.id.sleep_data_item_tv_time);
        TextView deep = viewHolder.getTextView(R.id.sleep_data_item_tv_deep);
        TextView shallow = viewHolder.getTextView(R.id.sleep_data_item_tv_shallow);
        LinearLayout linearLayout = viewHolder.getLinearLayout(R.id.sleep_data_item_ll);
        time.setText(threeTextData.getTime());
        deep.setText(threeTextData.getContent());
        shallow.setText(threeTextData.getContent1());
        switch (threeTextData.getType()){
            case 0:
                linearLayout.setBackgroundResource(R.color.common_purle);
                deep.setTextColor(R.color.common_white);
                time.setTextColor(R.color.common_white);
                shallow.setTextColor(R.color.common_white);
                break;
            case 1:
                linearLayout.setBackgroundResource(R.color.common_white);
                deep.setTextColor(R.color.common_black);
                time.setTextColor(R.color.common_black);
                shallow.setTextColor(R.color.common_black);
                break;
            case 2:
                linearLayout.setBackgroundResource(R.color.tips_text);
                deep.setTextColor(R.color.common_black);
                time.setTextColor(R.color.common_black);
                shallow.setTextColor(R.color.common_black);
                break;
        }


    }
}
