package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Adapter.SecurityFenceAdapter;
import com.yinaf.dragon.Content.Bean.SecurityFence;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.DelWatchRailAPI;
import com.yinaf.dragon.Content.Net.GetRailAPI;
import com.yinaf.dragon.Content.Receiver.RefreshSecurityFenceReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
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
 * Created by long on 2018/05/04.
 * 功能：展示安全围栏list的页面
 */

public class SecurityFenceAct extends BasicAct implements GetRailAPI.GetRailListener,
        RefreshSecurityFenceReceiver.RefreshSecurityFenceListener ,DelWatchRailAPI.DelWatchRailListener{


    @BindView(R.id.tool_bar_friends_circle_btn_back)
    RelativeLayout toolBarFriendsCircleBtnBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tool_bar_friends_circle_right_text)
    RelativeLayout toolBarFriendsCircleRightText;
    @BindView(R.id.security_fence_lv)
    RecyclerView securityFenceLv;
    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;

    LoadingDialog loadingDialog;
    List<SecurityFence> securityFenceList = new ArrayList<SecurityFence>(); //安全围栏列表
    SecurityFenceAdapter securityFenceAdapter;//安全围栏列表适配器

    RefreshSecurityFenceReceiver refreshSecurityFenceReceiver;//刷新安全围栏列表广播



    public SecurityFenceAct() {
        super(R.layout.act_security_fence_list, R.string.title_activity_security_fence, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId) {
        Intent intent = new Intent(context, SecurityFenceAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        tvRight.setText("添加");
        toolBarFriendsCircleTitle.setText(R.string.title_activity_security_fence);
        loadingDialog = LoadingDialog.showDialog(this);
        refreshSecurityFenceReceiver = new RefreshSecurityFenceReceiver(this);
        RefreshSecurityFenceReceiver.register(this,refreshSecurityFenceReceiver);
        initListView();
        loadingDialog.show();
        new GetRailAPI(this, getIntent().getStringExtra("memberId"));


    }

    /**
     * 初始化listview
     */
    private void initListView() {
        securityFenceAdapter = new SecurityFenceAdapter(this, securityFenceList);
        securityFenceAdapter.setOnDelListener(new SecurityFenceAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < securityFenceList.size()) {
                    loadingDialog.show();
                    new DelWatchRailAPI(SecurityFenceAct.this,securityFenceList.get(pos).getRailId());
                    securityFenceList.remove(pos);
                    securityFenceAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

        });
        //添加分割线
        securityFenceLv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        securityFenceLv.setLayoutManager(new LinearLayoutManager(this));
        securityFenceLv.setAdapter(securityFenceAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tool_bar_friends_circle_btn_back, R.id.tool_bar_friends_circle_right_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_btn_back:
                finish();
                break;
            case R.id.tool_bar_friends_circle_right_text:
                SetSecurityFenceAct.startActivity(this,getIntent().getStringExtra("memberId"),2);
                break;
        }
    }

    /**
     * 获取成员安全围栏
     *
     * @param content
     */
    @Override
    public void getRailSuccess(JSONArray content) {
        securityFenceList.clear();
        try {
            for (int i = 0; i < content.length(); i++) {
                JSONObject jsonObject = content.getJSONObject(i);
                SecurityFence securityFence = new SecurityFence();
                securityFence.setAddress(JSONUtils.getString(jsonObject, "address"));
                securityFence.setDeviceTime(JSONUtils.getString(jsonObject, "deviceTime"));
                securityFence.setLastAction(JSONUtils.getString(jsonObject, "lastAction"));
                securityFence.setLatitude(JSONUtils.getString(jsonObject, "lat"));
                securityFence.setLongitude(JSONUtils.getString(jsonObject, "lon"));
                securityFence.setName(JSONUtils.getString(jsonObject, "name"));
                securityFence.setRadius(JSONUtils.getInt(jsonObject, "radius"));
                securityFence.setWatchId(JSONUtils.getInt(jsonObject, "watchId"));
                securityFence.setRailId(JSONUtils.getInt(jsonObject, "railId"));
                securityFenceList.add(securityFence);
            }
            securityFenceAdapter.setItemList(securityFenceList);
            securityFenceAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadingDialog.dismiss();
    }

    @Override
    public void getRailError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void receiverRefreshSecurityFence() {
        loadingDialog.show();
        new GetRailAPI(this, getIntent().getStringExtra("memberId"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefreshSecurityFenceReceiver.unregister(this,refreshSecurityFenceReceiver);
    }

    /**
     * 删除安全围栏
     */
    @Override
    public void delWatchRailSuccess() {
        new GetRailAPI(this, getIntent().getStringExtra("memberId"));
    }

    @Override
    public void delWatchRailError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
