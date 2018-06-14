package com.yinaf.dragon.Content.Fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Activity.FeedbackAct;
import com.yinaf.dragon.Content.Activity.HelpAct;
import com.yinaf.dragon.Content.Activity.LoginAct;
import com.yinaf.dragon.Content.Activity.MyActivityAct;
import com.yinaf.dragon.Content.Activity.MyAddressSetAct;
import com.yinaf.dragon.Content.Activity.MySettingAct;
import com.yinaf.dragon.Content.Activity.family_set.AddressSetAct;
import com.yinaf.dragon.Content.Receiver.RefreshMemberReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.App;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicFrg;
import com.yinaf.dragon.Tool.DB.SPHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：我的fragment
 */

public class MyFrg extends BasicFrg implements RefreshMemberReceiver.RefreshMemberListener{

    @BindView(R.id.tv_number)
    TextView tv_number;

    @BindView(R.id.family_rounded_img)
    RoundedImageView familyRoundedImg;

    /**
     * 图像加载框架
     */
    private ImageLoader imageLoader;
    private String userName;
    private String sessionId;
    private String realName;
    private String image;
    private String userId;
    RefreshMemberReceiver refreshMemberReceiver;//刷新成员广播

    public MyFrg() {
        super(R.layout.my_frag);
    }

    @Override
    public void initView(View view) {
        imageLoader = App.getImageLoader();
        //用户名
        userName = SPHelper.getString(Builds.SP_USER, "userName");
        //sessionId
        sessionId = SPHelper.getString(Builds.SP_USER, "sessionId");
        //昵称
        realName = SPHelper.getString(Builds.SP_USER, "realName");
        tv_number.setText(realName);
        //头像图片地址
        image = SPHelper.getString(Builds.SP_USER, "image");
        //头像
        if (!TextUtils.isEmpty(image)) {
            imageLoader.displayImage(image, familyRoundedImg);
        }
        //用户 id
        userId = SPHelper.getString(Builds.SP_USER, "userId");
        refreshMemberReceiver = new RefreshMemberReceiver(this);
        RefreshMemberReceiver.register(getContext(),refreshMemberReceiver);
    }

    @OnClick({R.id.tv_address, R.id.tv_indent, R.id.tv_activity, R.id.tv_setting, R.id.tv_help
            , R.id.tv_idea, R.id.tv_login, R.id.family_rounded_img})
    public void onClick(View view) {
        Intent intent ;
        switch (view.getId()) {
            case R.id.family_rounded_img:
                //头像
                intent = new Intent(getContext(), MySettingAct.class);
                startActivity(intent);
                break;
            case R.id.tv_address:
                //常用地址
                intent = new Intent(getContext(), MyAddressSetAct.class);
                intent.putExtra("memberId", userId);
                startActivity(intent);
                break;
            case R.id.tv_indent:
                //我的订单
                break;
            case R.id.tv_activity:
                //我的活动
                startActivity(new Intent(getContext(),MyActivityAct.class));
                break;
            case R.id.tv_setting:
                intent = new Intent(getContext(), MySettingAct.class);
                startActivity(intent);
                //设置
                break;
            case R.id.tv_help:
                //常见帮助
                HelpAct.startActivity(getContext());
                break;
            case R.id.tv_idea:
                FeedbackAct.startActivity(getContext());
                //意见反馈
                break;
            case R.id.tv_login:
                //退出登录
                LoginAct.startActivity(getContext());
                getActivity().finish();
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefreshMemberReceiver.unregister(getContext(),refreshMemberReceiver);
    }

    /**
     * 接收到刷新用户头像的广播
     */
    @Override
    public void receiverRefreshMember() {
        //头像图片地址
        image = SPHelper.getString(Builds.SP_USER, "image");
        //头像
        if (!TextUtils.isEmpty(image)) {
            imageLoader.displayImage(image, familyRoundedImg);
        }
        //昵称
        realName = SPHelper.getString(Builds.SP_USER, "realName");
        tv_number.setText(realName);
    }
}
