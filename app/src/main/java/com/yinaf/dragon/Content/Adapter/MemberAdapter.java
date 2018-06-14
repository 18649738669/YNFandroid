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
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.R;

import java.util.List;

/**
 * Created by long on 2018/4/20.
 * 功能：亲友圈-成员列表的适配器
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

    private List<Member> mDatas;
    private ImageLoader imageLoader;
    private Context mContext;


    public MemberAdapter(Context context, List<Member> datas) {
        this.mContext = context;
        this.mDatas = datas;
        imageLoader = ImageLoader.getInstance();
    }

    public void setItemList(List date) {
        this.mDatas = date;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_friends_circle_lv_member,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Member member = mDatas.get(position);
        if (member.getTrueName() == null || member.getTrueName().equals("")){
            holder.textView.setText(member.getRealName());
        }else {
            holder.textView.setText(member.getTrueName());
        }
        if (member.getIsLead() == 1){
            holder.imageView.setImageResource(R.drawable.main);
        }else {
            holder.imageView.setImageResource(R.drawable.vice);
        }
        if (member.getImage().equals("")) {
            holder.roundedImageView.setImageResource(R.drawable.rounded_img2);
        } else {
            imageLoader.displayImage(member.getImage(), holder.roundedImageView);
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
//                Toast.makeText(mContext, "onClick:" + mDatas.get(holder.getAdapterPosition()).getRealName(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onClick() called with: v = [" + v + "]");
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
        TextView textView;
        RoundedImageView roundedImageView;
        ImageView imageView;
        TextView delete;
        LinearLayout content;

        public MyViewHolder(View itemView) {
            super(itemView);
            // 关联引动该元素 ，在item.xml中findView，注意不要忘写(itemview.)
            textView = itemView.findViewById(R.id.friends_circle_lv_item_name);
            delete = itemView.findViewById(R.id.friends_circle_lv_item_delete);
            content = itemView.findViewById(R.id.friends_circle_lv_item_content);
            roundedImageView = itemView.findViewById(R.id.friends_circle_lv_item_image_rounded);
            imageView = itemView.findViewById(R.id.friends_circle_lv_item_image_deputy);

        }
    }

}




