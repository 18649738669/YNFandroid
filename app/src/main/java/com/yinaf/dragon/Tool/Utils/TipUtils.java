package com.yinaf.dragon.Tool.Utils;

import android.widget.Toast;

import com.yinaf.dragon.Tool.APP.App;


/**
 * Created by long on 2018-4-12.
 * 功能：Toast工具类
 */
public class TipUtils {

    public static void showTip(String msg){
        Toast.makeText(App.getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
