package com.yinaf.dragon.Content.Adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.yinaf.dragon.Content.Bean.Step;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAdapter;
import com.yinaf.dragon.Tool.Activity.BasicViewHolder;

import java.util.List;

/**
 * Created by long on 2018-4-16.
 * 功能：家人页面 "动态" 列表的适配器
 */

public class StepAdapter extends BasicAdapter {

    private List<Step> date;

    public StepAdapter(List<Step> data) {
        super(data, R.layout.item_family_lv_my_step);
        this.date = data;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position%data.size());
    }

    @Override
    public long getItemId(int position) {
        return position % data.size();
    }

    @Override
    public void bind(BasicViewHolder viewHolder, int position) {

        Step step = date.get(position % data.size());

        TextView title = viewHolder.getTextView(R.id.family_tv_item_title);
        TextView time = viewHolder.getTextView(R.id.family_lv_item_my_step_time);
        TextView content = viewHolder.getTextView(R.id.family_lv_item_my_step_content);
        ImageView image = viewHolder.getImageView(R.id.family_lv_item_image);

        title.setText(step.getTitle());
        time.setText(step.getTime());
        content.setText(step.getContent());
        image.setImageResource(step.getImage());

    }
}
