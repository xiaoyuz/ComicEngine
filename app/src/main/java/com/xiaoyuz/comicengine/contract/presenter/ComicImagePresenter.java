package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xiaoyuz.comicengine.contract.ComicImageContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public class ComicImagePresenter implements ComicImageContract.Presenter {

    @NonNull
    private BookRepository mBookRepository;
    @NonNull
    private ComicImageContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;

    public ComicImagePresenter(@NonNull BookRepository bookRepository,
                               @NonNull ComicImageContract.View pageView) {
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

    /**
     * Load html page for webview, output is html code.
     * @param url
     */
    @Override
    public void loadHtmlPage(final String url) {
        mView.setUrl(url);
        Subscription subscription = mBookRepository.getHtml(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String html) {
                        if (TextUtils.isEmpty(html)) {
                            // Load remotely
                            mView.loadUrlByWebView(url);
                        } else {
                            // local
                            loadPage(false, url, html);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showNetError();
                    }
                });
        mSubscriptions.add(subscription);
    }

    /**
     * Load html code and get the page instance.
     * @param html
     */
    @Override
    public void loadPage(final boolean isRemote, final String url, final String html) {
        Subscription subscription = mBookRepository.getPage(html)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BasePage>() {
                    @Override
                    public void call(BasePage page) {
                        if(isRemote) {
                            saveHtmlToLocal(url, html);
                        }
                        mView.showPage(page);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mView.showNetError();
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void saveHtmlToLocal(String url, String html) {
        Subscription subscription = mBookRepository.saveHtml(url, html)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
        mSubscriptions.add(subscription);
    }
}
