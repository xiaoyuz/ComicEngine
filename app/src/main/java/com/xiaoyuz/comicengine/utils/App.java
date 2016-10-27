package com.xiaoyuz.comicengine.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.view.WindowManager;

import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.cache.ACache;

/**
 * Created by zhangxiaoyu on 16-10-11.
 */
public class App {

    private static Context sAppContext;
    private static LazyInstance<ACache> sLazyACache =
            new LazyInstance<>(new LazyInstance.InstanceCreator<ACache>() {
                @Override
                public ACache createInstance() {
                    return ACache.get(getContext());
                }
            });
    private static LazyInstance<WindowManager.LayoutParams> sLazyWindowParams =
            new LazyInstance<>(new LazyInstance.InstanceCreator<WindowManager.LayoutParams>() {
                @Override
                public WindowManager.LayoutParams createInstance() {
                    return new WindowManager.LayoutParams();
                }
            });
    private static LazyInstance<ActivityManager> sLazyActivityManager =
            new LazyInstance<>(new LazyInstance.InstanceCreator<ActivityManager>() {
                @Override
                public ActivityManager createInstance() {
                    return (ActivityManager) sAppContext
                            .getSystemService(Context.ACTIVITY_SERVICE);
                }
            });

    public static void initialize(Context context) {
        sAppContext = context;
    }

    public static boolean isInitialized() {
        return sAppContext != null;
    }

    public static Context getContext() {
        return sAppContext;
    }

    public static ACache getACache() {
        return sLazyACache.get();
    }

    public static WindowManager.LayoutParams getWindowParams() {
        return sLazyWindowParams.get();
    }

    public static ActivityManager getActivityManager() {
        return sLazyActivityManager.get();
    }
}