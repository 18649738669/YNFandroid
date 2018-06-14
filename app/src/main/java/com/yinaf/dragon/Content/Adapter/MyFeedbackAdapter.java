package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Activity.FeedbackDetailsAct;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Bean.ThreeTextData;
import com.yinaf.dragon.R;

import java.util.List;

/**
 * Created by long on 2018/4/20.
 * 功能：亲友圈-成员列表的适配器
 */

public class MyFeedbackAdapter extends RecyclerView.Adapter<MyFeedbackAdapter.MyViewHolder> {

    private List<ThreeTextData> mDatas;
    private Context mContext;


    public MyFeedbackAdapter(Context context, List<ThreeTextData> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public void setItemList(List date) {
        this.mDatas = date;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_feedback_lv,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ThreeTextData threeTextData = mDatas.get(position);
        holder.text.setText(threeTextData.getContent());
        holder.time.setText(threeTextData.getTime());
        switch (Integer.parseInt(threeTextData.getContent1())){
            case 0:
                holder.state.setText("待处理");
                break;
            case 1:
                holder.state.setText("正在处理");
                break;
            case 2:
                holder.state.setText("回复待评价");
                break;
            case 3:
                holder.state.setText("已评价");
                break;
            default:break;
        }
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });

        (holder.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackDetailsAct.startActivity(mContext,threeTextData.getType(),Integer.parseInt(threeTextData.getContent1()));
            }
        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * RecyclerView的ViewHolder
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        // Item子布局上的一个元素
        TextView text;
        TextView state;
        TextView time;
        TextView delete;
        LinearLayout content;

        public MyViewHolder(View itemView) {
            super(itemView);
            // 关联引动该元素 ，在item.xml中findView，注意不要忘写(itemview.)
            text = itemView.findViewById(R.id.text);
            time = itemView.findViewById(R.id.time);
            state = itemView.findViewById(R.id.state);
            delete = itemView.findViewById(R.id.my_feedback_lv_item_delete);
            content = itemView.findViewById(R.id.my_feedback_lv_item_content);

        }
    }

}




