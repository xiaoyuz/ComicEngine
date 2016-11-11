package com.xiaoyuz.comicengine.model.entity.history;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by zhangxiaoyu on 16-11-11.
 */
@Entity
public class History {

    private String bookCover;
    private String status;
    @Id
    private String url;
    private String title;
    private String updateTime;
    private String lastChapter;
    private Date historyTime;

    @Generated(hash = 1145632907)
    public History(String bookCover, String status, String url, String title,
            String updateTime, String lastChapter, Date historyTime) {
        this.bookCover = bookCover;
        this.status = status;
        this.url = url;
        this.title = title;
        this.updateTime = updateTime;
        this.lastChapter = lastChapter;
        this.historyTime = historyTime;
    }

    @Generated(hash = 869423138)
    public History() {
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public Date getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(Date historyTime) {
        this.historyTime = historyTime;
    }
}
