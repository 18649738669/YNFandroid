package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.yinaf.dragon.Content.Bean.MapErrorCode;
import com.yinaf.dragon.Content.Bean.SecurityFence;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.AddWatchRailAPI;
import com.yinaf.dragon.Content.Net.UpdateWatchRailAPI;
import com.yinaf.dragon.Content.Receiver.RefreshSecurityFenceReceiver;
import com.yinaf.dragon.Content.Utils.PermissionFail;
import com.yinaf.dragon.Content.Utils.PermissionSuccess;
import com.yinaf.dragon.Content.Utils.PermissionUtils;
import com.yinaf.dragon.Manifest;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.NetworkUtils;
import com.yinaf.dragon.Tool.Utils.StringUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by long on 2018/05/05.
 * 功能：设置安全围栏页面
 *  有两步围栏操作
 * 1.从列表中已有的围栏中进来  展示围栏
 * 2.新添加围栏   先展示手表的定位  手表没有定位的话再展示手机的定位
 */

public class SetSecurityFenceAct extends BasicAct implements GeocodeSearch.OnGeocodeSearchListener,
        AMapLocationListener,AddWatchRailAPI.AddWatchRailListener,UpdateWatchRailAPI.UpdateWatchRailListener {

    @BindView(R.id.tool_bar_friends_circle_btn_back)
    RelativeLayout toolBarFriendsCircleBtnBack;
    @BindView(R.id.tool_bar_friends_circle_title)
    TextView toolBarFriendsCircleTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tool_bar_friends_circle_right_text)
    RelativeLayout toolBarFriendsCircleRightText;
    @BindView(R.id.tb_watch_code_toolbar)
    Toolbar tbWatchCodeToolbar;
    @BindView(R.id.set_security_fence_et_name)
    EditText setSecurityFenceEtName;
    @BindView(R.id.set_security_fence_et_address)
    EditText setSecurityFenceEtAddress;
    @BindView(R.id.set_security_fence_spinner)
    Spinner setSecurityFenceSpinner;
    @BindView(R.id.set_security_fence_map)
    MapView setSecurityFenceMap;

    private AMap mAMap;
    public static final String FLAG = "FLAG";
    public static final String LATLNG = "LATLNG";
    public static final int SECURITY_LIST_INTENT = 100;

    public static final int HAVE_DATA = 1;//传递数据
    public static final int NO_DATA = 2;//没有数据
    private int flag;
    private int rails;

    private int radius_spinner = 1000;//spinner选择的半径
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LatLonPoint latLonPoint;
    private GeocodeSearch geocoderSearch;
    private LatLng latLng_watch;
    private int myFlag = 1;//判断右边title是编辑还是保存   1为编辑  2为保存
    private int spinnerMoveMap = 0;
    private boolean addFirst = true;//添加电子围栏 第一次进来不显示地址
    private boolean isLongClick;//从添加进来 是否有长按地图

    LoadingDialog loadingDialog;



    public SetSecurityFenceAct() {
        super(R.layout.act_set_security_fence, R.string.title_activity_security_fence, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId, int railId ,String latitude,
                                     String longitude,int radius,int flag,String name,String address) {
        Intent intent = new Intent(context, SetSecurityFenceAct.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("railId", railId);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("radius", radius);
        intent.putExtra(FLAG, flag);
        intent.putExtra("name", name);
        intent.putExtra("address", address);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String memberId,int flag) {
        Intent intent = new Intent(context, SetSecurityFenceAct.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra(FLAG, flag);
        context.startActivity(intent);
    }

    @Override
    public void initView() {
        loadingDialog = LoadingDialog.showDialog(this);
        initMap();

    }

    public void initViewData() {
        Intent intent = getIntent();
        toolBarFriendsCircleTitle.setText(R.string.title_activity_security_fence);
        setSecurityFenceEtName.setText(intent.getStringExtra("name"));
        setSecurityFenceEtAddress.setText(intent.getStringExtra("address"));
        setSecurityFenceEtName.setEnabled(false);
        setSecurityFenceEtAddress.setEnabled(false);

        String[] mItems = new String[]{intent.getIntExtra("radius",0) + getString(R.string.mi)};
        setSecurityFenceSpinner.setAdapter(getSpinnerAdapter(mItems));
        setSecurityFenceSpinner.setClickable(false);
        setSecurityFenceSpinner.setEnabled(false);
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        if (mAMap == null) {
            mAMap = setSecurityFenceMap.getMap();
            mAMap.getUiSettings().setScaleControlsEnabled(true);
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        setSecurityFenceEtAddress.setFocusable(false);
        myGetIntent();
    }

    /**
     * 判断是从什么进来的  1为有数据  2为没数据
     */
    public void myGetIntent() {
        Intent intent = getIntent();
        flag = intent.getIntExtra(FLAG, 0);

        switch (flag) {
            case HAVE_DATA:
                tvRight.setText(R.string.edit);
                latLng_watch = new LatLng(Double.parseDouble(intent.getStringExtra("latitude")),
                        Double.parseDouble(intent.getStringExtra("longitude")));
                radius_spinner = intent.getIntExtra("radius",0);

                isLongClick=true;

                showSecurity(radius_spinner, Float.parseFloat(intent.getStringExtra("latitude")),
                        Float.parseFloat(intent.getStringExtra("longitude")), true);

                initViewData();
                break;
            case NO_DATA:
                tvRight.setText(R.string.save);

                latLng_watch = intent.getParcelableExtra(LATLNG);

                if (latLng_watch != null) {
                    latLonPoint = new LatLonPoint(latLng_watch.latitude, latLng_watch.longitude);
                    getAddress(latLonPoint);
                    initSpinner();
                } else {
                    //如果无经纬度  则开启定位
                    PermissionUtils.needPermission(this, PermissionUtils.PER_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION);
                }
                initSpinner();
                mapLongCLick();
                break;
        }
    }

    /**
     * 初始化下拉框
     */
    public void initSpinner() {
        setSecurityFenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    radius_spinner = 1000;
                } else if (position == 1) {
                    radius_spinner = 2000;
                } else {
                    radius_spinner = 3000;
                }

                boolean b = false;
                if (spinnerMoveMap == 0) {
                    spinnerMoveMap++;
                    b = true;
                }
                if (latLng_watch == null)
                    return;

                showSecurity(radius_spinner, (float) latLng_watch.latitude, (float) latLng_watch.longitude, b);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setSecurityFenceMap.onCreate(savedInstanceState);
    }

    @OnClick({R.id.tool_bar_friends_circle_btn_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_friends_circle_btn_back:
                finish();
                break;
            case R.id.tv_right:
                if (flag == HAVE_DATA) {
                    if (myFlag == 1) {

                        clickSetting();

                        myFlag = 2;

                    } else {
                        if (StringUtils.isEmpty(setSecurityFenceEtName.getText().toString())) {
                            TipUtils.showTip(getResources().getString(R.string.set_security_fence_et_name));
                            return;
                        } else if (StringUtils.isEmpty(setSecurityFenceEtAddress.getText().toString())) {
                            TipUtils.showTip(getResources().getString(R.string.set_security_fence_et_address));
                            return;
                        }
                        saveSetting();

                        myFlag = 1;
                    }
                } else {
                    if (StringUtils.isEmpty(setSecurityFenceEtName.getText().toString())) {
                        TipUtils.showTip(getResources().getString(R.string.set_security_fence_et_name));
                    } else if (StringUtils.isEmpty(setSecurityFenceEtAddress.getText().toString())) {
                        TipUtils.showTip(getResources().getString(R.string.set_security_fence_et_address));
                    } else {
                        saveNewRail();
                    }
                }
                break;
        }
    }

    /**
     * 点击编辑后 字改为保存  edit可编辑 长按地图可移动
     */
    public void clickSetting() {
        tvRight.setText(R.string.save);
        setSecurityFenceEtName.setEnabled(true);
        setSecurityFenceEtAddress.setEnabled(true);
        setSecurityFenceSpinner.setClickable(true);
        setSecurityFenceSpinner.setEnabled(true);

        mapLongCLick();

        String[] mItems = new String[]{1000 + getString(R.string.mi), 2000 + getString(R.string.mi), 3000 + getString(R.string.mi)};
        setSecurityFenceSpinner.setAdapter(getSpinnerAdapter(mItems));
        if (radius_spinner == 1000) {
            setSecurityFenceSpinner.setSelection(0);
        } else if (radius_spinner == 2000) {
            setSecurityFenceSpinner.setSelection(1);
        } else {
            setSecurityFenceSpinner.setSelection(2);
        }
        initSpinner();
    }

    /**
     * 保存编辑
     */
    public void saveSetting() {
        tvRight.setText(R.string.edit);
        setSecurityFenceEtName.setEnabled(false);
        setSecurityFenceEtAddress.setEnabled(false);

        String[] mItems = new String[]{radius_spinner + getString(R.string.mi)};
        setSecurityFenceSpinner.setAdapter(getSpinnerAdapter(mItems));
        mAMap.setOnMapLongClickListener(null);
        setSecurityFenceSpinner.setOnItemSelectedListener(null);
        loadingDialog.show();
        //修改安全围栏
        new UpdateWatchRailAPI(this,getIntent().getIntExtra("railId",0),
                SPHelper.getInt(Builds.SP_USER,"watchId"),radius_spinner,
                setSecurityFenceEtName.getText().toString(),latLng_watch.latitude + "",latLng_watch.longitude + "",
                setSecurityFenceEtAddress.getText().toString(),"enter");
    }

    /**
     * 保存新建的电子围栏
     */
    public void saveNewRail() {
        loadingDialog.show();
        //新增安全围栏
        new AddWatchRailAPI(this, SPHelper.getInt(Builds.SP_USER,"watchId"),radius_spinner,
                setSecurityFenceEtName.getText().toString(),latLng_watch.latitude + "",latLng_watch.longitude + "",
                setSecurityFenceEtAddress.getText().toString(),"enter");
    }

    //长按地图
    public void mapLongCLick() {
        mAMap.setOnMapLongClickListener(new AMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                latLng_watch = latLng;
                latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);

                if (addFirst)
                    addFirst = false;
                if(!isLongClick)
                    isLongClick=true;

                getAddress(latLonPoint);

                showSecurity(radius_spinner, (float) latLng.latitude, (float) latLng.longitude, false);
            }
        });
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    /**
     * 显示围栏坐标
     */
    public void showSecurity(int radius_, float lat_, float lon_, boolean isMoveMap) {
        mAMap.clear();

        LatLng latLng = new LatLng(lat_, lon_);
        if (isMoveMap) {
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        }
        //第一次添加围栏进来  要求不显示maker
        if (addFirst && flag == NO_DATA)
            return;

        //从添加进来未长按
        if(!isLongClick)
            return;

        mAMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
//                .fromResource(R.drawable.gps_point))
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(latLng));

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius_);
        circleOptions.strokeWidth(5);
        circleOptions.strokeColor(Color.argb(100, 215, 158, 160));
        circleOptions.fillColor(Color.argb(80, 215, 158, 160));
        mAMap.addCircle(circleOptions);
    }


    public ArrayAdapter getSpinnerAdapter(String[] items) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return spinnerAdapter;
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int i) {
        if(!NetworkUtils.isNetworkConnect(this)){
            TipUtils.showTip(getResources().getString(R.string.act_login_tip_unconnect_network));
            return;
        }
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {

                String addressName = result.getRegeocodeAddress().getFormatAddress();
                if (flag == NO_DATA && addFirst) {
                    addFirst = false;
                } else {
                    setSecurityFenceEtAddress.setText(addressName);
                }
            } else {
                TipUtils.showTip(getResources().getString(R.string.do_not_find_place));
            }
        } else {
            TipUtils.showTip(MapErrorCode.mapErrorCode(i));
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 开启定位
     */
    public void startLocation() {
        //实例化定位客户端
        mLocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //设置定位监听
        mLocationClient.setLocationListener(this);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);

        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (loc != null) {
            mLocationClient.stopLocation();

            String addressName = loc.getAddress();
            if (loc.getLongitude() == 0) return;
            latLng_watch = new LatLng(loc.getLatitude(), loc.getLongitude());

            if (flag == NO_DATA && addFirst) {
                addFirst = false;
            } else {
                setSecurityFenceEtAddress.setText(addressName);
            }

            initSpinner();

            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng_watch));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        }
    }

    /**
     * 新增安全围栏
     * @param content
     */
    @Override
    public void addWatchRailSuccess(JSONObject content) {
        TipUtils.showTip("新增安全围栏成功");
        RefreshSecurityFenceReceiver.send(this);
        loadingDialog.dismiss();
        finish();


    }

    @Override
    public void addWatchRailError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    /**
     * 修改安全围栏
     * @param content
     */
    @Override
    public void updateWatchRailSuccess(JSONObject content) {

        TipUtils.showTip("修改安全围栏成功");
        RefreshSecurityFenceReceiver.send(this);
        loadingDialog.dismiss();
        finish();
    }

    @Override
    public void updateWatchRailError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSecurityFenceMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setSecurityFenceMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (setSecurityFenceMap != null)
            setSecurityFenceMap.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setSecurityFenceMap.onSaveInstanceState(outState);
    }

    @PermissionFail(requestCode = PermissionUtils.PER_LOCATION)
    private void grantLocationFail() {
        TipUtils.showTip(getResources().getString(R.string.please_open_location_per));
    }

    @PermissionSuccess(requestCode = PermissionUtils.PER_LOCATION)
    private void grantLocationSuccess() {
        startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
