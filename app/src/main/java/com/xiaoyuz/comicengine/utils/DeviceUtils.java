package com.xiaoyuz.comicengine.utils;

import android.app.Activity;

/**
 * Created by zhangxiaoyu on 16-12-1.
 */
public class DeviceUtils {

    public static void rotateScreen(Activity activity, int orientation) {
        activity.setRequestedOrientation(orientation);
    }
}
