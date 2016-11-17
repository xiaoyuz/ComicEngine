package com.xiaoyuz.comicengine.model.entity.base;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.jsoup.nodes.Element;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
@Entity
public class BaseSearchResult extends BaseEntity implements Parcelable {

    public static final Parcelable.Creator<BaseSearchResult> CREATOR = new Creator(){

        @Override
        public BaseSearchResult createFromParcel(Parcel source) {
            BaseSearchResult searchResult = new BaseSearchResult();
            searchResult.setBookCover(source.readString());
            searchResult.setStatus(source.readString());
            searchResult.setUrl(source.readString());
            searchResult.setTitle(source.readString());
            searchResult.setUpdateTime(source.readString());
            searchResult.setLastChapter(source.readString());
            return searchResult;
        }

        @Override
        public BaseSearchResult[] newArray(int size) {
            return new BaseSearchResult[size];
        }
    };

    protected String bookCover;
    protected String status;
    @Id
    protected String url;
    protected String title;
    protected String updateTime;
    protected String lastChapter;

    public BaseSearchResult() {
    }

    public BaseSearchResult(Element element) {
    }

    @Generated(hash = 1366281907)
    public BaseSearchResult(String bookCover, String status, String url,
            String title, String updateTime, String lastChapter) {
        this.bookCover = bookCover;
        this.status = status;
        this.url = url;
        this.title = title;
        this.updateTime = updateTime;
        this.lastChapter = lastChapter;
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

    public void fromHistory(BaseHistory history) {
        setUrl(history.getUrl());
        setLastChapter(history.getLastChapter());
        setStatus(history.getStatus());
        setUpdateTime(history.getUpdateTime());
        setTitle(history.getTitle());
        setBookCover(history.getBookCover());
    }

    public BaseHistory toHistory() {
        BaseHistory history = new BaseHistory();
        history.setBookCover(getBookCover());
        history.setTitle(getTitle());
        history.setUpdateTime(getUpdateTime());
        history.setStatus(getStatus());
        history.setLastChapter(getLastChapter());
        history.setUrl(getUrl());
        history.setHistoryTime(new Date());
        return history;
    }
}
