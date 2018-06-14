package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.yinaf.dragon.Content.Adapter.CustomInfoWindowAdapter;
import com.yinaf.dragon.Content.Bean.InfoWindow;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetCurrentLocusAPI;
import com.yinaf.dragon.Content.Net.GetLocationHistoryAPI;
import com.yinaf.dragon.Content.Net.GetWatchBatteryAPI;
import com.yinaf.dragon.Content.PopupWindow.MenuMapPopup;
import com.yinaf.dragon.Content.Receiver.JPushReceiver;
import com.yinaf.dragon.Content.Receiver.RefreshPlaceReceiver;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Activity.BasicAct;
import com.yinaf.dragon.Tool.Utils.JSONUtils;
import com.yinaf.dragon.Tool.Utils.TipUtils;

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

/**
 * Created by long on 2018/05/02.
 * 功能：手表定位页面
 */

public class PlaceAct extends BasicAct implements AMapLocationListener,GetWatchBatteryAPI.GetWatchBatteryListener,
        AMap.OnMarkerClickListener,GetCurrentLocusAPI.GetCurrentLocusListener,AMap.OnInfoWindowClickListener,
        AMap.OnMapTouchListener,AMap.OnMapClickListener,RefreshPlaceReceiver.RefreshPlaceListener{


    @BindView(R.id.tool_bar_place_btn_back)
    RelativeLayout toolBarPlaceBtnBack;
    @BindView(R.id.tool_bar_place_title)
    TextView toolBarPlaceTitle;
    @BindView(R.id.tool_bar_place_right)
    RelativeLayout toolBarPlaceRight;
    @BindView(R.id.place_map)
    MapView placeMap;
    @BindView(R.id.place_iv_electricity)
    ImageView placeIvElectricity;
    @BindView(R.id.place_tv_electricity)
    TextView placeTvElectricity;
    @BindView(R.id.place_btn)
    Button placeBtn;
    LoadingDialog loadingDialog;

    //地图控制器对象
    AMap aMap;
    //声明AMapLocationClient类对象
    AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    AMapLocationListener mLocationListener = null;
    //声明AMapLocationClientOption对象
    AMapLocationClientOption mLocationOption = null;
    MyLocationStyle myLocationStyle;
    Marker curShowWindowMarker;//缓存marker
    public static final int CURRENT_TASK_COUNT_DOWN = 10001;

    double start_lat;//起点纬度
    double start_lng;//起点经度

    RefreshPlaceReceiver refreshPlaceReceiver;//刷新成员广播

    public PlaceAct() {
        super(R.layout.act_place, R.string.title_activity_place, true, TOOLBAR_TYPE_NO_TOOLBAR, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId,String name) {
        Intent intent = new Intent(context, PlaceAct.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        loadingDialog = LoadingDialog.showDialog(this);
        toolBarPlaceTitle.setText(R.string.title_activity_place);

        loadingDialog.show();
        new GetWatchBatteryAPI(this,getIntent().getStringExtra("memberId"));
        new GetCurrentLocusAPI(this,getIntent().getStringExtra("memberId"));
        initMap();
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        //初始化地图控制器对象
        if (aMap == null){
            aMap = placeMap.getMap();
        }
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;
        //设置显示定位小蓝点
        myLocationStyle.showMyLocation(true);
        //设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        //设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置显示定位按钮
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        aMap.setOnMapTouchListener(this);
//        aMap.setOnMapClickListener(this);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        placeMap.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        placeMap.onDestroy();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        if(null != mLocationClient){//销毁定位客户端，同时销毁本地定位服务。
            mLocationClient.onDestroy();
        }
        RefreshPlaceReceiver.unregister(this,refreshPlaceReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        placeMap.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        placeMap.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        placeMap.onSaveInstanceState(outState);
    }

    @OnClick({R.id.tool_bar_place_btn_back, R.id.tool_bar_place_right,R.id.place_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_place_btn_back:
                finish();
                break;
            case R.id.tool_bar_place_right:
                MenuMapPopup.show(toolBarPlaceRight,getIntent().getStringExtra("memberId"));
                break;
            case R.id.place_btn:
                loadingDialog.show();
                new GetCurrentLocusAPI(this,getIntent().getStringExtra("memberId"));
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null
                &&aMapLocation.getErrorCode() == 0) {
            mLocationListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            start_lat = aMapLocation.getLatitude();
            start_lng = aMapLocation.getLongitude();
        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
            Log.e("AmapErr",errText);
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
            placeIvElectricity.setImageResource(R.drawable.electricity_num_0);
            placeTvElectricity.setText("尚未连接");
        }else if (obj >= 1 && obj <= 25){
            placeIvElectricity.setImageResource(R.drawable.electricity_num_1);
            placeTvElectricity.setText("当前电量"+obj+"%");
        }else if (obj > 25 && obj <= 50){
            placeIvElectricity.setImageResource(R.drawable.electricity_num_2);
            placeTvElectricity.setText("当前电量"+obj+"%");
        }else if (obj > 50 && obj <= 75){
            placeIvElectricity.setImageResource(R.drawable.electricity_num_3);
            placeTvElectricity.setText("当前电量"+obj+"%");
        }else if (obj > 75 && obj <= 100){
            placeIvElectricity.setImageResource(R.drawable.electricity_num_4);
            placeTvElectricity.setText("当前电量"+obj+"%");
        }
    }

    @Override
    public void getWatchBatteryError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }


    /**
     * 获取成员实时定位
     * @param content
     */
    @Override
    public void getCurrentLocusSuccess(JSONObject content) {
        if (content != null && !content.equals("") && JSONUtils.getInt(content, "pushed") == 0) {
            aMap.clear();
            InfoWindow infoWindow = new InfoWindow();
            String longitude = JSONUtils.getString(content, "lon");
            String latitude = JSONUtils.getString(content, "lat");
            double dlong = Double.valueOf(longitude).doubleValue();
            double dlat = Double.valueOf(latitude).doubleValue();
            infoWindow.setLatitude(dlat);
            infoWindow.setLongitude(dlong);
            infoWindow.setStart_lat(start_lat);
            infoWindow.setStart_lng(start_lng);
            infoWindow.setTitle(JSONUtils.getString(content, "name"));
            infoWindow.setTime(JSONUtils.getString(content, "createTime"));
            infoWindow.setContent(JSONUtils.getString(content, "address"));
            MarkerOptions options = new MarkerOptions();
            options.title("");
            options.position(new LatLng(dlat, dlong))
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location)));
            Marker marker = aMap.addMarker(options);
            marker.setObject(infoWindow);
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(dlat, dlong)));
            loadingDialog.dismiss();
        }else {
            aMap.clear();
            InfoWindow infoWindow = new InfoWindow();
            String longitude = JSONUtils.getString(content, "lon");
            String latitude = JSONUtils.getString(content, "lat");
            double dlong = Double.valueOf(longitude).doubleValue();
            double dlat = Double.valueOf(latitude).doubleValue();
            infoWindow.setLatitude(dlat);
            infoWindow.setLongitude(dlong);
            infoWindow.setStart_lat(start_lat);
            infoWindow.setStart_lng(start_lng);
            infoWindow.setTitle(JSONUtils.getString(content, "name"));
            infoWindow.setTime(JSONUtils.getString(content, "createTime"));
            infoWindow.setContent(JSONUtils.getString(content, "address"));
            MarkerOptions options = new MarkerOptions();
            options.title("");
            options.position(new LatLng(dlat, dlong))
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location)));
            Marker marker = aMap.addMarker(options);
            marker.setObject(infoWindow);
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(dlat, dlong)));
            loadingDialog.dismiss();
            refreshPlaceReceiver = new RefreshPlaceReceiver(this);
            RefreshPlaceReceiver.register(this,refreshPlaceReceiver);
        }
    }

    @Override
    public void getCurrentLocusError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        curShowWindowMarker = marker;//保存当前点击的Marker，以便点击地图其他地方设置InfoWindow消失
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (aMap != null && curShowWindowMarker != null) {
            if (curShowWindowMarker.isInfoWindowShown()) {
                curShowWindowMarker.hideInfoWindow();
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (aMap != null && curShowWindowMarker != null) {
            if (curShowWindowMarker.isInfoWindowShown()) {
                curShowWindowMarker.hideInfoWindow();
            }
        }
    }

    /**
     * 接收刷新成员实时定位的广播
     * @param data
     */
    @Override
    public void receiverRefreshPlace(String data) {

        aMap.clear();
        JSONObject content = JSONUtils.toJSONObject(data);
        InfoWindow infoWindow = new InfoWindow();
        String longitude = JSONUtils.getString(content, "lon");
        String latitude = JSONUtils.getString(content, "lat");
        double dlong = Double.valueOf(longitude).doubleValue();
        double dlat = Double.valueOf(latitude).doubleValue();
        infoWindow.setLatitude(dlat);
        infoWindow.setLongitude(dlong);
        infoWindow.setStart_lat(start_lat);
        infoWindow.setStart_lng(start_lng);
        infoWindow.setTitle(getIntent().getStringExtra("name"));
        infoWindow.setTime(JSONUtils.getString(content, "create_time"));
        infoWindow.setContent(JSONUtils.getString(content, "address"));
        MarkerOptions options = new MarkerOptions();
        options.title("");
        options.position(new LatLng(dlat, dlong))
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location)));
        Marker marker = aMap.addMarker(options);
        marker.setObject(infoWindow);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(dlat, dlong)));
        loadingDialog.dismiss();
    }
}
