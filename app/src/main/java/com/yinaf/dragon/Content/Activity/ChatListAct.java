package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.yinaf.dragon.Content.Adapter.WatchChatListAdapter;
import com.yinaf.dragon.Content.Bean.ChatListBean;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.CustomUi.DividerItemDecoration;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetMemberBindedListAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by long on 2018/05/10.
 * 功能：微聊的list
 */

public class ChatListAct extends BasicAct implements GetMemberBindedListAPI.GetMemberBindedListListener{
    @BindView(R.id.chatlist_list)
    RecyclerView chatlistList;

    private RecyclerView.Adapter adapter;

    private static final String WATCH_LIST = "WATCH_LIST";

    //这是从前面传过来的手表信息
    private List<Member> memberList;
    //这是需要保存的手表的聊天的列表的信息
    private List<ChatListBean> chatList = new ArrayList<>();

    private DatabaseHelper dbHelper;

    LoadingDialog loadingDialog;


    public ChatListAct() {
        super(R.layout.act_chat_list, R.string.title_activity_chat_list, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChatListAct.class);
        context.startActivity(intent);
    }


    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        dbHelper = new DatabaseHelper(this, SPHelper.getString(Builds.SP_USER, "userName"));
        getWatchListData();
        initListView();
        loadingDialog.show();
        new GetMemberBindedListAPI(this);
    }

    /**
     * 初始化列表
     */
    private void initListView() {
        adapter = new WatchChatListAdapter(this,chatList);
        chatlistList.setLayoutManager(new LinearLayoutManager(this));
        chatlistList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        chatlistList.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 将手表信息 添加到另外一个共用list中去
     */
    public void getWatchListData() {
        memberList = dbHelper.selectAllMember();
        for (final Member member : memberList) {
            if (member.getWatchId() != 0) {
                Parcel parcel = Parcel.obtain();
                ChatListBean chatListBean = new ChatListBean(parcel);
                chatListBean.setImg(member.getImage());
                chatListBean.setMemberId(member.getMemberId());
                chatListBean.setName(member.getRealName());
                chatListBean.setWatch_id(member.getWatchId() + "");
                chatList.add(chatListBean);
            }
        }
    }

    /**
     * 查询用户已绑定腕表成员及最后消息时间
     * @param content
     */
    @Override
    public void getMemberBindedListSuccess(JSONArray content) {

        for (int i = 0;i < content.length(); i++){
            try {
                JSONObject jsonObject = content.getJSONObject(i);
                for (int j = 0; j < chatList.size(); j++){
                    if (JSONUtils.getInt(jsonObject,"memberId") == chatList.get(j).getMemberId()){
                        chatList.get(j).setMessageNum(JSONUtils.getInt(jsonObject,"readCount"));
                        chatList.get(j).setLastSendTime(JSONUtils.getString(jsonObject,"lastchatTime"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
        loadingDialog.dismiss();
    }

    @Override
    public void getMemberBindedListError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
