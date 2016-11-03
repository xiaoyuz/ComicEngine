package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;

import com.xiaoyuz.comicengine.contract.PageContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.entity.Page;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public class PagePresenter implements PageContract.Presenter {

    @NonNull
    private BookRepository mBookRepository;
    @NonNull
    private PageContract.View mPageView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public PagePresenter(@NonNull BookRepository bookRepository,
                         @NonNull PageContract.View pageView) {
        mBookRepository = bookRepository;
        mPageView = pageView;
        mSubscriptions= new CompositeSubscription();
        mPageView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    /**
     * Load html page for webview, output is html code.
     * @param url
     */
    @Override
    public void loadHtmlPage(String url) {
        mPageView.loadUrlByWebView(url);
    }

    /**
     * Load html code and get the page instance.
     * @param html
     */
    @Override
    public void loadPage(String html) {
        Subscription subscription = mBookRepository.getPage(html)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Page>() {
                    @Override
                    public void call(Page page) {
                        mPageView.showPage(page);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        mSubscriptions.add(subscription);
    }
}
