package com.yinaf.dragon.Content.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Bean.MembersIntegralLog;
import com.yinaf.dragon.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/05/09.
 * 功能：成员积分详细列表的适配器
 */

public class MembersIntegralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    public static enum ITEM_TYPE {
        ITEM_TYPE_SUM,
        ITEM_TYPE_TEXT
    }

    private final LayoutInflater mLayoutInflater;
    private final Context context;
    private List<MembersIntegralLog> data;
    private ImageLoader imageLoader;


    public MembersIntegralAdapter( Context context ,List<MembersIntegralLog> data) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.data = data;
        imageLoader = ImageLoader.getInstance();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_SUM.ordinal()) {
            return new SumViewHolder(mLayoutInflater.inflate(R.layout.item_members_integral_1, parent, false));
        } else {
            return new TextViewHolder(mLayoutInflater.inflate(R.layout.item_members_integral_2, parent, false));
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).time.setText(data.get(position).getCreateTime());
            ((TextViewHolder) holder).content.setText(data.get(position).getReminder());
            if (data.get(position).getLogType() == 1 ){
                ((TextViewHolder) holder).number.setText("+" + data.get(position).getMemIntegral());
                ((TextViewHolder) holder).number.setTextColor(Color.RED);
            }else if (data.get(position).getLogType() == 2 ){
                ((TextViewHolder) holder).number.setText("-" + data.get(position).getMemIntegral());
                ((TextViewHolder) holder).number.setTextColor(Color.argb(255,0,170,239));
            }
        } else if (holder instanceof SumViewHolder) {
            ((SumViewHolder) holder).sum.setText( "总积分：" + data.get(position).getMemIntegral() );
            ((SumViewHolder) holder).name.setText( data.get(position).getCreateTime() );
            imageLoader.displayImage(data.get(position).getReminder(), ((SumViewHolder) holder).img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType() == 0 ? ITEM_TYPE.ITEM_TYPE_SUM.ordinal() : ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public static class SumViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.members_integral_sum)
        TextView sum;
        @BindView(R.id.members_integral_rounded_img)
        RoundedImageView img;
        @BindView(R.id.members_integral_name)
        TextView name;

        SumViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.members_integral_content)
        TextView content;
        @BindView(R.id.members_integral_number)
        TextView number;
        @BindView(R.id.members_integral_time)
        TextView time;

        TextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
