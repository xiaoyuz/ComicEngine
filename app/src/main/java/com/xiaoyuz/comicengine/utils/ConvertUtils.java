package com.xiaoyuz.comicengine.utils;

import android.text.TextUtils;

/**
 * Created by zhangxiaoyu on 16-10-11.
 * Convert object to some other types safely.
 */
public class ConvertUtils {

    public final static int convert2Int(Object value, int defaultValue) {
        if (value == null || TextUtils.equals(value.toString().trim(), "")) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public final static long convert2Long(Object value, long defaultValue) {
        if (value == null || TextUtils.equals(value.toString().trim(), "")) {
            return defaultValue;
        }
        try {
            return Long.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).longValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

}