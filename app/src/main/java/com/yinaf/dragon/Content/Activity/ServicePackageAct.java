package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.amap.api.services.help.Tip;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Dialog.PayDialog;
import com.yinaf.dragon.Content.Dialog.WhetherRenewDialog;
import com.yinaf.dragon.Content.Net.AddPackageOrderAPI;
import com.yinaf.dragon.Content.Net.GetPackageInfoAPI;
import com.yinaf.dragon.Content.Net.ServicePackageRenewAPI;
import com.yinaf.dragon.Content.Utils.AuthResult;
import com.yinaf.dragon.Content.Utils.PayResult;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.DateTimeUtil;
import com.yinaf.dragon.Tool.Utils.DateUtils;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.xml.transform.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/07.
 * 功能：服务续费页面
 */

public class ServicePackageAct extends BasicAct implements GetPackageInfoAPI.GetPackageInfoListener,
        AddPackageOrderAPI.AddPackageOrderListener{
    @BindView(R.id.package_tv_equipment_imei)
    TextView packageTvEquipmentImei;
    @BindView(R.id.package_tv_phone)
    TextView packageTvPhone;
    @BindView(R.id.package_tv_service_expiration_time)
    TextView packageTvServiceExpirationTime;
    @BindView(R.id.package_sp_the_service_time)
    Spinner packageSpTheServiceTime;
    @BindView(R.id.package_tv_due_the_time)
    TextView packageTvDueTheTime;
    @BindView(R.id.package_tv_package)
    TextView packageTvPackage;
    @BindView(R.id.package_tv_service_fee)
    TextView packageTvServiceFee;
    @BindView(R.id.package_tv_combined)
    TextView packageTvCombined;
    @BindView(R.id.package_tv_generate_orders)
    TextView packageTvGenerateOrders;

    LoadingDialog loadingDialog;
    private int mYear, mMonth, mDay;
    private int serviceTime;
    private int oneMonthMoney = 30;
    private int payWay = 0;
    private String netEndService = "", mEndTime;
    private Calendar calendar = Calendar.getInstance();
    private PayDialog payDialog;//支付方式选择框
    private WhetherRenewDialog whetherRenewDialog;//是否续费选择框
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;



    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (resultStatus.equals("9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ServicePackageAct.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ServicePackageAct.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case SDK_AUTH_FLAG:
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (resultStatus.equals("9000") && authResult.getResultCode().equals( "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        TipUtils.showTip("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        TipUtils.showTip("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));
                    }
                    break;
                default:
                    break;
            }
        };
    };
    private IWXAPI api;


    public ServicePackageAct() {
        super(R.layout.act_service_package, R.string.title_activity_service_package, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId) {
        Intent intent = new Intent(context, ServicePackageAct.class);
        intent.putExtra("memberId", memberId);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        initDialog();
        loadingDialog.show();
        api = WXAPIFactory.createWXAPI(this, "wx7e6fb6e371626009");
        new GetPackageInfoAPI(this,getIntent().getStringExtra("memberId"));

    }

    /**
     * 初始化选择弹出框
     */
    public void initDialog(){
        loadingDialog = LoadingDialog.showDialog(this);
        payDialog = new PayDialog(this);
        whetherRenewDialog = new WhetherRenewDialog(this);

        payDialog.setListenerCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payDialog.dismiss();
            }
        });
        payDialog.setListenerWx(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TipUtils.showTip("还未开通微信支付，请选择支付宝支付！");
                if (wxCanPay()){
                    api.registerApp("wx7e6fb6e371626009");
                    payWay = 1;
                    GetPayData();
                }
                payDialog.dismiss();

            }
        });
        payDialog.setListenerZfb(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipUtils.showTip("选择了支付宝支付");
                payWay = 2;
                payDialog.dismiss();
                GetPayData();

            }
        });
        whetherRenewDialog.setListenerCancel(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipUtils.showTip("取消续费");
                whetherRenewDialog.dismiss();
            }
        });
        whetherRenewDialog.setListenerConfirm(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payDialog.show();
                whetherRenewDialog.dismiss();
            }
        });

    }


    private void initSpinner() {
        final ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.service_time, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packageSpTheServiceTime.setAdapter(arrayAdapter);
        packageSpTheServiceTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    serviceTime = 0;
                }else if (position == 1) {
                    serviceTime = 1;
                } else if (position == 2) {
                    serviceTime = 3;
                } else if (position == 3) {
                    serviceTime = 6;
                } else if (position == 4) {
                    serviceTime = 12;
                } else if (position == 5){
                    serviceTime = 24;
                }
                setServiceTime();
                packageTvCombined.setText(oneMonthMoney * serviceTime + getString(R.string.element));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //天数加1
    String startTime() throws ParseException {
        int day = mDay + 1;
        int month = mMonth;
        int year = mYear;
        if (day > DateUtils.getDaysByYearMonth(year, month)) {
            month += 1;
            day = 1;
        }
        if (month > 12) {
            month = 1;
            year += 1;
        }
        return year + "-" + month + "-" + day;
    }

    public void setServiceTime() {
        int serviceY = mYear;
        int serviceM = mMonth + serviceTime;
        int serviceD = mDay;

        if (serviceM > 12 && serviceM < 24) {
            serviceM -= 12;
            serviceY += 1;
        } else if (serviceM > 24) {
            serviceM -= 24;
            serviceY += 2;
        }
        if (serviceD > DateUtils.getDaysByYearMonth(serviceY, serviceM)) {
            serviceM += 1;
            serviceD = 1;
        }
        if (serviceM > 12) {
            serviceM = 1;
            serviceY += 1;
        }

        if (serviceM >= 10 && serviceD >= 10){
            mEndTime = serviceY + "-" + serviceM + "-" + serviceD + "";
        }else if (serviceM < 10 && serviceD >= 10){
            mEndTime = serviceY + "-0" + serviceM + "-" + serviceD + "";
        }else if (serviceM >= 10 && serviceD < 10){
            mEndTime = serviceY + "-" + serviceM + "-0" + serviceD + "";
        }else if (serviceM < 10 && serviceD < 10){
            mEndTime = serviceY + "-0" + serviceM + "-0" + serviceD + "";
        }
        packageTvDueTheTime.setText(mEndTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.package_tv_generate_orders)
    public void onViewClicked() {

        if (serviceTime > 0) {
            //true 结束时间大  false 开始时间大
            if (DateTimeUtil.getTimeExpendMaxMin(netEndService, DateTimeUtil.getNowTime("yyyy-MM-dd"), "yyyy-MM-dd")) {
                payDialog.show();
            } else {
                //弹窗是否续费选择框
                whetherRenewDialog.show();
            }
        }else {
            TipUtils.showTip("请选择服务时长！");
        }
    }

    public void GetPayData(){
        loadingDialog.show();
        new AddPackageOrderAPI(this,Integer.parseInt(getIntent().getStringExtra("memberId")),oneMonthMoney * serviceTime,payWay);
    }

    /**
     * 查询当前服务套餐信息
     * @param content
     */
    @Override
    public void getPackageInfoSuccess(JSONObject content) {

        packageTvPhone.setText(JSONUtils.getString(content,"phone"));
        packageTvEquipmentImei.setText(JSONUtils.getString(content,"imei"));
        packageTvServiceExpirationTime.setText(JSONUtils.getString(content,"endTime"));
        if (!JSONUtils.getString(content,"endTime").equals("")) {
            netEndService = packageTvServiceExpirationTime.getText().toString();
        } else {
            packageTvDueTheTime.setText("");
            netEndService = StringUtils.getYearMonthDay(StringUtils.getCurrentTimeStamp());
        }
        mYear = Integer.parseInt(netEndService.substring(0,4));
        mMonth = Integer.parseInt(netEndService.substring(5,7));
        mDay = Integer.parseInt(netEndService.substring(netEndService.length() - 2));
        initSpinner();
        loadingDialog.dismiss();

    }

    @Override
    public void getPackageInfoError(long code, String msg) {

        TipUtils.showTip(msg);
        loadingDialog.dismiss();
    }

    /**
     * 生成续费套餐订单
     * @param content
     */
    @Override
    public void addPackageOrderSuccess(String content) {
//        Log.e("微信支付",content);
        if (payWay == 1) {//微信支付

            try {
                JSONObject jsonObject = new JSONObject(content);
                PayReq payReq = new PayReq();
                payReq.appId = JSONUtils.getString(jsonObject,"appid");
                payReq.partnerId = JSONUtils.getString(jsonObject,"partnerid");
                payReq.prepayId = JSONUtils.getString(jsonObject,"prepayid");
                payReq.packageValue = "Sign=WXPay";
                payReq.nonceStr = JSONUtils.getString(jsonObject,"noncestr");
                payReq.timeStamp = JSONUtils.getString(jsonObject,"timestamp");
                payReq.sign = JSONUtils.getString(jsonObject,"sign");
                api.sendReq(payReq);
                loadingDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (payWay == 2){//支付宝
            final String orderInfo = content;   // 订单信息
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(ServicePackageAct.this);
                    Map<String, String> result = alipay.payV2(orderInfo, true);

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
            loadingDialog.dismiss();
        }
    }

    @Override
    public void addPackageOrderError(long code, String msg) {
        TipUtils.showTip(msg);
        loadingDialog.dismiss();
    }

    /**
     * 检测微信
     * @return
     */
    private boolean wxCanPay(){

        try {
            if (!api.isWXAppInstalled()){
                TipUtils.showTip("请安装微信！");
                return false;
            }
            if (!api.isWXAppSupportAPI()){
                TipUtils.showTip("微信版本不支持支付！");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            TipUtils.showTip("请安装或者升级微信版本！");
            return false;
        }
        return true;
    }

}
