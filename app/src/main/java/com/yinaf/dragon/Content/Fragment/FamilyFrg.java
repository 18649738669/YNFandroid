package com.yinaf.dragon.Content.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yinaf.dragon.Content.Activity.AnAlarmAct;
import com.yinaf.dragon.Content.Activity.CaptureAct;
import com.yinaf.dragon.Content.Activity.ChatListAct;
import com.yinaf.dragon.Content.Activity.FamilySettingAct;
import com.yinaf.dragon.Content.Activity.FriendsCircleAct;
import com.yinaf.dragon.Content.Activity.HealthRecordsAct;
import com.yinaf.dragon.Content.Activity.HomeAct;
import com.yinaf.dragon.Content.Activity.MembersIntegralAct;
import com.yinaf.dragon.Content.Activity.PlaceAct;
import com.yinaf.dragon.Content.Activity.ServicePackageAct;
import com.yinaf.dragon.Content.Activity.TimeDateAct;
import com.yinaf.dragon.Content.Activity.family_set.MemberSetAct;
import com.yinaf.dragon.Content.Activity.family_set.model.WatchesSetModel;
import com.yinaf.dragon.Content.Adapter.StepAdapter;
import com.yinaf.dragon.Content.Bean.Member;
import com.yinaf.dragon.Content.Bean.Step;
import com.yinaf.dragon.Content.Db.DatabaseHelper;
import com.yinaf.dragon.Content.Dialog.ChooseBindingDialog;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.BindWatchAPI;
import com.yinaf.dragon.Content.Net.GetMemberAPI;
import com.yinaf.dragon.Content.Net.GetWatchBatteryAPI;
import com.yinaf.dragon.Content.Net.WatchesSetAPI;
import com.yinaf.dragon.Content.Receiver.RefreshMemberListReceiver;
import com.yinaf.dragon.Content.Receiver.RefreshMemberReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.App;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicFrg;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;
import com.yinaf.dragon.Tool.Utils.UIUtils;
import com.yinaf.dragon.externalDemo.utils.CommonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by long on 2018-4-16.
 * 功能：家人的fragment
 */

