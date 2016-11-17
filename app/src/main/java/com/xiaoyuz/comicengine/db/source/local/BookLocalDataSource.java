package com.xiaoyuz.comicengine.db.source.local;

import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.cache.ComicEngineCache;
import com.xiaoyuz.comicengine.db.source.BaseBookDataSource;
import com.xiaoyuz.comicengine.db.source.BookDataSource;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.history.History;
import com.xiaoyuz.comicengine.model.entity.history.HistoryDao;
import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.Constants;

import org.greenrobot.greendao.query.CountQuery;
import org.greenrobot.greendao.query.Query;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhangxiaoyu on 16/11/3.
 */
public class BookLocalDataSource extends BaseBookDataSource {

    private static BookLocalDataSource sInstance;
    private LazyInstance<HistoryDao> mLazyHistoryDao
            = new LazyInstance<>(new LazyInstance.InstanceCreator<HistoryDao>() {
        @Override
        public HistoryDao createInstance() {
            return App.getDaoSession().getHistoryDao();
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
    public Observable<Object> saveHistory(final History history) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                mLazyHistoryDao.get().getSession().runInTx(new Runnable() {
                    @Override
                    public void run() {
                        mLazyHistoryDao.get().insertOrReplace(history);
                        CountQuery<History> countQuery = mLazyHistoryDao.get().queryBuilder().buildCount();
                        if (countQuery.count() > Constants.Database.MAX_HISTORY_COUNT) {
                            deleteOldestHistory();
                        }
                    }
                });
            }
        });
    }

    @Override
    public Observable<List<History>> getHistories() {
        return Observable.create(new Observable.OnSubscribe<List<History>>() {
            @Override
            public void call(Subscriber<? super List<History>> subscriber) {
                Query<History> query = mLazyHistoryDao.get().queryBuilder()
                        .orderDesc(HistoryDao.Properties.HistoryTime).build();
                subscriber.onNext(query.list());
                subscriber.onCompleted();
            }
        });
    }

    private void deleteOldestHistory() {
        Query<History> query = mLazyHistoryDao.get().queryBuilder()
                .offset(0).limit(1).orderAsc(HistoryDao.Properties.HistoryTime).build();
        mLazyHistoryDao.get().delete(query.list().get(0));
    }
}
