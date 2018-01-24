package com.android.bignerdranch.criminalintent.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Kaustav on 17-11-2017.
 */

public class PicUtils {

    public static Bitmap getScaledBitmap(String path,int destWidth,int destHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        float actualWidth = options.outWidth;
        float actualHeight = options.outHeight;

        int inSamplesize = 1;
        if(actualHeight > destHeight || actualWidth > destWidth) {
            float widthScale = destWidth / actualWidth;
            float heightScale = destHeight / actualHeight;

            inSamplesize = Math.round(widthScale > heightScale ? widthScale : heightScale);
        }


        options.inSampleSize = inSamplesize;
        options = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path,size.x,size.y);
    }

}
