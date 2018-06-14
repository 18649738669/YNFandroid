package com.yinaf.dragon.Content.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.yinaf.dragon.Content.Bean.InfoWindow;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;

import java.util.List;

/**
 * Created by long on 2018/05/03.
 * 功能：自定义地图信息框infoWindow
 */

public class CustomInfoWindow2Adapter implements AMap.InfoWindowAdapter{

    Context context;
    public CustomInfoWindow2Adapter(Context context){
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_map_info_window_2,null);
        setViewContent(marker,view);
        return view;
    }
    //这个方法根据自己的实体信息来进行相应控件的赋值
    private void setViewContent(Marker marker,View view) {

        TextView title = view.findViewById(R.id.map_info_window_tv_title);
        TextView time = view.findViewById(R.id.map_info_window_tv_time);
        TextView content = view.findViewById(R.id.map_info_window_tv_content);
        if (marker.getTitle() == null || marker.getTitle().equals("")){
            title.setText(SPHelper.getString(Builds.SP_USER,"memberRealName"));
        }else {
            title.setText(marker.getTitle());
        }
        String []str =marker.getSnippet().split("#");
        time.setText(str[0]);
        content.setText(str[1]);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

}
