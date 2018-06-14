package com.yinaf.dragon.Content.Adapter;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Bean.AnAlarm;
import com.yinaf.dragon.Content.Bean.ThreeTextData;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAdapter;
import com.yinaf.dragon.Tool.Activity.BasicViewHolder;

import java.util.List;

/**
 * Created by long on 2018-4-14.
 * 功能：健康警报列表的适配器
 */

public class AnAlarmAdapter extends BasicAdapter{

    private List<AnAlarm> date;

    public AnAlarmAdapter(List<AnAlarm> data) {
        super(data, R.layout.item_an_alarm);
        this.date = data;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void bind(BasicViewHolder viewHolder, int position) {

        AnAlarm anAlarm = date.get(position);
        TextView title = viewHolder.getTextView(R.id.an_alarm_lv_item_title);
        TextView time = viewHolder.getTextView(R.id.an_alarm_lv_item_time);
        ImageView image = viewHolder.getImageView(R.id.an_alarm_lv_item_image);
        title.setText(anAlarm.getName() + "  " +anAlarm.getTitle());
        time.setText(anAlarm.getTime());
        image.setImageResource(R.drawable.fb_msg_tip);




    }
}
