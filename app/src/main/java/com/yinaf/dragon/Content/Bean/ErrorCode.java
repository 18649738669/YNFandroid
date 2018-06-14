package com.yinaf.dragon.Content.Bean;

import java.util.HashMap;

/**
 * Created by long on 2018-4-12.
 */

public class ErrorCode {
    private static HashMap<Integer, String> instance;

    public static HashMap<Integer, String> getInstance() {
        if (instance == null) {
            synchronized (ErrorCode.class) {
                if (instance == null) {
                    instance = getErrorCode();
                }
            }
        }
        return instance;
    }

    private static HashMap<Integer, String> getErrorCode() {
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(0, "请求正常");
        hashMap.put(404, "数据异常");
        hashMap.put(413, "您输入的验证码无效，请重新输入");
        hashMap.put(2000, "用户session过期或无效");
        hashMap.put(2001, "验证码无效或过期");
        hashMap.put(2002, "用户名或密码错误");
        hashMap.put(2003, "手机号码已被注册");
        hashMap.put(3001, "该手机号未注册");
        hashMap.put(3002, "腕表不存在");
        hashMap.put(3003, "联系人不存在");
        hashMap.put(3004, "地址不存在");
        hashMap.put(3005, "时间格式错误");
        hashMap.put(3006, "药物提醒不存在");
        hashMap.put(3008, "广告不存在");
        hashMap.put(3009, "活动不存在");
        hashMap.put(3010, "活动存在");
        hashMap.put(3011, "消息不存在");
        hashMap.put(3012, "生活习惯记录不存在");
        hashMap.put(3013, "位置信息记录不存在");
        hashMap.put(3018, "资讯不存在");
        hashMap.put(3019, "资讯存在");
        hashMap.put(3020, "日期类型超出范围");
        hashMap.put(3021, "腕表服务套餐已不存在");
        hashMap.put(3022, "警报范围设置错误:最小比最大大");
        hashMap.put(3023, "警报范围设置错误:最大比最小小");
        hashMap.put(3024, "警报范围设置顺序错误");
        hashMap.put(3025, "该手机号已注册");
        hashMap.put(3026, "关爱评分不存在");
        hashMap.put(3027, "该手机号未注册");
        hashMap.put(3028, "用户已绑定过该手表");
        hashMap.put(3029, "该手机号已被使用");
        hashMap.put(3030, "服药提醒最多可添加3个");
        hashMap.put(3031, "提醒日期不能为已过去的时间");
        hashMap.put(4000, "服务器异常");
        hashMap.put(4001, "服务器资源超限，请稍后重试");
        hashMap.put(4002, "请求接口缺少必要参数");
        hashMap.put(4003, "请求接口参数类型错误");
        hashMap.put(5001, "腕表不在线");
        hashMap.put(5002, "请求腕表指令超时");
        hashMap.put(6000, "请在管理平台开通该表功能");
        hashMap.put(6001, "手表未开户");

        return hashMap;
    }
}
