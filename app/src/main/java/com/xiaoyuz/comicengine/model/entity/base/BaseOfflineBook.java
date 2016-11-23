package com.xiaoyuz.comicengine.model.entity.base;

import com.xiaoyuz.comicengine.model.entity.BookDetailTypeConverter;
import com.xiaoyuz.comicengine.model.entity.ChapterListTypeConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangxiaoyu on 16-11-18.
 */
@Entity
public class BaseOfflineBook {
    @Id
    protected String url;
    @Convert(converter = BookDetailTypeConverter.class, columnType = String.class)
    protected BaseBookDetail bookDetail;
    @Convert(converter = ChapterListTypeConverter.class, columnType = String.class)
    protected List<BaseChapter> chapters;
    public BaseOfflineBook() {
    }

    @Generated(hash = 2162795)
    public BaseOfflineBook(String url, BaseBookDetail bookDetail,
            List<BaseChapter> chapters) {
        this.url = url;
        this.bookDetail = bookDetail;
        this.chapters = chapters;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<BaseChapter> getChapters() {
        if (chapters == null) {
            chapters = new ArrayList<>();
        }
        return this.chapters;
    }

    public void setChapters(List<BaseChapter> chapters) {
        this.chapters = chapters;
    }

    public BaseBookDetail getBookDetail() {
        return bookDetail;
    }

    public void setBookDetail(BaseBookDetail bookDetail) {
        this.bookDetail = bookDetail;
    }
}
