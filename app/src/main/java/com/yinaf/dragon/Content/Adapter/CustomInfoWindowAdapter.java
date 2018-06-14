package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.yinaf.dragon.Content.Bean.InfoWindow;
import com.yinaf.dragon.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long on 2018/05/03.
 * 功能：自定义地图信息框infoWindow
 */

public class CustomInfoWindowAdapter implements AMap.InfoWindowAdapter{

    Context context;
    public CustomInfoWindowAdapter(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_map_info_window,null);
        setViewContent(marker,view);
        return view;
    }

    //这个方法根据自己的实体信息来进行相应控件的赋值
    private void setViewContent(Marker marker,View view) {

        final InfoWindow infoWindow = (InfoWindow) marker.getObject();
        TextView title = view.findViewById(R.id.map_info_window_tv_title);
        TextView time = view.findViewById(R.id.map_info_window_tv_time);
        TextView content = view.findViewById(R.id.map_info_window_tv_content);
        TextView btn = view.findViewById(R.id.map_info_window_tv_btn);
        title.setText(infoWindow.getTitle());
        time.setText(infoWindow.getTime());
        content.setText(infoWindow.getContent());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInstallApk(context, "com.autonavi.minimap")) {// 是否安装了高德
                    Intent intents = new Intent();
                    intents.setData(Uri.parse("androidamap://navi?sourceApplication=nyx_super&lat=" + infoWindow.getLatitude() + "&lon=" + infoWindow.getLongitude() + "&dev=0&style=2"));
                    context.startActivity(intents); // 启动调用
                }else {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    // 驾车导航
                    intent.setData(Uri.parse("http://uri.amap.com/navigation?from=" + infoWindow.getStart_lng() + "," + infoWindow.getStart_lat() + "&to="+ infoWindow.getLongitude() + "," + infoWindow.getLatitude() + "&mode=car&src=nyx_super"));
                    context.startActivity(intent); // 启动调用
                }
            }
        });

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /** 判断手机中是否安装指定包名的软件 */
    public static boolean isInstallApk(Context context, String name) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(name)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }


}
