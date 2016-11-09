package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;

import com.xiaoyuz.comicengine.contract.PageContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxiaoyu on 16-11-5.
 */
public class PagePresenter implements PageContract.Presenter {

    @NonNull
    private BookRepository mBookRepository;
    @NonNull
    private PageContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public PagePresenter(@NonNull BookRepository bookRepository,
                         @NonNull PageContract.View view) {
        mBookRepository = bookRepository;
        mView = view;
        mSubscriptions= new CompositeSubscription();
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void saveChapterHistory(String bookUrl, int chapterIndex, String chapterTitle,
                                   int pagePosition) {
        Subscription subscription = mBookRepository.saveChapterHistory(bookUrl,
                chapterIndex, chapterTitle, pagePosition)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
        mSubscriptions.add(subscription);
    }
}
