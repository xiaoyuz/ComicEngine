package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;

import com.xiaoyuz.comicengine.contract.OfflineContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.model.entity.base.BaseOfflineBook;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxiaoyu on 16-11-22.
 */
public class OfflinePresenter implements OfflineContract.Presenter {

    @NonNull
    private BookRepository mBookRepository;
    @NonNull
    private OfflineContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public OfflinePresenter(@NonNull BookRepository bookRepository,
                                    @NonNull OfflineContract.View offlineView) {
        mView = offlineView;
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
    public void loadOfflineBooks() {
        Subscription subscription = mBookRepository.getAllOfflineBooks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BaseOfflineBook>>() {
                    @Override
                    public void call(List<BaseOfflineBook> baseOfflineBooks) {
                        mView.showOfflineBooks(baseOfflineBooks);
                    }
                });
        mSubscriptions.add(subscription);
    }
}
