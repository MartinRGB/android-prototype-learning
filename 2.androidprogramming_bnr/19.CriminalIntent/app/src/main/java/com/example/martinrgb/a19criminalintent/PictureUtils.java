package com.example.martinrgb.a19criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by MartinRGB on 2017/2/24.
 */

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        //设置解码选项
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //所旅途像素大小， inSampleSize = 2，像素数是原来1/4
        int intSampleSize = 1;

        if (srcHeight > destHeight || srcHeight > destWidth) {
            if (srcWidth > srcHeight) {
                intSampleSize = Math.round(srcHeight / destHeight);
            } else {
                intSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = intSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }

    //缩放，等待布局实例化完成后，显示
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

}
