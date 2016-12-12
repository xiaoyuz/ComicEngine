package com.xiaoyuz.comicengine.db.source.remote;

import android.text.TextUtils;

import com.xiaoyuz.comicengine.db.source.BaseBookDataSource;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.utils.App;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class BookRemoteDataSource extends BaseBookDataSource {

    private static BookRemoteDataSource sInstance;

    private BookRemoteDataSource() {

    }

    public static BookRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new BookRemoteDataSource();
        }
        return sInstance;
    }

    // TODO: This block is just for learning, a better way is to return Observable<List> directly.
    @Override
    public Observable<BaseSearchResult> getSearchResult(final String keyword, final int page) {
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
        }).concatMap(new Func1<List<BaseSearchResult>, Observable<BaseSearchResult>>() {
            @Override
            public Observable<BaseSearchResult> call(List<BaseSearchResult> baseSearchResults) {
                return Observable.from(baseSearchResults);
            }
        });
    }

    @Override
    public Observable<BaseBookDetail> getBookDetail(final BaseSearchResult searchResult) {
        return Observable.create(new Observable.OnSubscribe<BaseBookDetail>() {
            @Override
            public void call(Subscriber<? super BaseBookDetail> subscriber) {
                BaseBookDetail bookDetail = App.getEntityFactory()
                        .createBookDetailEntity(searchResult);
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
}
