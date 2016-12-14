package com.xiaoyuz.comicengine.utils;

import android.app.Activity;
import android.content.Context;

import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.cache.ACache;
import com.xiaoyuz.comicengine.engine.exception.CrashHandler;
import com.xiaoyuz.comicengine.model.entity.base.DaoMaster;
import com.xiaoyuz.comicengine.model.entity.base.DaoSession;
import com.xiaoyuz.comicengine.model.factory.BaseEntityFactory;
import com.xiaoyuz.comicengine.model.factory.Mh57EntityFactory;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-11.
 */
public class App {

    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;

    private static Context sAppContext;
    private static LazyInstance<ACache> sLazyACache =
            new LazyInstance<>(new LazyInstance.InstanceCreator<ACache>() {
                @Override
                public ACache createInstance() {
                    return ACache.get(getContext());
                }
            });

    private static LazyInstance<Mh57EntityFactory> sLazyMh57EntityFactory =
            new LazyInstance<>(new LazyInstance.InstanceCreator<Mh57EntityFactory>() {
                @Override
                public Mh57EntityFactory createInstance() {
                    return new Mh57EntityFactory();
                }
            });

    public static void initialize(Context context) {
        sAppContext = context;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getContext());
    }

    private static LazyInstance<List<Activity>> sLazyActivityList =
            new LazyInstance<>(new LazyInstance.InstanceCreator<List<Activity>>() {
                @Override
                public List<Activity> createInstance() {
                    return new ArrayList<>();
                }
            });

    public static boolean isInitialized() {
        return sAppContext != null;
    }

    public static Context getContext() {
        return sAppContext;
    }

    public static ACache getACache() {
        return sLazyACache.get();
    }

    public static BaseEntityFactory getEntityFactory() {
        return sLazyMh57EntityFactory.get();
    }

    public static DaoSession getDaoSession() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(sAppContext,
                ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") :
                helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }

    public static void removeActivity(Activity activity){
        sLazyActivityList.get().remove(activity);
    }

    public static void addActivity(Activity activity){
        sLazyActivityList.get().add(activity);
    }

    public static void finishActivity(){
        for (Activity activity : sLazyActivityList.get()) {
            if (null != activity) {
                activity.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}