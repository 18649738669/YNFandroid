package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.adapter.AddressBookSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.adapter.AddressSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddressBookSetAPI;
import com.yinaf.dragon.Content.Net.AddressSetAPI;
import com.yinaf.dragon.Content.Net.ContactsSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地址管理列表
 */
public class AddressSetAct extends BasicAct implements AddressSetAPI.AddressSetListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    private AddressSetAdapter drugSetAdapter;
    private int memberId;
    private LoadingDialog loadingDialog;

    public AddressSetAct() {
        super(R.layout.address_set_act, R.string.family_address_set, R.string.family_add_address_set
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);

        loadingDialog = LoadingDialog.showDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //网络请求,获取药物提醒列表
        memberId = getIntent().getIntExtra("memberId", 0);

        if (!NetworkUtils.isNetworkConnect(getApplicationContext())){
            String string = SPHelper.getString(Builds.SP_USER, memberId + "Address");
            if (string!=null&&!TextUtils.isEmpty(string)){
                AddressSetModel   model = JSONUtils.parseJson(string, AddressSetModel.class);
                drugSetAdapter = new AddressSetAdapter(getApplicationContext()
                        , this,model.getObj());
                recyclerView.setAdapter(drugSetAdapter);
            }else {
                TipUtils.showTip("请开启网络!");
            }
        }else {
            loadingDialog.show();
            new AddressSetAPI(this, memberId);
        }


    }

    @Override
    public void addressSetSuccess(AddressSetModel drugSetModel) {
        List<AddressSetModel.ObjBean> obj = drugSetModel.getObj();

        if (obj.size() > 0) {
            if (drugSetAdapter == null) {
                drugSetAdapter = new AddressSetAdapter(getApplicationContext(), this, obj);
                recyclerView.setAdapter(drugSetAdapter);

            } else if (drugSetAdapter.getDatas() != null && drugSetAdapter.getDatas().size() > 0) {
                drugSetAdapter.getDatas().clear();
                drugSetAdapter.getDatas().addAll(obj);
            }
            drugSetAdapter.notifyDataSetChanged();
            tv_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            tv_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        //显示提醒列表
        loadingDialog.dismiss();
    }

    @OnClick({R.id.tool_bar_friends_circle_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_right_text:
                //添加地址
                Intent intent = new Intent(getApplicationContext(), AddressAddSetAct.class);
                intent.putExtra("type", 0);
                intent.putExtra("memberId",memberId);
                startActivity(intent);
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DataEventBus eventBus) {
        new AddressSetAPI(this, memberId);
    }

    @Override
    public void addressSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}