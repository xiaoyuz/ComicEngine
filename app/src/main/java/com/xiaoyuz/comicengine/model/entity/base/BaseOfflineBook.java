package com.xiaoyuz.comicengine.model.entity.base;

import com.xiaoyuz.comicengine.model.entity.ChapterListTypeConverter;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by zhangxiaoyu on 16-11-18.
 */
@Entity
public class BaseOfflineBook {
    @Id
    protected String url;
    @ToOne(joinProperty = "url")
    protected BaseBookDetail bookDetail;
    @Convert(converter = ChapterListTypeConverter.class, columnType = String.class)
    protected List<BaseChapter> chapters;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 518775653)
    private transient BaseOfflineBookDao myDao;
    @Generated(hash = 2003548009)
    private transient String bookDetail__resolvedKey;

    public BaseOfflineBook() {
    }

    @Generated(hash = 996000398)
    public BaseOfflineBook(String url, List<BaseChapter> chapters) {
        this.url = url;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 204659818)
    public BaseBookDetail getBookDetail() {
        String __key = this.url;
        if (bookDetail__resolvedKey == null || bookDetail__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BaseBookDetailDao targetDao = daoSession.getBaseBookDetailDao();
            BaseBookDetail bookDetailNew = targetDao.load(__key);
            synchronized (this) {
                bookDetail = bookDetailNew;
                bookDetail__resolvedKey = __key;
            }
        }
        return bookDetail;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1145932821)
    public void setBookDetail(BaseBookDetail bookDetail) {
        synchronized (this) {
            this.bookDetail = bookDetail;
            url = bookDetail == null ? null : bookDetail.getUrl();
            bookDetail__resolvedKey = url;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1224754925)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBaseOfflineBookDao() : null;
    }
}
