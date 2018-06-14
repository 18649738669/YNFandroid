package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Activity.ChatMainAct;
import com.yinaf.dragon.Content.Bean.ChatListBean;
import com.yinaf.dragon.Content.CustomUi.CircleImageView;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.App;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2016/10/31.
 * 功能：微聊的list适配器
 */

public class WatchChatListAdapter extends RecyclerView.Adapter<WatchChatListAdapter.MyHolder> {

    private Context context;
    private List<ChatListBean> listBeen;
    private ImageLoader imageLoader;

    public WatchChatListAdapter(Context context, List<ChatListBean> listBeen) {
        this.context = context;
        this.listBeen = listBeen;
        imageLoader = App.getImageLoader();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item_chatlist, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        if (listBeen.get(position).getImg() == null || listBeen.get(position).getImg().equals("")){
            holder.head.setImageResource(R.drawable.rounded_img2);
        }else {
            imageLoader.displayImage(listBeen.get(position).getImg(), holder.head);
        }
        if(listBeen.get(position).getMessageNum()==0){
            holder.num.setVisibility(View.GONE);
        }else{
            holder.num.setVisibility(View.VISIBLE);
            if(listBeen.get(position).getMessageNum()>99){
                holder.num.setText("99+");
            }else{
                holder.num.setText(String.valueOf(listBeen.get(position).getMessageNum()));
            }
        }
        if(listBeen.get(position).getLastSendTime()!=null){
            if(listBeen.get(position).getLastSendTime().equals("0.0")){
                holder.content.setText(R.string.no_message);
                holder.time.setVisibility(View.GONE);
            }else{
                holder.content.setText(R.string.voice_chat_message);
                holder.time.setVisibility(View.VISIBLE);
                holder.time.setText(listBeen.get(position).getLastSendTime());
            }
        }else{
            holder.time.setText("");
        }

        holder.name.setText(listBeen.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int pos = holder.getLayoutPosition();

                ChatMainAct.startActivity(context,listBeen.get(pos));

            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chatlistitem_head)
        CircleImageView head;
        @BindView(R.id.chatlistitem_num)
        TextView num;
        @BindView(R.id.chatlistitem_name)
        TextView name;
        @BindView(R.id.chatlistitem_time)
        TextView time;
        @BindView(R.id.chatlist_content)
        TextView content;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
