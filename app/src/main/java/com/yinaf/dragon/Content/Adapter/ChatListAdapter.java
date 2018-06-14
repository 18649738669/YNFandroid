package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Bean.ChatMessage;
import com.yinaf.dragon.Content.CustomUi.CircleImageView;
import com.yinaf.dragon.Content.Utils.ChatTimeUtil;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.App;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by long on 2018/5/10.
 * 功能：语音聊天页面的内容列表的适配器
 */

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChatMessage> listBeen;
    private ItemListener itemListener;
    private ImageLoader imageLoader;


    public static final int USER_CLICK = 1;
    public static final int WATCH_CLICK = 2;

    public static final int UPDATING = 100, SUCCESS = 200, FAIL = 300;

    private int WATCH_MESSAGE = 0;
    private int USER_MESSAGE = 1;

    private UserHolder userHolder;
    private WatchHolder watchHolder;

    public ChatListAdapter(Context context,List<ChatMessage> listBeen,ItemListener itemListener) {
        this.context = context;
        this.listBeen = listBeen;
        this.itemListener = itemListener;
        imageLoader = App.getImageLoader();

    }

    public interface ItemListener {
        void itemCLick(int type, ChatMessage bean, View v, int position);

        void rePost(ChatMessage bean, int position);

        void headInfoClick(int type);
    }

    @Override
    public int getItemViewType(int position) {
        return listBeen.get(position).getSend_type();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == USER_MESSAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_message_user_layout, parent, false);
            return new UserHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_message_layout, parent, false);
            return new WatchHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (listBeen.get(position).getSend_type() == 1) {
            userHolder = (UserHolder) holder;
            setChatTime(listBeen,position,userHolder.chat_time);
            if (listBeen.get(position).getImg() == null || listBeen.get(position).getImg().equals("")){
                userHolder.head.setImageResource(R.drawable.rounded_img2);
            }else {
                imageLoader.displayImage(listBeen.get(position).getImg(), userHolder.head);
            }
            userHolder.message_news.setText((int)listBeen.get(position).getVoice_length() + "\"");
//            userHolder.message_date.setText(DateTimeUtil.ConvertStampToDate(((float) listBeen.get(position).getDate()) * 1000, "yyyy.MM.dd HH:mm"));

            //设置linearlayout的长度
            ViewGroup.LayoutParams lp = userHolder.news_layout.getLayoutParams();
            lp.width = listBeen.get(position).getVoice_length() > 8 ? 420 : (int) listBeen.get(position).getVoice_length() * 25 + 220;

            userHolder.news_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.itemCLick(USER_CLICK, listBeen.get(position),v,position);
                }
            });
            userHolder.head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.headInfoClick(USER_CLICK);
                }
            });

            //成功发送消息
            if (listBeen.get(position).getIs_send() == UPDATING) {
                userHolder.progressBar.setVisibility(View.VISIBLE);
                userHolder.send_fail.setVisibility(View.GONE);
            } else if (listBeen.get(position).getIs_send() == SUCCESS) {
                userHolder.progressBar.setVisibility(View.GONE);
                userHolder.send_fail.setVisibility(View.GONE);
            } else {
                userHolder.progressBar.setVisibility(View.GONE);
                userHolder.send_fail.setVisibility(View.VISIBLE);
            }
            userHolder.send_fail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.rePost(listBeen.get(position),position);
                }
            });

        } else {
            watchHolder = (WatchHolder) holder;
            setChatTime(listBeen,position,watchHolder.chat_time);
            if (listBeen.get(position).getImg() == null || listBeen.get(position).getImg().equals("")){
                watchHolder.head.setImageResource(R.drawable.rounded_img2);
            }else {
                imageLoader.displayImage(listBeen.get(position).getImg(), watchHolder.head);
            }
            watchHolder.message_news.setText((int)listBeen.get(position).getVoice_length() + "\"");
            watchHolder.progressBar.setVisibility(View.GONE);
//            watchHolder.message_date.setText(DateTimeUtil.timedate(String.valueOf(listBeen.get(position).getDate())));

            //设置linearlayout的长度
            ViewGroup.LayoutParams lp = watchHolder.news_layout.getLayoutParams();
            lp.width = listBeen.get(position).getVoice_length() > 7 ? 420 : (int) listBeen.get(position).getVoice_length() * 20 + 280;

            watchHolder.news_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.itemCLick(WATCH_CLICK, listBeen.get(position),v,position);
                }
            });

            watchHolder.head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    itemListener.headInfoClick(WATCH_CLICK);
                }
            });

            //成功发送消息
            if (listBeen.get(position).getIs_send() == UPDATING) {
                watchHolder.progressBar.setVisibility(View.VISIBLE);
                watchHolder.send_fail.setVisibility(View.GONE);
            } else if (listBeen.get(position).getIs_send() == SUCCESS) {
                watchHolder.progressBar.setVisibility(View.GONE);
                watchHolder.send_fail.setVisibility(View.GONE);
            } else {
                watchHolder.progressBar.setVisibility(View.GONE);
                watchHolder.send_fail.setVisibility(View.VISIBLE);
            }

            watchHolder.send_fail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.rePost(listBeen.get(position),position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    protected void setChatTime(List<ChatMessage> messages,int pos,TextView text_time){
        if (messages.size() < pos + 2) {
            text_time.setVisibility(View.GONE);
            return;
        }
        double timestamp = messages.get(pos).getCreate_time_double();
        double nextTimestamp = messages.get(pos + 1).getCreate_time_double();
        if (timestamp - nextTimestamp > 3*60){
            text_time.setVisibility(View.VISIBLE);
            text_time.setText(ChatTimeUtil.getNewChatTime((long)timestamp * 1000));
        }else {
            text_time.setVisibility(View.GONE);
        }
    }

    public class WatchHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chatlistitem_head)
        CircleImageView head;
        @BindView(R.id.message_news)
        TextView message_news;
        @BindView(R.id.send_fail)
        ImageView send_fail;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.message_date)
        TextView message_date;
        @BindView(R.id.send_img)
         ImageView send_img;
        @BindView(R.id.news_layout)
        RelativeLayout news_layout;
        @BindView(R.id.chat_time)
        TextView chat_time;

        public WatchHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chatlistitem_head)
        CircleImageView head;
        @BindView(R.id.message_news)
        TextView message_news;
        @BindView(R.id.send_fail)
        ImageView send_fail;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.message_date)
        TextView message_date;
        @BindView(R.id.send_img)
         ImageView send_img;
        @BindView(R.id.news_layout)
        RelativeLayout news_layout;
        @BindView(R.id.chat_time)
        TextView chat_time;

        public UserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
