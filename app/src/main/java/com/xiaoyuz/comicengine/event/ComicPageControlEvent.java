package com.xiaoyuz.comicengine.event;

/**
 * Created by zhangxiaoyu on 16-11-5.
 */
public class ComicPageControlEvent {

    public static final int SINGLE_CLICK_TYPE = 1;
    public static final int FLIP_TYPE = 2;

    private int mType;

    public ComicPageControlEvent(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }
}
