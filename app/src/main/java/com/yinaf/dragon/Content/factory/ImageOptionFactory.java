package com.yinaf.dragon.Content.factory;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yinaf.dragon.R;

/**
 * Created by chen on 2016/8/5.
 */
public class ImageOptionFactory {

    /**
     * 救援类型图片的配置
     * @return
     */
//    public static DisplayImageOptions getImageOption(){
//
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.n_other)
//                .showImageForEmptyUri(R.drawable.n_other)
//                .showImageOnFail(R.drawable.n_other)
//                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
//                .bitmapConfig(Bitmap.Config.RGB_565).build();
//        return options;
//    }

    /**
     * 默认图片图片配置
     * @return
     */
    public static DisplayImageOptions getDefaultImageOption(){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.rounded_img2)
                .showImageForEmptyUri(R.drawable.rounded_img2)
                .showImageOnFail(R.drawable.rounded_img2)
                .cacheInMemory(false)     //图片是否在内存中
                .cacheOnDisk(false)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)   //图片的缩放类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }

}
