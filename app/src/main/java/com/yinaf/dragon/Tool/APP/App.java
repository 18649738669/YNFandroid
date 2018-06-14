package com.yinaf.dragon.Tool.APP;
import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.tencent.bugly.crashreport.CrashReport;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;


/**
 * Created by long on 2018-4-12.
 */
public class App extends MultiDexApplication {


    public static Context context;
    public static App app;
    public static RequestQueue requestQueue;
    public static Handler mainHandler;

    public static ImageLoader imageLoader;

    public static Handler getMainHandler(){
        return mainHandler;
    }

    public static Context getContext() {

        return context;
    }

    public static ImageLoader getImageLoader(){return imageLoader;}

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        mainHandler = new Handler();


//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(initConfiguration());

        /**
         * 初始化极光推送SDK
         */
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(this);
//        builder.statusBarDrawable = R.mipmap.logo;
//        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
//        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE ;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
//        JPushInterface.setPushNotificationBuilder(1, builder);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();


        //测试
//        CrashReport.initCrashReport(getApplicationContext(), "238fbecfd1", true);

        //Stetho初始化
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        );

        //设置可以访问Https
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null,null,null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60000L, TimeUnit.MILLISECONDS)
                .readTimeout(60000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public ImageLoaderConfiguration initConfiguration(){
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(2)
                .memoryCache(new WeakMemoryCache())
                .build();

        return configuration;
    }


    public static App getApp(){
        if(app == null){
            synchronized (App.class){
                if(app == null){
                    app = new App();
                }
            }
        }
        return app;
    }


    /**
     * 获取网络请求的消息队列
     * @return
     */
    public static RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

}
