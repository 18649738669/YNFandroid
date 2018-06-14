package com.yinaf.dragon.Content.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.YinafAct;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicFrg;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by long on 2018/05/16.
 * 功能：服务页面
 */

public class ServiceFrg extends BasicFrg {


    @BindView(R.id.service_tv_yinafu)
    TextView serviceTvYinafu;
    @BindView(R.id.service_tv_shopping)
    TextView serviceTvShopping;
    @BindView(R.id.service_tv_house)
    TextView serviceTvHouse;
    @BindView(R.id.service_tv_eat)
    TextView serviceTvEat;
    @BindView(R.id.service_tv_doctor)
    TextView serviceTvDoctor;
    @BindView(R.id.service_tv_telemedicine)
    TextView serviceTvTelemedicine;
    @BindView(R.id.service_tv_healthy)
    TextView serviceTvHealthy;
    @BindView(R.id.service_tv_channel)
    TextView serviceTvChannel;
    Unbinder unbinder;

    public ServiceFrg() {
        super(R.layout.frg_service);
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.service_tv_yinafu, R.id.service_tv_shopping, R.id.service_tv_house, R.id.service_tv_eat, R.id.service_tv_doctor, R.id.service_tv_telemedicine, R.id.service_tv_healthy, R.id.service_tv_channel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.service_tv_yinafu:
                YinafAct.startActivity(getContext());
                break;
            case R.id.service_tv_shopping:
                TipUtils.showTip("敬请期待");

                break;
            case R.id.service_tv_house:
                TipUtils.showTip("敬请期待");

                break;
            case R.id.service_tv_eat:
                TipUtils.showTip("敬请期待");

                break;
            case R.id.service_tv_doctor:
                TipUtils.showTip("敬请期待");

                break;
            case R.id.service_tv_telemedicine:
                TipUtils.showTip("敬请期待");

                break;
            case R.id.service_tv_healthy:
                TipUtils.showTip("敬请期待");

                break;
            case R.id.service_tv_channel:
                TipUtils.showTip("敬请期待");

                break;
        }
    }
}
