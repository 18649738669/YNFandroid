package com.yinaf.dragon.Tool.APP;

import android.os.Environment;

/**
 * Created by long on 2018-4-12.
 */

public class Builds {

    public static final String SP_USER = "ynf_user";

//    public static final String HOST = "http://192.168.1.101:8090/yinaf";//测试服务器
  public static final String HOST = "http://mobile.yinaf.com";//外网服务器
//    public static final String HOST = "http://192.168.1.131:8080/yinaf";
//    public static final String HOST = "http://www.javatest.top/yinaf";//外网测试服务器

    public final static String LOCAL_HOST = Environment.getExternalStorageDirectory()
            + "/WisdomEndowmentRoad";
    /**
     * 图片路径
     */
    public final static String IMAGE_PATH = Environment.getExternalStorageDirectory()
            + "/WisdomEndowmentRoad/Image";
    /**
     * 图片压缩路径
     */
    public final static String IMAGE_COMPRESS_PATH = Environment.getExternalStorageDirectory()
            + "/WisdomEndowmentRoad/ImageCompress";
    /**
     * 图片水印路径
     */
    public final static String IMAGE_WATERMASK_PATH = Environment.getExternalStorageDirectory()
            + "/WisdomEndowmentRoad/ImageWKCompress";

    public final static String APK_PATH = Environment.getExternalStorageDirectory()
            + "/WisdomEndowmentRoad/apk";

    public final static String VOICE_PATH = Environment.getExternalStorageDirectory()
            + "/WisdomEndowmentRoad/chatvoice";

}
