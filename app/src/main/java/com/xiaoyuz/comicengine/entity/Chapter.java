package com.xiaoyuz.comicengine.entity;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class Chapter {

    private String title;
    private String pageInfo;
    private String url;

    public Chapter(Element element) {
        title = element.select("a").attr("title");
        url = element.select("a").attr("href");
        pageInfo = element.select("a span i").text();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(String pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
