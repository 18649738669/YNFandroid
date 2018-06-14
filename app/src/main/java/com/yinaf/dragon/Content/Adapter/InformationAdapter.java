package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yinaf.dragon.Content.Bean.HomeFrgModel;
import com.yinaf.dragon.Content.Bean.Information;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAdapter;
import com.yinaf.dragon.Tool.Activity.BasicViewHolder;
import com.yinaf.dragon.Tool.Utils.StringUtils;

import java.util.List;

/**
 * Created by long on 2018-4-14.
 * 功能：首页 "实时资讯" 列表的适配器
 */

public class InformationAdapter extends BasicAdapter{

    private List<HomeFrgModel.ObjBean.NewsListBean> date;
private Context context;

    public InformationAdapter(Context context,List<HomeFrgModel.ObjBean.NewsListBean> data) {
        super(data, R.layout.item_home_lv_information);
        this.date = data;
        this.context =context;
    }

    @Override
    public void bind(BasicViewHolder viewHolder, int position) {

        HomeFrgModel.ObjBean.NewsListBean information = date.get(position);

        TextView title = viewHolder.getTextView(R.id.home_lv_item_title);
        TextView time = viewHolder.getTextView(R.id.home_lv_item_time);
        ImageView image = viewHolder.getImageView(R.id.home_lv_item_image);
        title.setText(information.getNew_title());
        time.setText(StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp()));
        Glide.with(context).load(information.getNew_pic()).into(image);


    }
}
