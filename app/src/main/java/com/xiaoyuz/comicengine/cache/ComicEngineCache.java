package com.xiaoyuz.comicengine.cache;

import com.xiaoyuz.comicengine.utils.App;

/**
 * Created by zhangxiaoyu on 16-11-3.
 */
public class ComicEngineCache {

    private static final int EXPIRE_TIME = 24 * 60 * 60; // seconds

    private static final String PAGE_HTML_KEY = "page_html_";
    private static final String CHAPTER_HISTORY_KEY = "chapter_history_";

    public static void putPageHtml(String url, String html) {
        App.getACache().put(PAGE_HTML_KEY + url, html, EXPIRE_TIME);
    }

    public static String getPageHtml(String url) {
        return App.getACache().getAsString(PAGE_HTML_KEY + url);
    }

    public static void putChapterHistory(String bookUrl, int chapterIndex,
                                         String chapterTitle, int position) {
        App.getACache().put(CHAPTER_HISTORY_KEY + bookUrl, String.valueOf(chapterIndex)
                + "-" + chapterTitle + "-" + String.valueOf(position));
    }

    public static String getChapterHistory(String bookUrl) {
        return App.getACache().getAsString(CHAPTER_HISTORY_KEY + bookUrl);
    }
}
