package com.yinaf.dragon.Content.PopupWindow;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yinaf.dragon.Content.Activity.HistoricalPositionAct;
import com.yinaf.dragon.Content.Activity.SecurityFenceAct;
import com.yinaf.dragon.R;

/**
 * Created by long on 2018/05/03.
 * 功能：弹出地图下拉菜单
 */

public class MenuMapPopup {

    public static void show(View view, final String memberId){
        final View contentView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_menu_map,null);
        TextView menu_one = contentView.findViewById(R.id.map_menu_tv_one);
        TextView menu_two = contentView.findViewById(R.id.map_menu_tv_two);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                false);
        //必须实现setFocusable和setBackgrounfDrawable方法，否则无法实现dismiss
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        popupWindow.showAsDropDown(view);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        menu_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoricalPositionAct.startActivity(v.getContext(),memberId);
                popupWindow.dismiss();
            }
        });
        menu_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecurityFenceAct.startActivity(v.getContext(),memberId);
                popupWindow.dismiss();
            }
        });

    }


}
