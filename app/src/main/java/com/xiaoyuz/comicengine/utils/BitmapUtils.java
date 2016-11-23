package com.xiaoyuz.comicengine.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

/**
 * Created by zhangxiaoyu on 16-10-11.
 */
public class BitmapUtils {

    /*
     * Bitmap → byte[]
    */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /*
     * byte[] → Bitmap
     */
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /*
     * Drawable → Bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;

        Bitmap bitmap = Bitmap.createBitmap(w, h, config);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);

        drawable.draw(canvas);
        return bitmap;
    }

    /*
     * Bitmap → Drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmap2Drawable(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        return new BitmapDrawable(bm);
    }

    public static Bitmap getBitmapFromPath(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }
}
