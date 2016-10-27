package com.xiaoyuz.comicengine.engine;

import android.app.Application;

import com.xiaoyuz.comicengine.utils.App;

/**
 * Created by zhangxiaoyu on 16/10/27.
 */
public class ComicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        App.initialize(this);
    }
}
