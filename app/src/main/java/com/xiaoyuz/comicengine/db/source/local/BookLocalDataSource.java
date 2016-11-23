package com.xiaoyuz.comicengine.db.source.local;

import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.cache.ComicEngineCache;
import com.xiaoyuz.comicengine.db.source.BaseBookDataSource;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistory;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistoryDao;
import com.xiaoyuz.comicengine.model.entity.base.BaseOfflineBook;
import com.xiaoyuz.comicengine.model.entity.base.BaseOfflineBookDao;
import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.Constants;

import org.greenrobot.greendao.query.CountQuery;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhangxiaoyu on 16/11/3.
 */
public class BookLocalDataSource extends BaseBookDataSource {

    private static BookLocalDataSource sInstance;
    private LazyInstance<BaseHistoryDao> mLazyHistoryDao
            = new LazyInstance<>(new LazyInstance.InstanceCreator<BaseHistoryDao>() {
        @Override
        public BaseHistoryDao createInstance() {
            return App.getDaoSession().getBaseHistoryDao();
        }
    });
    private LazyInstance<BaseOfflineBookDao> mLazyOfflineBookDao
            = new LazyInstance<>(new LazyInstance.InstanceCreator<BaseOfflineBookDao>() {
        @Override
        public BaseOfflineBookDao createInstance() {
            return App.getDaoSession().getBaseOfflineBookDao();
        }
    });

    private BookLocalDataSource() {

    }

    public static BookLocalDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new BookLocalDataSource();
        }
        return sInstance;
    }

    @Override
    public Observable<String> getHtml(final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String html = ComicEngineCache.getPageHtml(url);
                subscriber.onNext(html);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<String> getChapterHistory(final String bookUrl) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(ComicEngineCache.getChapterHistory(bookUrl));
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Object> saveHtml(final String url, final String html) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                ComicEngineCache.putPageHtml(url, html);
            }
        });
    }

    @Override
    public Observable<Object> saveChapterHistory(final String bookUrl, final int chapterIndex,
                                                 final String chapterTitle, final int position) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                ComicEngineCache.putChapterHistory(bookUrl, chapterIndex, chapterTitle, position);
            }
        });
    }

    @Override
    public Observable<Object> saveHistory(final BaseHistory history) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                mLazyHistoryDao.get().getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        mLazyHistoryDao.get().insertOrReplace(history);
                        CountQuery<BaseHistory> countQuery = mLazyHistoryDao.get()
                                .queryBuilder().buildCount();
                        if (countQuery.count() > Constants.Database.MAX_HISTORY_COUNT) {
                            deleteOldestHistory();
                        }
                    }
                });
            }
        });
    }

    @Override
    public Observable<List<BaseHistory>> getHistories() {
        return Observable.create(new Observable.OnSubscribe<List<BaseHistory>>() {
            @Override
            public void call(Subscriber<? super List<BaseHistory>> subscriber) {
                Query<BaseHistory> query = mLazyHistoryDao.get().queryBuilder()
                        .orderDesc(BaseHistoryDao.Properties.HistoryTime).build();
                subscriber.onNext(query.list());
                subscriber.onCompleted();
            }
        });
    }

    private void deleteOldestHistory() {
        Query<BaseHistory> query = mLazyHistoryDao.get().queryBuilder()
                .offset(0).limit(1).orderAsc(BaseHistoryDao.Properties.HistoryTime).build();
        mLazyHistoryDao.get().delete(query.list().get(0));
    }

    @Override
    public Observable<Object> addOfflineChapter(final BaseBookDetail bookDetail,
                                                final BaseChapter chapter) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                mLazyOfflineBookDao.get().getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        Query<BaseOfflineBook> query = mLazyOfflineBookDao.get().queryBuilder()
                                .where(BaseOfflineBookDao.Properties.Url.eq(bookDetail.getUrl()))
                                .build();
                        List<BaseOfflineBook> offlineBooks = query.list();
                        BaseOfflineBook offlineBook;
                        if (!offlineBooks.isEmpty()) {
                            offlineBook = offlineBooks.get(0);
                            List<BaseChapter> chapters = offlineBook.getChapters();
                            chapters.add(chapter);
                            offlineBook.setChapters(chapters);
                        } else {
                            offlineBook = new BaseOfflineBook();
                            offlineBook.setUrl(bookDetail.getUrl());
                            offlineBook.setBookDetail(bookDetail);
                            List<BaseChapter> list = new ArrayList<>();
                            list.add(chapter);
                            offlineBook.setChapters(list);
                        }
                        mLazyOfflineBookDao.get().insertOrReplace(offlineBook);
                    }
                });
            }
        });
    }

    @Override
    public Observable<Object> deleteOfflineChapter(final BaseBookDetail bookDetail,
                                                   final BaseChapter chapter) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                mLazyOfflineBookDao.get().getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        Query<BaseOfflineBook> query = mLazyOfflineBookDao.get().queryBuilder()
                                .where(BaseOfflineBookDao.Properties.Url.eq(bookDetail.getUrl()))
                                .build();
                        List<BaseOfflineBook> offlineBooks = query.list();
                        if (!offlineBooks.isEmpty()) {
                            BaseOfflineBook offlineBook = offlineBooks.get(0);
                            offlineBook.getChapters().remove(chapter);
                            if (offlineBook.getChapters().size() != 0) {
                                mLazyOfflineBookDao.get().insertOrReplace(offlineBook);
                            } else {
                                mLazyOfflineBookDao.get().delete(offlineBook);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public Observable<List<BaseChapter>> getOfflineChapters(final BaseBookDetail bookDetail) {
        return Observable.create(new Observable.OnSubscribe<List<BaseChapter>>() {
            @Override
            public void call(Subscriber<? super List<BaseChapter>> subscriber) {
                Query<BaseOfflineBook> query = mLazyOfflineBookDao.get().queryBuilder()
                        .where(BaseOfflineBookDao.Properties.Url.eq(bookDetail.getUrl())).build();
                List<BaseOfflineBook> offlineBooks = query.list();
                if (!offlineBooks.isEmpty()) {
                    subscriber.onNext(query.list().get(0).getChapters());
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public Observable<List<BaseOfflineBook>> getAllOfflineBooks() {
        return Observable.create(new Observable.OnSubscribe<List<BaseOfflineBook>>() {
            @Override
            public void call(Subscriber<? super List<BaseOfflineBook>> subscriber) {
                Query<BaseOfflineBook> query = mLazyOfflineBookDao.get().queryBuilder().build();
                List<BaseOfflineBook> offlineBooks = query.list();
                subscriber.onNext(offlineBooks);
                subscriber.onCompleted();
            }
        });
    }
}
