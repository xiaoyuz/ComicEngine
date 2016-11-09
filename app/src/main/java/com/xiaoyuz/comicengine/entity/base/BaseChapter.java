package com.xiaoyuz.comicengine.entity.base;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BaseChapter extends BaseEntity {

    protected String title;
    protected String pageInfo;
    protected String url;

    public BaseChapter(Element element) {
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
