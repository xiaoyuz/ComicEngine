package com.xiaoyuz.comicengine.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xiaoyuz.comicengine.base.LazyInstance;

import java.util.Set;

/**
 * Created by zhangxiaoyu on 16-10-17.
 */
public class SharePreferenceUtils {

    private static final String TAG = "SharePreferenceUtils";
    private static final String PREFS_STORE = "reading_assistant";

    private static final LazyInstance<SharedPreferences> mLazySharedPreferences =
            new LazyInstance<>(new LazyInstance.InstanceCreator<SharedPreferences>() {
                @Override
                public SharedPreferences createInstance() {
                    return App.getContext().getSharedPreferences(PREFS_STORE,
                            Context.MODE_PRIVATE);
                }
            });

    private static SharedPreferences getPrefs() {
        return mLazySharedPreferences.get();
    }

    public static int getInt(String key, int defValue) {
        return getPrefs().getInt(key, defValue);
    }

    public static void putInt(String key, int value) {
        getPrefs().edit().putInt(key, value).apply();
    }

    public static long getLong(String key, long defValue) {
        return getPrefs().getLong(key, defValue);
    }

    public static void putLong(String key, long value) {
        getPrefs().edit().putLong(key, value).apply();
    }

    public static float getFloat(String key, float defValue) {
        return getPrefs().getFloat(key, defValue);
    }

    public static void putFloat(String key, float value) {
        getPrefs().edit().putFloat(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getPrefs().getBoolean(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).apply();
    }

    @Nullable
    public static String getString(String key, @Nullable String defValue) {
        return getPrefs().getString(key, defValue);
    }

    public static void putString(String key, @Nullable String value) {
        getPrefs().edit().putString(key, value).apply();
    }

    @Nullable
    public static Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        try {
            return getPrefs().getStringSet(key, defValues);
        } catch (ClassCastException e) {
            Log.e(TAG, "getStringSet failed, key=" + key, e);
        }
        return null;
    }

    public static void putStringSet(String key, @Nullable Set<String> values) {
        getPrefs().edit().putStringSet(key, values).apply();
    }

    public static boolean contains(String key) {
        return getPrefs().contains(key);
    }

    public static void remove(String key) {
        getPrefs().edit().remove(key).apply();
    }

    public static void removeIfContains(String key) {
        if (contains(key)) {
            remove(key);
        }
    }
}
