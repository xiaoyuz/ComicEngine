package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;

import com.xiaoyuz.comicengine.contract.SearchResultContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class SearchResultPresenter implements SearchResultContract.Presenter {

    @NonNull
    private final BookRepository mBookRepository;
    @NonNull
    private final SearchResultContract.View mSearchResultView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public SearchResultPresenter(@NonNull BookRepository searchResultRepository,
                                 @NonNull SearchResultContract.View searchResultView) {
        mBookRepository = searchResultRepository;
        mSearchResultView = searchResultView;
        mSubscriptions= new CompositeSubscription();
        mSearchResultView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadSearchResults(String keyword, int page) {
        Subscription subscription = mBookRepository.getSearchResult(keyword, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Action1<List<BaseSearchResult>>() {
                    @Override
                    public void call(List<BaseSearchResult> searchResults) {
                        mSearchResultView.showSearchResults(searchResults);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mSearchResultView.showNoResult();
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void openBookDetail(final BaseSearchResult searchResult) {
        mSearchResultView.openBookDetail(searchResult);
    }
}
