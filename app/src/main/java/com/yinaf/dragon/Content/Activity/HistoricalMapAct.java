package com.yinaf.dragon.Content.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.yinaf.dragon.Content.Adapter.CustomInfoWindow2Adapter;
import com.yinaf.dragon.Content.Adapter.CustomInfoWindowAdapter;
import com.yinaf.dragon.Content.Dialog.LoadingDialog;
import com.yinaf.dragon.Content.Net.GetLocationHistoryAPI;
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


/**
 * Created by long on 2018/05/04.
 * 功能：历史定位的地图页面
 */

public class HistoricalMapAct extends BasicAct implements GetLocationHistoryAPI.GetLocationHistoryListener,
        AMap.OnMapClickListener,AMap.OnMarkerClickListener,AMap.OnInfoWindowClickListener{

    @BindView(R.id.tool_bar_title)
    TextView toolBarTitle;
    @BindView(R.id.historical_position_map)
    MapView historicalPositionMap;

    //地图控制器对象
    AMap aMap;
    LoadingDialog loadingDialog;
    int index=0;
    private List<MarkerOptions> markers=new ArrayList<MarkerOptions>();
    private List<Marker> mList=new ArrayList<Marker>();
    Marker curShowWindowMarker;//缓存marker

    public HistoricalMapAct() {
        super(R.layout.act_historical_map, R.string.title_activity_historical_position, true, TOOLBAR_TYPE_DEFAULT, R.color.common_blue);
    }

    public static void startActivity(Context context, String memberId,String day) {
        Intent intent = new Intent(context, HistoricalMapAct.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("day",day);
        context.startActivity(intent);
    }

    @Override
    public void initView() {

        toolBarTitle.setText(getIntent().getStringExtra("day"));
        loadingDialog = LoadingDialog.showDialog(this);
        initMap();
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        //初始化地图控制器对象
        if (aMap == null){
            aMap = historicalPositionMap.getMap();
        }
        aMap.setInfoWindowAdapter(new CustomInfoWindow2Adapter(this));
        aMap.setOnMapClickListener(this);
        loadingDialog.show();
        new GetLocationHistoryAPI(this,getIntent().getStringExtra("day"),getIntent().getStringExtra("memberId"));

    }

    /**绘制两个坐标点之间的线段,从以前位置到现在位置*/
    private void setUpMap(LatLng oldData,LatLng newData ) {

        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.argb(255,0,170,239)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        historicalPositionMap.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        historicalPositionMap.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        historicalPositionMap.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        historicalPositionMap.onSaveInstanceState(outState);
    }

    /**
     * 获取成员当天定位
     * @param content
     */
    @Override
    public void getLocationHistorySuccess(JSONArray content) {
        mList.clear();
        aMap.clear();
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
        for (int i = 0; i < content.length(); i++){
            try {
                JSONObject jsonObject = content.getJSONObject(i);
                String longitude = JSONUtils.getString(jsonObject,"lon");
                String latitude = JSONUtils.getString(jsonObject,"lat");
                double dlong=Double.valueOf(longitude).doubleValue();
                double dlat=Double.valueOf(latitude).doubleValue();
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(new LatLng(dlat, dlong));
                if (i == 0){
                    markerOption .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.locate_destination)));
                }else if (i + 1 == content.length()){
                    markerOption .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_rescue)));
                }else {
                    markerOption .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location)));
                }
                markerOption.title(JSONUtils.getString(jsonObject,"name"));
                markerOption.snippet(JSONUtils.getString(jsonObject,"device_time") + "\n #"+JSONUtils.getString(jsonObject,"address"));
                markerOption.draggable(true);
                markerOption.setFlat(true);
                markerOptionlst.add(markerOption);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mList = aMap.addMarkers(markerOptionlst, true);
        for (int i = 1; i < mList.size(); i++){
            LatLng oldData = new LatLng(mList.get(i - 1).getPosition().latitude,mList.get(i - 1).getPosition().longitude);
            LatLng newData = new LatLng(mList.get(i).getPosition().latitude,mList.get(i).getPosition().longitude);
            setUpMap(oldData,newData);
        }
        loadingDialog.dismiss();
    }

    @Override
    public void getLocationHistoryError(long code, String msg) {

        loadingDialog.dismiss();
        TipUtils.showTip(msg);
    }

    private LatLng getpositionformlist(int index){
        LatLng latLng=null;
        if(mList.size()>0){
            double dlong=mList.get(index).getPosition().longitude;
            double dlat=mList.get(index).getPosition().latitude;
            latLng=new LatLng(dlat, dlong);
        }
        return latLng;
    }
    public void jumpPoint(final Marker marker,int index) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng latLng=getpositionformlist(index);
        handler.post(new Runnable() {
            @Override
            public void run() {
                marker.setPosition(new LatLng(latLng.latitude, latLng.longitude));
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (aMap != null && curShowWindowMarker != null) {
            if (curShowWindowMarker.isInfoWindowShown()) {
                curShowWindowMarker.hideInfoWindow();
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for(int i=0;i<mList.size();i++){
            if (marker.equals(mList.get(i))) {
                if (aMap != null) {
                    jumpPoint(marker,i);
                    curShowWindowMarker = marker;//保存当前点击的Marker，以便点击地图其他地方设置InfoWindow消失
                }
            }
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
    }
}
