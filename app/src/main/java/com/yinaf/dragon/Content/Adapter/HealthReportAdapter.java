package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Bean.OneTextData;
import com.yinaf.dragon.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/05/08.
 * 功能：健康报告列表的适配器
 */

public class HealthReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{



    public static enum ITEM_TYPE {
        ITEM_TYPE_YEAR,
        ITEM_TYPE_TEXT
    }

    private final LayoutInflater mLayoutInflater;
    private final Context context;
    private List<OneTextData> data;
    private OnClickItemListener listener;

    public HealthReportAdapter( Context context ,List<OneTextData> data) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_YEAR.ordinal()) {
            return new YearViewHolder(mLayoutInflater.inflate(R.layout.item_year, parent, false));
        } else {
            return new TextViewHolder(mLayoutInflater.inflate(R.layout.item_health_report, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TextViewHolder) {
            ((TextViewHolder) holder).mTextView.setText(data.get(position).getText() + "月份  健康报告");

            ((TextViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickItem(view, (String) view.getTag(),position);
                }
            });
        } else if (holder instanceof YearViewHolder) {
            ((YearViewHolder) holder).year.setText(data.get(position).getText() + "年");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getType() == 0 ? ITEM_TYPE.ITEM_TYPE_YEAR.ordinal() : ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.health_records_tv_health_report)
        TextView mTextView;
        @BindView(R.id.health_records_ll_health_report)
        LinearLayout mLinearLayout;

        TextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    public static class YearViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_year)
        TextView year;

        YearViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    public interface OnClickItemListener {
        void onClickItem(View view, String tag,int position);
    }
}
