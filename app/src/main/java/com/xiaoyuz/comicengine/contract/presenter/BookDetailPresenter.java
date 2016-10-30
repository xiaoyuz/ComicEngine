package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;

import com.xiaoyuz.comicengine.contract.BookDetailContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.entity.BookDetail;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxiaoyu on 16/10/29.
 */
public class BookDetailPresenter implements BookDetailContract.Presenter {

    @NonNull
    private BookRepository mBookRepository;
    @NonNull
    private BookDetailContract.View mBookDetailView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public BookDetailPresenter(@NonNull BookRepository bookRepository,
                               @NonNull BookDetailContract.View bookDetailView) {
        mBookDetailView = bookDetailView;
        mBookRepository = bookRepository;
        mSubscriptions= new CompositeSubscription();
        mBookDetailView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadBookDetail(String url) {
        Subscription subscription = mBookRepository.getBookDetail(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BookDetail>() {
            @Override
            public void call(BookDetail bookDetail) {
                mBookDetailView.showBookDetail(bookDetail);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
        mSubscriptions.add(subscription);
    }
}
