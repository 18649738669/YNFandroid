package com.yinaf.dragon.Content.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.yinaf.dragon.R;

/**
 * Created by long on 2018/04/26.
 * 功能：加载动画的弹窗
 */

public class LoadingDialog extends Dialog{

    Context context;
    static LoadingDialog dialog;
    ImageView loadingImg;
    AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        super(context);
    }
    //显示dialog的方法
    public static LoadingDialog showDialog(Context context){
        dialog = new LoadingDialog(context);//dialog样式
        dialog.setContentView(R.layout.dialog_loading);//dialog布局文件
        dialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        return dialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && dialog != null){
            loadingImg = (ImageView) dialog.findViewById(R.id.loading_img);
            loadingImg.setImageResource(R.drawable.view_loading_anim);
            animationDrawable = ((AnimationDrawable) loadingImg.getDrawable());
            animationDrawable.start();
        }
    }
}
