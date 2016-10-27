package com.xiaoyuz.comicengine.entity;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16/10/27.
 */
public class SearchResult {

    private String bookCover;
    private boolean status;
    private String url;
    private String title;
    private String updateTime;
    private String lastChapter;

    public SearchResult(Element element) {
        Element detail = element.select(".book-detail").first();
        bookCover = element.select(".book-cover .bcover img").attr("src");
        status = detail.select("dd.tags.status>span>span").first().text().equals("连载中");
        url = detail.select("dl>dt>a").attr("href");
        title = detail.select("dl>dt>a").attr("title");
        updateTime = detail.select("dd.tags.status>span>span").get(1).text();
        lastChapter = detail.select("dd.tags.status>span>a").text();
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

    public boolean isStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getLastChapter() {
        return lastChapter;
    }
}