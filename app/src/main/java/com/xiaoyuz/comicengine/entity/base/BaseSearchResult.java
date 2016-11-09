package com.xiaoyuz.comicengine.entity.base;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BaseSearchResult extends BaseEntity {

    protected String bookCover;
    protected String status;
    protected String url;
    protected String title;
    protected String updateTime;
    protected String lastChapter;

    public BaseSearchResult() {
    }

    public BaseSearchResult(Element element) {
    }


    @Override
    public boolean equals(Object obj) {
        return url.equals(((BaseSearchResult) obj).url)
                && title.equals(((BaseSearchResult) obj).title);
    }

    @Override
    public String toString() {
        return new StringBuffer().append("Cover: ").append(bookCover).append("\n")
                .append("status: ").append(status).append("\n")
                .append("url: ").append(url).append("\n")
                .append("title: ").append(title).append("\n")
                .append("updatetime: ").append(updateTime).append("\n")
                .append("lastChaper: ").append(lastChapter).toString();
    }

    public String getBookCover() {
        return bookCover;
    }

    public String getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }
}
