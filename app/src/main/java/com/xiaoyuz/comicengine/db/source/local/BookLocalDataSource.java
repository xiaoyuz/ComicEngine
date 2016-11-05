package com.xiaoyuz.comicengine.db.source.local;

import com.xiaoyuz.comicengine.cache.ComicEngineCache;
import com.xiaoyuz.comicengine.db.source.BookDataSource;
import com.xiaoyuz.comicengine.entity.BookDetail;
import com.xiaoyuz.comicengine.entity.Page;
import com.xiaoyuz.comicengine.entity.SearchResult;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
                String html = ComicEngineCache.getPageHtml(url);
                subscriber.onNext(html);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Page> getPage(String html) {
        return null;
    }

    @Override
    public Observable<Integer> getChapterHistory(final String chapterUrl) {
        int a = 1;
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(ComicEngineCache.getChapterHistory(chapterUrl));
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
    public Observable<Object> saveChapterHistory(final String chapterUrl, final int position) {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                ComicEngineCache.putChapterHistory(chapterUrl, position);
            }
        });
    }
}
