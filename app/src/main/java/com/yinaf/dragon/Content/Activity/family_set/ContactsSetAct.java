package com.yinaf.dragon.Content.Activity.family_set;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.family_set.adapter.AddressBookSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.adapter.AppSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.adapter.ContactsSetAdapter;
import com.yinaf.dragon.Content.Activity.family_set.model.AddressBookSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.AppSetModel;
import com.yinaf.dragon.Content.Activity.family_set.model.ContactsSetModel;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddressBookSetAPI;
import com.yinaf.dragon.Content.Net.AppSetAPI;
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

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 紧急联系人
 */
public class ContactsSetAct extends BasicAct implements ContactsSetAPI.ContactsSetListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty)
    TextView tv_empty;
    private int memberId;
    private ContactsSetAdapter adapter;
    private LoadingDialog loadingDialog;

    public ContactsSetAct() {
        super(R.layout.contacts_set_act, R.string.family_contacts_set, R.string.family_add_address_book_set
                , true, TOOLBAR_TYPE_TITLE, R.color.common_blue);

    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        loadingDialog = LoadingDialog.showDialog(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        memberId = getIntent().getIntExtra("memberId", 0);
        //网络请求,获取药物提醒列表
        if (!NetworkUtils.isNetworkConnect(getApplicationContext())){
            String string = SPHelper.getString(Builds.SP_USER, memberId + "Contacts");
            if (string!=null&&!TextUtils.isEmpty(string)){
                ContactsSetModel model = JSONUtils.parseJson(string, ContactsSetModel.class);
                adapter = new ContactsSetAdapter(getApplicationContext(), this, model.getObj());
                recyclerView.setAdapter(adapter);
            }else {
                TipUtils.showTip("请开启网络!");
            }
        }else {
            loadingDialog.dismiss();
            new ContactsSetAPI(this, memberId);
        }


    }


    @OnClick({R.id.tool_bar_friends_circle_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_right_text:
                //添加提醒
                Intent intent = new Intent(getApplicationContext(), ContactsSetAddAct.class);
                intent.putExtra("type", 0);
                intent.putExtra("memberId", memberId);
                startActivity(intent);
                break;

        }
    }

    @Subscribe
    public void onEven(DataEventBus eventBus) {
        new ContactsSetAPI(this, memberId);
    }

    @Override
    public void contactsSetSuccess(ContactsSetModel model) {
        List<ContactsSetModel.ObjBean> obj = model.getObj();
        loadingDialog.dismiss();
        //显示提醒列表
        if (obj.size() > 0) {
            if (adapter==null){
                adapter = new ContactsSetAdapter(getApplicationContext(), this, obj);
                recyclerView.setAdapter(adapter);
            }else {
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

    @Override
    public void contactsSetError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

