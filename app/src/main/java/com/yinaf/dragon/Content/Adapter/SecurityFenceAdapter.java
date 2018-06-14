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

import com.amap.api.maps.model.LatLng;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Activity.SetSecurityFenceAct;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Bean.SecurityFence;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;

import java.util.List;

/**
 * Created by long on 2018/4/20.
 * 功能：安全围栏列表的适配器
 */

public class SecurityFenceAdapter extends RecyclerView.Adapter<SecurityFenceAdapter.MyViewHolder> {

    private List<SecurityFence> mDatas;
    private Context mContext;


    public SecurityFenceAdapter(Context context, List<SecurityFence> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public void setItemList(List date) {
        this.mDatas = date;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_security_fence,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SecurityFence securityFence = mDatas.get(position);

        holder.name.setText(securityFence.getName());
        holder.address.setText(securityFence.getAddress());

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
                String memberId = SPHelper.getInt(Builds.SP_USER,"memberId") +"";
                SetSecurityFenceAct.startActivity(v.getContext(),memberId,securityFence.getRailId(),
                        securityFence.getLatitude(),securityFence.getLongitude(),securityFence.getRadius(),
                        1,securityFence.getName(),securityFence.getAddress());
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
        TextView name;
        TextView address;
        TextView delete;
        LinearLayout content;

        public MyViewHolder(View itemView) {
            super(itemView);
            // 关联引动该元素 ，在item.xml中findView，注意不要忘写(itemview.)
            name = itemView.findViewById(R.id.security_fence_item_title);
            address = itemView.findViewById(R.id.security_fence_item_content);
            delete = itemView.findViewById(R.id.security_fence_lv_item_delete);
            content = itemView.findViewById(R.id.security_fence_lv_item_content);

        }
    }

}




