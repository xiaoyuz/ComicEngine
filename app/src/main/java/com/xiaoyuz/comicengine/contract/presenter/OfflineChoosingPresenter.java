package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;

import com.xiaoyuz.comicengine.contract.OfflineChoosingContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxiaoyu on 16-11-18.
 */
public class OfflineChoosingPresenter implements OfflineChoosingContract.Presenter {

    @NonNull
    private BookRepository mBookRepository;
    @NonNull
    private OfflineChoosingContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public OfflineChoosingPresenter(@NonNull BookRepository bookRepository,
                               @NonNull OfflineChoosingContract.View offlineChoosingView) {
        mView = offlineChoosingView;
        mBookRepository = bookRepository;
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
    public void addOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter) {
        Subscription subscription = mBookRepository.addOfflineChapter(bookDetail, chapter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mSubscriptions.add(subscription);
    }

    @Override
    public void deleteOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter) {
        Subscription subscription = mBookRepository.deleteOfflineChapter(bookDetail, chapter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        mSubscriptions.add(subscription);
    }

    @Override
    public void loadOfflineChapters(BaseBookDetail bookDetail) {
        Subscription subscription = mBookRepository.getOfflineChapters(bookDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BaseChapter>>() {
                    @Override
                    public void call(List<BaseChapter> chapters) {
                        mView.showOfflineChapters(chapters);
                    }
                });
        mSubscriptions.add(subscription);
    }
}
