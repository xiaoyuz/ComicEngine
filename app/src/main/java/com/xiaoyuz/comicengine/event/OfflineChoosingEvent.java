package com.xiaoyuz.comicengine.event;

import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;

/**
 * Created by zhangxiaoyu on 16-11-18.
 */
public class OfflineChoosingEvent {

    private BaseChapter mChapter;

    public OfflineChoosingEvent(BaseChapter chapter) {
        mChapter = chapter;
    }

    public BaseChapter getChapter() {
        return mChapter;
    }
}