public class FamilyFrg extends BasicFrg implements GetWatchBatteryAPI.GetWatchBatteryListener,
        BindWatchAPI.BindWatchAPIListener,RefreshMemberReceiver.RefreshMemberListener,
        RefreshMemberListReceiver.RefreshMemberListListener,GetMemberAPI.GetMemberAPIListener,
        WatchesSetAPI.WatchesSetListener{


    @BindView(R.id.family_iv_message)
    ImageView familyIvMessage;
    @BindView(R.id.family_tv_title)
    TextView familyTvTitle;
    @BindView(R.id.family_iv_scan_code)
    ImageView familyIvScanCode;
    @BindView(R.id.family_iv_switch_left)
    ImageView familyIvSwitchLeft;
    @BindView(R.id.family_iv_electricity_num)
    ImageView familyIvElectricityNum;
    @BindView(R.id.family_rounded_img)
    RoundedImageView familyRoundedImg;
    @BindView(R.id.family_iv_switch_right)
    ImageView familyIvSwitchRight;
    @BindView(R.id.family_tv_name)
    TextView familyTvName;
    @BindView(R.id.family_lv_my_step)
    ListView familyLvMyStep;
    @BindView(R.id.family_tv_real_time_date)
    TextView familyTvRealTimeDate; //实时数据
    @BindView(R.id.family_tv_package)
    TextView familyTvPackage; //资讯
    @BindView(R.id.family_tv_friends_circle)
    TextView familyTvFriendsCircle;  //亲友圈
    @BindView(R.id.family_tv_place)
    TextView familyTvPlace;  //定位
    @BindView(R.id.family_tv_members_integral)
    TextView familyTvMembersIntegral;  //成员积分
    @BindView(R.id.family_tv_health_records)
    TextView familyTvHealthRecords;  //健康档案
    @BindView(R.id.family_tv_an_alarm)
    TextView familyTvAnAlarm;  //警报提示
    @BindView(R.id.family_tv_set_up_the)
    TextView familyTvSetUpThe;  //设置
    Unbinder unbinder;

    List<Step> stepList = new ArrayList<Step>(); //动态列表
    StepAdapter stepAdapter; //动态列表适配器

    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;

    //当前索引位置
    int index = 0;
    //是否需要轮播标志
    boolean isContinue = true;
    //定时器，用于实现轮播
    Timer timer;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    index += 1;
                    if (familyLvMyStep.getCount() > 0 && index >= familyLvMyStep.getCount()) {
                        index = 0;
                    }
                    familyLvMyStep.smoothScrollToPosition(index);
            }
        }
    };

    public DatabaseHelper dbHelper;
    public List<Member> memberList;//存放成员信息的list
    public int memberIndex = 0;//标记当前显示的成员信息的下标
    private ImageLoader imageLoader;
    LoadingDialog loadingDialog;
    WatchesSetModel.ObjBean watchData = new WatchesSetModel.ObjBean();

    RefreshMemberReceiver refreshMemberReceiver;//刷新成员广播
    RefreshMemberListReceiver refreshMemberListReceiver;//刷新成员广播

    /**
     * 需要的权限
     */
    private static final String[] INITIAL_PERMS={
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private static final int INITIAL_REQUEST=1337;

    public FamilyFrg() {
        super(R.layout.frg_family);
    }

    @Override
    public void initView(View view) {
        loadingDialog = LoadingDialog.showDialog(getContext());
        initListView();
        dbHelper = new DatabaseHelper(getContext(), SPHelper.getString(Builds.SP_USER, "userName"));
        imageLoader = App.getImageLoader();
        initRoundedImg();
        refreshMemberReceiver = new RefreshMemberReceiver(this);
        RefreshMemberReceiver.register(getContext(),refreshMemberReceiver);
        refreshMemberListReceiver = new RefreshMemberListReceiver(this);
        RefreshMemberListReceiver.register(getContext(),refreshMemberListReceiver);

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

    @OnClick({R.id.family_iv_message, R.id.family_iv_scan_code, R.id.family_iv_switch_left,
            R.id.family_rounded_img, R.id.family_iv_switch_right,
            R.id.family_tv_real_time_date, R.id.family_tv_package, R.id.family_tv_friends_circle,
            R.id.family_tv_place, R.id.family_tv_members_integral, R.id.family_tv_health_records,
            R.id.family_tv_an_alarm, R.id.family_tv_set_up_the, R.id.family_iv_phone})
    public void onViewClicked(View view) {
        getMemberList();
        if (dbHelper.selectAllMember().size() == 0) {
            ChooseBindingDialog.startActivity(getContext(),0);
        }else{
            SPHelper.save(Builds.SP_USER,"memberId",memberList.get(memberIndex).getMemberId());
            SPHelper.save(Builds.SP_USER,"memberRealName",memberList.get(memberIndex).getRealName());
            SPHelper.save(Builds.SP_USER,"watchId",memberList.get(memberIndex).getWatchId());

            switch (view.getId()) {
                case R.id.family_iv_message: //消息
                    if (memberList.get(memberIndex).getWatchId() == 0 || memberList.get(memberIndex).getWatchId() == -1){
                        ChooseBindingDialog.startActivity(getContext(),1);
                    }else {
                        ChatListAct.startActivity(getContext());
                    }
                    break;
                case R.id.family_iv_scan_code: //扫码

                    boolean permission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED ;

                    if (!permission ) {

                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                        }else{
                            TipUtils.showTip("请先在手机的设置中心，设置允许访问相机、相册的权限");
                        }
                    }else {
                        //打开二维码扫描界面
                        if (CommonUtil.isCameraCanUse()) {
                            Intent intent = new Intent(getContext(), CaptureAct.class);
                            intent.putExtra("isWatchId",0);
                            intent.putExtra("memberId", memberList.get(memberIndex).getMemberId());
                            startActivityForResult(intent, REQUEST_CODE);
                        } else {
                            Toast.makeText(getContext(), "请打开此应用的摄像头权限！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.family_iv_switch_left: //左边按钮
                    if (memberList.size() > 1) {
                        if (memberIndex == 0) {
                            memberIndex = memberList.size() - 1;
                            initRoundedImg();
                        } else {
                            memberIndex -= 1;
                            initRoundedImg();
                        }
                    }else {
                        TipUtils.showTip("仅此一个，无法切换");
                    }
                    break;
                case R.id.family_rounded_img: //头像
                    //成员设置
                    Intent intent1 = new Intent(getContext(), MemberSetAct.class);
                    intent1.putExtra("watchId",memberList.get(memberIndex).getWatchId());
                    intent1.putExtra("memberId", memberList.get(memberIndex).getMemberId());
                    startActivity(intent1);

                    break;
                case R.id.family_iv_switch_right:  //右边按钮
                    if (memberList.size() > 1) {
                        if (memberIndex == memberList.size() - 1) {
                            memberIndex = 0;
                            initRoundedImg();
                        } else {
                            memberIndex += 1;
                            initRoundedImg();
                        }
                    }else {
                        TipUtils.showTip("仅此一个，无法切换");
                    }
                    break;
                case R.id.family_tv_real_time_date:  //实时数据
                    TimeDateAct.startActivity(getContext(), memberList.get(memberIndex).getMemberId() + "");
                    break;
                case R.id.family_tv_package:  //服务套餐
                    if (memberList.get(memberIndex).getWatchId() == 0 || memberList.get(memberIndex).getWatchId() == -1){
                        ChooseBindingDialog.startActivity(getContext(),1);
                    }else {
                        ServicePackageAct.startActivity(getContext(), memberList.get(memberIndex).getMemberId() + "");
                    }
                    break;
                case R.id.family_tv_friends_circle:  //亲友圈
                    FriendsCircleAct.startActivity(getContext());
                    break;
                case R.id.family_tv_place:  //定位
                    boolean permission1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED ;

                    if (!permission1 ) {

                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                        }else{
                            TipUtils.showTip("请先在手机的设置中心，设置允许访问位置信息的权限");
                        }
                    }else {
                        if (memberList.get(memberIndex).getWatchId() == 0 || memberList.get(memberIndex).getWatchId() == -1) {
                            ChooseBindingDialog.startActivity(getContext(), 1);
                        } else {
                            PlaceAct.startActivity(getContext(), memberList.get(memberIndex).getMemberId() + "", memberList.get(memberIndex).getRealName());
                        }
                    }
                    break;
                case R.id.family_tv_members_integral:  //成员积分
                    MembersIntegralAct.startActivity(getContext(),memberList.get(memberIndex).getMemberId()+"");
                    break;
                case R.id.family_tv_health_records: //健康档案
                    HealthRecordsAct.startActivity(getContext(), memberList.get(memberIndex).getMemberId());
                    break;
                case R.id.family_tv_an_alarm: //报警提示
                    AnAlarmAct.startActivity(getContext(), memberList.get(memberIndex).getMemberId() + "");
                    break;
                case R.id.family_tv_set_up_the:  //设置

                    if (memberList.get(memberIndex).getWatchId() == 0 || memberList.get(memberIndex).getWatchId() == -1){
                        ChooseBindingDialog.startActivity(getContext(),1);
                    }else {
                        Intent intent = new Intent(getContext(), FamilySettingAct.class);
                        int memberId = memberList.get(memberIndex).getMemberId();
                        int watchId = memberList.get(memberIndex).getWatchId();
                        if (memberId <= 0) {
                            return;
                        }
                        intent.putExtra("memberId", memberId);
                        intent.putExtra("watchId", watchId);
                        startActivity(intent);
                    }

                    break;
                case R.id.family_iv_phone: //拨打电话
                    String phone = watchData.getPhone();
                    if (memberList.get(memberIndex).getWatchId() == 0 || memberList.get(memberIndex).getWatchId() == -1){
                        ChooseBindingDialog.startActivity(getContext(),1);
                    }else  if(phone == null || phone.equals("")){
                        ChooseBindingDialog.startActivity(getContext(),2);
                    }else {
                        dialPhone();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 初始化圆形头像数据
     */
    private void initRoundedImg() {
        getMemberList();
        memberList = dbHelper.selectAllMember();
        if (memberList.size() > 0){
            if (memberList.get(memberIndex).getImage() == null ||
                    memberList.get(memberIndex).getImage().equals("")){
                familyRoundedImg.setImageResource(R.drawable.rounded_img2);
            }else {
                imageLoader.displayImage(memberList.get(memberIndex).getImage(), familyRoundedImg);
            }
            familyTvName.setText(memberList.get(memberIndex).getRealName());
            loadingDialog.show();
            new GetWatchBatteryAPI(this,memberList.get(memberIndex).getMemberId()+"");
            new WatchesSetAPI(this,memberList.get(memberIndex).getMemberId());
            SPHelper.save(Builds.SP_USER,"memberId",memberList.get(memberIndex).getMemberId());
            SPHelper.save(Builds.SP_USER,"memberRealName",memberList.get(memberIndex).getRealName());
            SPHelper.save(Builds.SP_USER,"watchId",memberList.get(memberIndex).getWatchId());
        }
    }


    /**
     * 初始化动态列表
     */
    private void initListView() {

        for (int i = 0; i < 2; i++) {
            Step step = new Step();
            step.setImage(R.drawable.step);
            step.setTitle("我的动态" + (i + 1));
            step.setContent("今天走了5000步");
            step.setTime("小明在" + StringUtils.getHourSecond(StringUtils.getCurrentTimeStamp()) + "发布了动态");
            stepList.add(step);
        }

        stepAdapter = new StepAdapter(stepList);
        familyLvMyStep.setAdapter(stepAdapter);
        familyLvMyStep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int k = position + 1;
                TipUtils.showTip("这是第" + k + "条动态");
            }
        });

        timer = new Timer();//创建Timer对象
        //执行定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //首先判断是否需要轮播，是的话我们才发消息
                if (isContinue) {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }, 5000, 5000);//延迟5秒，每隔5秒发一次消息

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来
            loadingDialog.show();
            new BindWatchAPI(this,memberList.get(memberIndex).getMemberId(),scanResult);
        }
    }

    /**
     * 获取成员手表电量
     * @param content
     */
    @Override
    public void getWatchBatterySuccess(JSONObject content) {

        int obj = JSONUtils.getInt(content,"obj");
        if (obj == 0){
            familyIvElectricityNum.setImageResource(R.drawable.electricity_num_0);
        }else if (obj >= 1 && obj <= 25){
            familyIvElectricityNum.setImageResource(R.drawable.electricity_num_1);
        }else if (obj > 25 && obj <= 50){
            familyIvElectricityNum.setImageResource(R.drawable.electricity_num_2);
        }else if (obj > 50 && obj <= 75){
            familyIvElectricityNum.setImageResource(R.drawable.electricity_num_3);
        }else if (obj > 75 && obj <= 100){
            familyIvElectricityNum.setImageResource(R.drawable.electricity_num_4);
        }
        loadingDialog.dismiss();

    }

    @Override
    public void getWatchBatteryError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        RefreshMemberReceiver.unregister(getContext(),refreshMemberReceiver);
        RefreshMemberListReceiver.unregister(getContext(),refreshMemberListReceiver);
    }

    /**
     * 绑定腕表接口
     * @param content
     */
    @Override
    public void bindWatchSuccess(JSONObject content) {

        TipUtils.showTip("绑定成功");
        loadingDialog.dismiss();
        memberList.clear();
        initRoundedImg();
    }

    @Override
    public void bindWatchError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 接收到刷新成员的广播
     */
    @Override
    public void receiverRefreshMember() {
        memberList.clear();
        initRoundedImg();
    }
    /**
     * 接收到刷新成员list的广播
     */
    @Override
    public void receiverRefreshMemberList() {
        memberList.clear();
        initRoundedImg();
    }

    /**
     * 获取刷新成员列表数据
     */
    public void getMemberList(){
        loadingDialog.show();
        new GetMemberAPI(this);
    }

    /**
     * 获取用户的所有成员
     * @param content
     */
    @Override
    public void getMemberSuccess(JSONArray content) {
        dbHelper.deleteMemberData();
        try {
            for (int i = 0 ; i < content.length() ; i++){
                JSONObject jsonObject = content.getJSONObject(i);
                if (dbHelper.isMemberExists(JSONUtils.getInt(jsonObject,"memberId"))){
                    //本地已存在此成员信息,则更新成员信息
                    Member member = dbHelper.selectMemberByMemberId(JSONUtils.getInt(jsonObject,"memberId"));
                    if (jsonObject.getString("watchId").equals("")){
                        member.setWatchId(-1);
                    }else {
                        member.setWatchId(JSONUtils.getInt(jsonObject,"watchId"));
                    }
                    member.setIsLead(JSONUtils.getInt(jsonObject,"isLead"));
                    member.setImage(JSONUtils.getString(jsonObject,"image"));
                    member.setRealName(JSONUtils.getString(jsonObject,"realName"));
                    member.setMemberNum(JSONUtils.getString(jsonObject,"memberNum"));
                    member.setRela(JSONUtils.getString(jsonObject,"rela"));
                    dbHelper.updateMember(member);

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
                }
            }
            memberList.clear();
            memberList = dbHelper.selectAllMember();
            loadingDialog.dismiss();
            new WatchesSetAPI(this,memberList.get(memberIndex).getMemberId());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMemberError(long code, String msg) {
        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 检查应用程序的权限,并拨打电话
     */
    public void dialPhone(){

        boolean permission = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED ;

        if (!permission ) {

            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
            }else{
                TipUtils.showTip("请先在手机的设置中心，设置允许拨打电话的权限");
            }

        }else{
            String phone = watchData.getPhone();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setData(Uri.parse("tel:" + phone));
            startActivity(callIntent);
        }
    }

    /**
     * 获取手表信息
     * @param drugSetModel
     */
    @Override
    public void footstepSetSuccess(WatchesSetModel drugSetModel) {
        if (drugSetModel != null && drugSetModel.getObj() != null) {
             watchData = drugSetModel.getObj();
        }
    }

    @Override
    public void footstepSetError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }
}
