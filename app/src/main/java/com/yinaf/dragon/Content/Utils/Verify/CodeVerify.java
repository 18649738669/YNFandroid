package com.yinaf.dragon.Content.Utils.Verify;

import android.text.TextUtils;

import com.yinaf.dragon.R;
import com.yinaf.dragon.Tool.Utils.ResUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chen on 2016/7/26.
 * 功能：验证输入的手机号码信息
 */
public class CodeVerify extends BasicVerify {

    public static VerifyResult verify(String code){

        CodeVerify phoneVerify = new CodeVerify();
        if(TextUtils.isEmpty(code)){
            return phoneVerify.error(ResUtils.getString(R.string.verification_verify_error_empty));
        }
        //正则表达式，6位数字
        String regex = "[0-9]{6}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);
        boolean isRight = matcher.matches();
        if(!isRight){
            return phoneVerify.error(ResUtils.getString(R.string.verification_verify_error_no_num));
        }
        return  phoneVerify.success();
    }

}
