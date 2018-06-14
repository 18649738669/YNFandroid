package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.adapter.AddressBookSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.adapter.DrugSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.DrugSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddressBookSetAPI;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通讯录列表
 */
public class AddressBookSetAct extends BasicAct implements AddressBookSetAPI.AddressBookSetListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    private int memberId;
    private AddressBookSetAdapter adapter;
    private LoadingDialog loadingDialog;
    private AddressBookSetModel model;

    public AddressBookSetAct() {
        super(R.layout.address_book_set_act, R.string.family_address_book_set,R.string.family_add_address_book_set
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
            String string = SPHelper.getString(Builds.SP_USER, memberId + "AddressBook");
            if (string!=null&&!TextUtils.isEmpty(string)){
                model = JSONUtils.parseJson(string, AddressBookSetModel.class);
                adapter = new AddressBookSetAdapter(getApplicationContext(),this, model.getObj());
                recyclerView.setAdapter(adapter);
            }else {
                TipUtils.showTip("请开启网络!");
            }
        }else {
            loadingDialog.show();
            new AddressBookSetAPI(this, memberId);
        }
    }



    @OnClick({R.id.tool_bar_friends_circle_right_text})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tool_bar_friends_circle_right_text:
                //添加提醒
                Intent intent = new Intent(getApplicationContext(), AddressBookSetAddAct.class);
                intent.putExtra("type",0);
                intent.putExtra("memberId",memberId);
                startActivity(intent);
                break;

        }
    }



    @Override
    public void addressBookSetSuccess(AddressBookSetModel model) {
        loadingDialog.dismiss();
        List<AddressBookSetModel.ObjBean> obj = model.getObj();

        //显示提醒列表
        if (obj.size() > 0) {
            if (adapter == null) {
                adapter = new AddressBookSetAdapter(getApplicationContext(),this, obj);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.getDatas().clear();
                adapter.getDatas().addAll(obj);
            }
            adapter.notifyDataSetChanged();
            tv_empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else if (adapter==null){
            tv_empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }


    }
    @Subscribe()
    public void onMessageEvent(DataEventBus event) {
        new AddressBookSetAPI(this, memberId);

    }

    @Override
    public void addressBookSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
