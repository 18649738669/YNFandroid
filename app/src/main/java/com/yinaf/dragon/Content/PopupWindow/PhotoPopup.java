package com.yinaf.dragon.Content.PopupWindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.APP.Builds;
import com.yinaf.dragon.Tool.DB.SPHelper;
import com.yinaf.dragon.Tool.Utils.LogUtils;
import com.yinaf.dragon.Tool.Utils.UIUtils;

import java.util.List;

/**
 * Created by chen on 2016/8/15.
 * 功能：弹出"拍照"窗口
 */
public class PhotoPopup {

    /**
     * 显示弹出框
     *
     * @param view
     */
    public static void show(final PhotoListener listener, final Activity activity, final View
            view) {

        View contentView = LayoutInflater.from(view.getContext()).inflate(R.layout.popup_photo, null);
        Button camera = (Button) contentView.findViewById(R.id
                .popup_photo_btn_camera);
        Button gallery = (Button) contentView.findViewById(R.id.popup_photo_btn_gallery);
        Button cancel = (Button) contentView.findViewById(R.id.popup_photo_btn_cancel);

        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams
                .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);

        //必须实现setFocusable和setBackgrounfDrawable方法，否则无法实现dismiss
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        UIUtils.setWindowAlpha(activity, 0.4f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                UIUtils.setWindowAlpha(activity, 1f);
            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.getCameraPhoto();
                popupWindow.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.getGalleyPhoto();
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }

    public interface PhotoListener {
        public void getCameraPhoto();

        public void getGalleyPhoto();
    }
}
