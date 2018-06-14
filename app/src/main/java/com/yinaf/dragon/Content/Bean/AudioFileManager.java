package com.yinaf.dragon.Content.Bean;

import android.content.Context;
import android.text.TextUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.yinaf.dragon.Content.Utils.EncodeUtil.md5;


/**
 * Created by long on 2018/5/10
 */

public class AudioFileManager {

    private String basePath;
    private static AudioFileManager instance;
    private GetFileListener getFileListener;

    public static AudioFileManager getInstance() {
        if (instance == null) {
            synchronized (AudioFileManager.class) {
                if (instance == null)
                    instance = new AudioFileManager();
            }
        }
        return instance;
    }

    public void setBasePath(Context context) {
        basePath = context.getFilesDir().getAbsolutePath()+ "/chatvoice/";
    }

    //语音路径
    public String getPath(String url){
        return basePath + md5(url) + ".amr";
    }

    public void getSoundFile(Context context, String url, GetFileListener getFileListener) {
        this.getFileListener = getFileListener;
        if (TextUtils.isEmpty(basePath))
            setBasePath(context);
        getFile(url);
    }

    /**
     * 拿文件   本地有就拿本地，没有就下载
     *
     * @param url
     */

    public void getFile(String url) {

        if (diskHaveIt(url)) {
            byte[] fileByteArray = new byte[1024];
            try {
                File file = new File(getPath(url));
                FileInputStream in;
                in = new FileInputStream(file);
                int length = in.available();
                fileByteArray = new byte[length];
                in.read(fileByteArray);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fileByteArray.length != 0) {
                if (getFileListener != null) {
                    getFileListener.onSussess(getPath(url));
                    return;
                }
            }
        }
        getFileListener.onFail("no");
        downLoad(url);
    }

    //检测url文件是否存在
    public boolean diskHaveIt(String url) {
        String path = getPath(url);
        if (path != null &&
                !path.equals("")) {
            File file = new File(path);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    public void saveFile(String url, InputStream is) throws IOException {
        File file = new File(basePath);
        if (!file.exists())
            file.mkdirs();
        // 构建文件输出流
        FileOutputStream fos = new FileOutputStream(new File(getPath(url)));
        byte[] b = new byte[1024]; // 构建缓冲区
        // 循环读取数据
        int byteCount; // 每次读取的长度
        while ((byteCount = is.read(b)) != -1) {
            fos.write(b, 0, byteCount); // 将每次读取的数据保存到文件当中
        }
        fos.close(); // 关闭文件输出流
        is.close(); // 关闭输入流
    }

    public void downLoad(String url) {
        Flowable.just(url).subscribeOn(Schedulers.io()).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                try {
                    URL url = new URL(s); // 构建URL
                    // 构造网络连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 保存音频文件
                    saveFile(s, conn.getInputStream());
                    conn.disconnect(); // 断开网络连接
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return getPath(s);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
//                getFileListener.onSussess(s);
            }
        });
    }

    public interface GetFileListener {
        void onSussess(String filePath);

        void onFail(String reason);
    }
}
