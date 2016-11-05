package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;

import com.xiaoyuz.comicengine.contract.PageContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
    public void saveChapterHistory(String chapterUrl, int position) {
        Subscription subscription = mBookRepository.saveChapterHistory(chapterUrl, position)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadChapterHistory(String chapterUrl) {
        Subscription subscription = mBookRepository.getChapterHistory(chapterUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer position) {
                        mView.jump2HistoryPage(position);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        mSubscriptions.add(subscription);
    }
}
