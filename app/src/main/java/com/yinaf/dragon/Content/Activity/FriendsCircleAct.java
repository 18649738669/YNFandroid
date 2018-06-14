package com.yinaf.dragon.Content.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yinaf.dragon.Content.Adapter.MemberAdapter;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.DeleteMemberAPI;
import com.yinaf.dragon.Content.Net.GetMemberAPI;
import com.yinaf.dragon.Content.Receiver.RefreshMemberListReceiver;
import com.yinaf.dragon.Content.Receiver.RefreshMemberReceiver;
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
import butterknife.OnClick;

/**
 * Created by long on 2018/4/20.
 * 功能：亲友圈页面
 */

public class FriendsCircleAct extends BasicAct implements RefreshMemberReceiver.RefreshMemberListener ,
        DeleteMemberAPI.DeleteMemberListener,GetMemberAPI.GetMemberAPIListener{

    @BindView(R.id.tool_bar_friends_circle_btn_back)
    RelativeLayout toolBarFriendsCircleBtnBack;
    @BindView(R.id.tool_bar_friends_circle_right_text)
    RelativeLayout toolBarFriendsCircleRightText;
    @BindView(R.id.friends_circle_lv)
    RecyclerView friendsCircleLv;
    @BindView(R.id.friends_circle_tv_add_family)
    TextView friendsCircleTvAddFamily;
    @BindView(R.id.friends_circle_tv_binding_family)
    TextView friendsCircleTvBindingFamily;

    List<Member> memberList = new ArrayList<Member>(); //成员信息列表
    MemberAdapter memberAdapter; //成员信息列表适配器
    DatabaseHelper dbHelper;
    LoadingDialog loadingDialog;

    RefreshMemberReceiver refreshMemberReceiver;//刷新成员广播

    public FriendsCircleAct() {
        super(R.layout.act_friends_circle, R.string.title_activity_friends_circle, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, FriendsCircleAct.class);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        toolBarFriendsCircleRightText.setVisibility(View.GONE);
        loadingDialog = LoadingDialog.showDialog(this);
        dbHelper = new DatabaseHelper(this, SPHelper.getString(Builds.SP_USER,"userName"));
        refreshMemberReceiver = new RefreshMemberReceiver(this);
        RefreshMemberReceiver.register(this,refreshMemberReceiver);
        initListView();

    }

    /**
     * 初始化listview
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListView() {

        memberList = dbHelper.selectAllMember();
        memberAdapter = new MemberAdapter(this,memberList);
        memberAdapter.setOnDelListener(new MemberAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < memberList.size()) {
                    loadingDialog.show();
                    new DeleteMemberAPI(FriendsCircleAct.this,memberList.get(pos).getMemberId() + "");
                    dbHelper.deleteMemberByMemberId(memberList.get(pos).getMemberId());
                    memberList.remove(pos);
                    memberAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

        });
        //添加分割线
        friendsCircleLv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        friendsCircleLv.setLayoutManager(new LinearLayoutManager(this));
        friendsCircleLv.setAdapter(memberAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tool_bar_friends_circle_btn_back, R.id.tool_bar_friends_circle_right_text, R.id.friends_circle_tv_add_family, R.id.friends_circle_tv_binding_family})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_btn_back:
                finish();
                break;
            case R.id.tool_bar_friends_circle_right_text:
                break;
            case R.id.friends_circle_tv_add_family:
                AddFamilyAct.startActivity(this,0);
                break;
            case R.id.friends_circle_tv_binding_family:
                BindingFamilyAct.startActivity(this);
                break;
        }
    }

    /**
     * 接受到刷新成员广播
     */
    @Override
    public void receiverRefreshMember() {

        loadingDialog.show();
        new GetMemberAPI(this);

    }

    /**
     * 删除成员
     * @param content
     */
    @Override
    public void deleteMemberSuccess(JSONArray content) {

        loadingDialog.dismiss();
        TipUtils.showTip("删除成功！");
        RefreshMemberListReceiver.send(this);


    }

    @Override
    public void deleteMemberError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 获取用户的所有成员
     * @param content
     */
    @Override
    public void getMemberSuccess(JSONArray content) {
        try {
            for (int i = 0 ; i < content.length() ; i++){
                JSONObject jsonObject = content.getJSONObject(i);
                if (dbHelper.isMemberExists(JSONUtils.getInt(jsonObject,"memberId"))){
                    //本地已存在此成员信息,则更新成员信息
                    Member member = dbHelper.selectMemberByMemberId(JSONUtils.getInt(jsonObject,"memberId"));
                    member.setWatchId(JSONUtils.getInt(jsonObject,"watchId"));
                    member.setIsLead(JSONUtils.getInt(jsonObject,"isLead"));
                    member.setImage(JSONUtils.getString(jsonObject,"image"));
                    member.setRealName(JSONUtils.getString(jsonObject,"realName"));
                    member.setMemberNum(JSONUtils.getString(jsonObject,"memberNum"));
                    member.setRela(JSONUtils.getString(jsonObject,"rela"));
                    dbHelper.updateMember(member);
//                    memberList.clear();
                    memberList = dbHelper.selectAllMember();
                    memberAdapter.setItemList(memberList);
                    memberAdapter.notifyDataSetChanged();
                }else {
                    //本地不存在此成员信息，则插入一条成员信息
                    Member member = new Member();
                    member.setMemberId(JSONUtils.getInt(jsonObject,"memberId"));
                    member.setWatchId(JSONUtils.getInt(jsonObject,"watchId"));
                    member.setIsLead(JSONUtils.getInt(jsonObject,"isLead"));
                    member.setImage(JSONUtils.getString(jsonObject,"image"));
                    member.setRealName(JSONUtils.getString(jsonObject,"realName"));
                    member.setMemberNum(JSONUtils.getString(jsonObject,"memberNum"));
                    member.setRela(JSONUtils.getString(jsonObject,"rela"));
                    dbHelper.insertNewMember(member);
//                    memberList.clear();
                    memberList = dbHelper.selectAllMember();
                    memberAdapter.setItemList(memberList);
                    memberAdapter.notifyDataSetChanged();
                }
            }
            loadingDialog.dismiss();
            RefreshMemberListReceiver.send(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMemberError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefreshMemberReceiver.unregister(this,refreshMemberReceiver);
    }
}
