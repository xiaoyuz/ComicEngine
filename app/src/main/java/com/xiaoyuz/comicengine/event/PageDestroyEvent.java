package com.xiaoyuz.comicengine.event;

/**
 * Created by zhangxiaoyu on 16-11-8.
 */
public class PageDestroyEvent {

    private String mBookUrl;

    public PageDestroyEvent(String bookUrl) {
        mBookUrl = bookUrl;
    }

    public String getBookUrl() {
        return mBookUrl;
    }
}
