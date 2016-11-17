package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;

import com.xiaoyuz.comicengine.contract.HistoryContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistory;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxiaoyu on 16-11-11.
 */
public class HistoryPresenter implements HistoryContract.Presenter {

    @NonNull
    private BookRepository mBookRepository;
    @NonNull
    private HistoryContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public HistoryPresenter(@NonNull BookRepository bookRepository,
                               @NonNull HistoryContract.View pageView) {
        mBookRepository = bookRepository;
        mView = pageView;
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
    public void loadHistories() {
        mBookRepository.getHistories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<BaseHistory>>() {
                    @Override
                    public void call(List<BaseHistory> histories) {
                        mView.showHistories(histories);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Override
    public void openBookDetail(BaseHistory history) {
        mView.openBookDetail(history);
    }
}
