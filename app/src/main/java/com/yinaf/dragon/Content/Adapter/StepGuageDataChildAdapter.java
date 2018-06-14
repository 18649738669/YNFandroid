package com.yinaf.dragon.Content.Adapter;

import android.widget.TextView;

import com.yinaf.dragon.Content.Bean.StepGuageDataChild;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAdapter;
import com.yinaf.dragon.Tool.Activity.BasicViewHolder;

import java.util.List;

/**
 * Created by long on 2018/04/26.
 * 功能：计步数据的子listview的适配器
 */

public class StepGuageDataChildAdapter extends BasicAdapter{

    private List<StepGuageDataChild> data;

    public StepGuageDataChildAdapter(List<StepGuageDataChild> data) {
        super(data, R.layout.item_step_gauge_data_child_lv_item);
        this.data = data;
    }

    @Override
    public void bind(BasicViewHolder viewHolder, int position) {

        StepGuageDataChild stepGuageDataChild = data.get(position);
        TextView time = viewHolder.getTextView(R.id.step_gauge_data_item_lv_item_time);
        TextView step = viewHolder.getTextView(R.id.step_gauge_data_item_lv_item_step);
        time.setText(stepGuageDataChild.getTime());
        step.setText(stepGuageDataChild.getStep() + "");


    }
}
