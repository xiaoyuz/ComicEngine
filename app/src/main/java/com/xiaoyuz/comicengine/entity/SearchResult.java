package com.xiaoyuz.comicengine.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16/10/27.
 */
public class SearchResult implements Parcelable {

    private String bookCover;
    private String status;
    private String url;
    private String title;
    private String updateTime;
    private String lastChapter;

    public static final Parcelable.Creator<SearchResult> CREATOR = new Creator(){

        @Override
        public SearchResult createFromParcel(Parcel source) {
            SearchResult searchResult = new SearchResult();
            searchResult.setBookCover(source.readString());
            searchResult.setStatus(source.readString());
            searchResult.setUrl(source.readString());
            searchResult.setTitle(source.readString());
            searchResult.setUpdateTime(source.readString());
            searchResult.setLastChapter(source.readString());
            return searchResult;
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    public SearchResult() {

    }

    public SearchResult(Element element) {
        bookCover = element.select(".book-cover .bcover img").attr("src");
        status = element.select(".book-detail dd.tags.status>span>span")
                .first().text();
        url = element.select(".book-detail dl>dt>a").attr("href");
        title = element.select(".book-detail dl>dt>a").attr("title");
        updateTime = element.select(".book-detail dd.tags.status>span>span").get(1).text();
        lastChapter = element.select(".book-detail dd.tags.status>span>a").text();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookCover);
        dest.writeString(status);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(updateTime);
        dest.writeString(lastChapter);
    }

    @Override
    public boolean equals(Object obj) {
        return url.equals(((SearchResult) obj).url) && title.equals(((SearchResult) obj).title);
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
