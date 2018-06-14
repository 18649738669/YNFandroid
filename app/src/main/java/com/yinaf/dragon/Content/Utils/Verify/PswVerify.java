package com.yinaf.dragon.Content.Utils.Verify;

import android.text.TextUtils;

import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Utils.ResUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by chen on 2016/7/26.
 * 功能：验证输入的密码信息
 */
public class PswVerify extends BasicVerify {

    public static VerifyResult verify(String pwd){

        PswVerify pswVerify = new PswVerify();
        if(TextUtils.isEmpty(pwd)){
            return pswVerify.error(ResUtils.getString(R.string.psw_verify_error_empty));
        }
        //正则表达式，密码范围在8至16个字符之间，只能输入数字、英文字母和简单标点符号
        String regex = "^\\w{6,16}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pwd);
        boolean isRight = matcher.matches();
        if(!isRight){
            return pswVerify.error(ResUtils.getString(R.string.psw_verify_error__no_num));
        }
        return pswVerify.success();
    }

}
