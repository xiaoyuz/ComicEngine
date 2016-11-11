package com.xiaoyuz.comicengine.db.source.remote;

import android.text.TextUtils;

import com.xiaoyuz.comicengine.db.source.BookDataSource;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.history.History;
import com.xiaoyuz.comicengine.utils.App;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class BookRemoteDataSource implements BookDataSource {

    private static BookRemoteDataSource sInstance;

    private BookRemoteDataSource() {

    }

    public static BookRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new BookRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public Observable<List<BaseSearchResult>> getSearchResults(final String keyword, final int page) {
        return Observable.create(new Observable.OnSubscribe<List<BaseSearchResult>>() {
            @Override
            public void call(Subscriber<? super List<BaseSearchResult>> subscriber) {
                List<BaseSearchResult> searchResults = App.getEntityFactory()
                        .createSearchResultEntity(keyword, page);
                if (searchResults.size() != 0) {
                    subscriber.onNext(searchResults);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new NullPointerException());
                }
            }
        });
    }

    @Override
    public Observable<BaseBookDetail> getBookDetail(final String url) {
        return Observable.create(new Observable.OnSubscribe<BaseBookDetail>() {
            @Override
            public void call(Subscriber<? super BaseBookDetail> subscriber) {
                BaseBookDetail bookDetail = App.getEntityFactory()
                        .createBookDetailEntity(url);
                if (bookDetail != null) {
                    subscriber.onNext(bookDetail);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new NullPointerException());
                }
            }
        });
    }

    @Override
    public Observable<String> getHtml(String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("");
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<BasePage> getPage(final String html) {
        return Observable.create(new Observable.OnSubscribe<BasePage>() {
            @Override
            public void call(Subscriber<? super BasePage> subscriber) {
                BasePage page = App.getEntityFactory().createPageEntity(html);
                if (!TextUtils.isEmpty(page.getImageUrl())) {
                    subscriber.onNext(page);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new NullPointerException());
                }
            }
        });
    }

    @Override
    public Observable<String> getChapterHistory(String bookUrl) {
        return null;
    }

    @Override
    public Observable<Object> saveHtml(String url, String html) {
        return null;
    }

    @Override
    public Observable<Object> saveChapterHistory(String bookUrl, int chapterIndex,
                                                 String chapterTitle, int position) {
        return null;
    }

    @Override
    public Observable<Object> saveHistory(History history) {
        return null;
    }

    @Override
    public Observable<List<History>> getHistories() {
        return null;
    }
}
