package com.xiaoyuz.comicengine.db.source.local;

import com.xiaoyuz.comicengine.cache.ComicEngineCache;
import com.xiaoyuz.comicengine.db.source.BookDataSource;
import com.xiaoyuz.comicengine.entity.BookDetail;
import com.xiaoyuz.comicengine.entity.Page;
import com.xiaoyuz.comicengine.entity.SearchResult;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhangxiaoyu on 16/11/3.
 */
public class BookLocalDataSource implements BookDataSource {

    private static BookLocalDataSource sInstance;

    private BookLocalDataSource() {

    }

    public static BookLocalDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new BookLocalDataSource();
        }
        return sInstance;
    }

    @Override
    public Observable<List<SearchResult>> getSearchResults(String keyword, int page) {
        return null;
    }

    @Override
    public Observable<BookDetail> getBookDetail(String url) {
        return null;
    }

    @Override
    public Observable<String> getHtml(final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(ComicEngineCache.getPageHtml(url));
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Page> getPage(String html) {
        return null;
    }
}
