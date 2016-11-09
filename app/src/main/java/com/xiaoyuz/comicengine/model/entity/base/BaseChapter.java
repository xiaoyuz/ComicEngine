package com.xiaoyuz.comicengine.model.entity.base;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BaseChapter extends BaseEntity {

    protected String title;
    protected String pageInfo;
    protected String url;
    protected Integer maxPageNum;

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

    public Integer getMaxPageNum() {
        return maxPageNum;
    }

    public void setMaxPageNum(Integer maxPageNum) {
        this.maxPageNum = maxPageNum;
    }

    public abstract ArrayList<String> createPageUrlList();
}
