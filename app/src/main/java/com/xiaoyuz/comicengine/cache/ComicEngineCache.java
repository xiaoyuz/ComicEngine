package com.xiaoyuz.comicengine.cache;

import android.graphics.Bitmap;

import com.xiaoyuz.comicengine.utils.App;

/**
 * Created by zhangxiaoyu on 16-11-3.
 */
public class ComicEngineCache {

    private static final int EXPIRE_TIME = 600; // seconds

    private static final String PAGE_HTML_KEY = "page_html_";
    private static final String COMIC_PIC_KEY = "comic_pic_";

    public static void putPageHtml(String url, String html) {
        App.getACache().put(PAGE_HTML_KEY + url, html, EXPIRE_TIME);
    }

    public static String getPageHtml(String url) {
        return App.getACache().getAsString(PAGE_HTML_KEY + url);
    }

    public static void putComicPic(String url, Bitmap bitmap) {
        App.getACache().put(COMIC_PIC_KEY + url, bitmap);
    }

    public static Bitmap getComicPic(String url) {
        return App.getACache().getAsBitmap(COMIC_PIC_KEY + url);
    }
}
