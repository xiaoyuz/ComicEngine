package com.xiaoyuz.comicengine.base;

/**
 * A generic wrapper for instances which are to be instantiated lazily.
 *
 * @param <T> The type of the instance which will be lazily instantiated
 *
 * Created by zhangxiaoyu on 16-10-17.
 */
public class LazyInstance<T> {
    /**
     * An instance creation closure, standing in as a default constructor.
     */
    public static interface InstanceCreator<T> {
        T createInstance();
    }

    private final InstanceCreator<T> mInstanceCreator;
    protected T mInstance;

    /**
     * Creates a lazy instance of type T.
     * @param instanceCreator The instance creation closure
     */
    public LazyInstance(InstanceCreator<T> instanceCreator) {
        mInstanceCreator = instanceCreator;
    }

    /**
     * Creates a non-lazy instance (where the instance is already set).
     * @param instance The instance to use
     */
    public LazyInstance(T instance) {
        mInstance = instance;
        mInstanceCreator = null;
    }

    /**
     * @return the instance, creating it if it didn't exist
     */
    public T get() {
        if (mInstance == null) mInstance = mInstanceCreator.createInstance();
        return mInstance;
    }

    /**
     * @return whether there's an instance created or not
     */
    public boolean hasInstance() {
        return mInstance != null;
    }
}