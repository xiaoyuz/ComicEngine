package com.xiaoyuz.comicengine.cache;

import android.text.TextUtils;

import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.ConvertUtils;

/**
 * Created by zhangxiaoyu on 16-11-3.
 */
public class ComicEngineCache {

    private static final int EXPIRE_TIME = 600; // seconds

    private static final String PAGE_HTML_KEY = "page_html_";
    private static final String CHAPTER_HISTORY_KEY = "chapter_history_";

    public static void putPageHtml(String url, String html) {
        App.getACache().put(PAGE_HTML_KEY + url, html, EXPIRE_TIME);
    }

    public static String getPageHtml(String url) {
        return App.getACache().getAsString(PAGE_HTML_KEY + url);
    }

    public static void putChapterHistory(String chapterUrl, int position) {
        App.getACache().put(CHAPTER_HISTORY_KEY + chapterUrl, String.valueOf(position));
    }

    public static int getChapterHistory(String chapterUrl) {
        String string = App.getACache().getAsString(CHAPTER_HISTORY_KEY + chapterUrl);
        try {
            int position = Integer.parseInt(string);
            return position;
        } catch (Exception e) {
            return 0;
        }
    }
}
