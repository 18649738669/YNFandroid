package com.yinaf.dragon.Content.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;


import com.yinaf.dragon.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by long on 18/5/10.
 */
public class CommonUtil {

    public static void showInfoDialog(Context context, String message,
                                      int titleStrId,
                                      DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setTitle(titleStrId);
        localBuilder.setMessage(message);
        if (onClickListener == null)
            onClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            };
        localBuilder.setPositiveButton(R.string.commit, onClickListener);
        localBuilder.show();
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取现在时间
     *
     * @return 返回日期字符串格式yyyy.MM.dd
     */

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }

    public static void clearWebViewCache(Activity context) {
        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
//        File appCacheDir = new File(context.getFilesDir().getAbsolutePath() + ShopFragment.WEB_CACHE);
        ///data/data/com.lk.elderlywatch/cache/webviewCache

        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath() + "/webviewCache");
        ///data/data/com.lk.elderlywatch/files/webCache

        //删除webview 缓存目录
        deleteFile(webviewCacheDir);
        //删除webview 缓存 缓存目录
//        deleteFile(appCacheDir);
    }


    /**
     * 将String数据存为文件
     */
    public static File getFileFromBytes(String name, String path) {
        byte[] b = name.getBytes();
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(path);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 将文件转为String
     */
    public static String getStringFromFile(String fileString) {
        File file = new File(fileString);
        String xmlString;
        byte[] strBuffer = null;
        int flen;
        try {
            InputStream in = new FileInputStream(file);
            flen = (int) file.length();
            strBuffer = new byte[flen];
            in.read(strBuffer, 0, flen);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        xmlString = new String(strBuffer);

        return xmlString;
    }

    /**
     * retrofit
     * 上传语音消息文件
     */
    public static MultipartBody.Part retrofitUploadFile(String path, String filename) {
        //1、根据地址拿到File
        final File file = new File(path);
        //2、创建RequestBody，其中`multipart/form-data`为编码类型
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/amr"), file);
        //3、创建`MultipartBody.Part`，其中需要注意第一个参数`filename`需要与服务器对应,也就是`键`
        MultipartBody.Part part = MultipartBody.Part.createFormData(filename, file.getName(), requestFile);
        return part;
    }

    /**
     * 通过流传照片
     *
     * @param bitmap
     * @param name
     * @return
     */
    public static MultipartBody.Part retrofitUploadStream(Bitmap bitmap, String name) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] bytes = os.toByteArray();
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", name,
                RequestBody.create(MediaType.parse("image/*"), bytes));
        return part;
    }

    @TargetApi(19)
    public static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 保存多媒体数据为文件.
     *
     * @param data     多媒体数据
     * @param fileName 保存文件名
     * @return 保存成功或失败
     */
    public static boolean saveFile(InputStream data, String fileName) {
        File file = new File(fileName);
        FileOutputStream fos;
        try {
            // 文件或目录不存在时,创建目录和文件.
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            // 写入数据
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = data.read(b)) > -1) {
                fos.write(b, 0, len);
            }
            fos.close();

            return true;
        } catch (IOException ex) {

            return false;
        }
    }

    /**
     * 手机号正则
     *
     * @return true 正确  false  错误
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "13\\d{9}|14[57]\\d{8}|15[012356789]\\d{8}|18[012456789]\\d{8}|17[0678]\\d{8}";
        if (TextUtils.isEmpty(mobiles))
            return
                    false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 屏蔽掉editText的复制粘贴框
     */
    public static void hintEdit(EditText editText){
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return true;
            }
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return true;
            }
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return true;
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
        editText.setLongClickable(false);
    }


}
