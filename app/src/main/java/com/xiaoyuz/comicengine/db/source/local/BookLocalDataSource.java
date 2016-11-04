package com.xiaoyuz.comicengine.db.source.local;

import android.graphics.Bitmap;

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
    public Observable<Bitmap> getComicPic(final String url) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(ComicEngineCache.getComicPic(url));
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<Page> getPage(String html) {
        return null;
    }

    @Override
    public void saveHtml(String url, final String html) {
        Observable.just(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        ComicEngineCache.putPageHtml(url, html);
                    }
                });
    }

    @Override
    public void saveComicPic(String url, final Bitmap bitmap) {
        Observable.just(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        ComicEngineCache.putComicPic(url, bitmap);
                    }
                });
    }
}
