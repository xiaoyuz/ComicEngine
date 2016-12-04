package com.xiaoyuz.comicengine.event;

/**
 * Created by zhangxiaoyu on 2016/12/4.
 */
public class PageTurningEvent {

    private boolean mIsNext;
    private int mChaperIndex;

    public PageTurningEvent(boolean isNext, int chapterIndex) {
        mIsNext = isNext;
        mChaperIndex = chapterIndex;
    }

    public boolean isNext() {
        return mIsNext;
    }

    public int getChaperIndex() {
        return mChaperIndex;
    }
}
