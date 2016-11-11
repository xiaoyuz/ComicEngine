package com.xiaoyuz.comicengine.contract.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xiaoyuz.comicengine.contract.BookDetailContract;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.history.History;

import java.util.ArrayList;

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
                .subscribe(new Action1<BaseBookDetail>() {
            @Override
            public void call(BaseBookDetail bookDetail) {
                mBookDetailView.showBookDetail(bookDetail);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mBookDetailView.showNoChapter();
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void openChapter(BaseChapter chapter, int chapterIndex) {
        ArrayList<String> pageUrls = new ArrayList<>();
        for (int i = 1; i <= chapter.getMaxPageNum(); i++) {
            pageUrls.add(chapter.getUrl() + "?p=" + i);
        }
        mBookDetailView.showChapter(chapterIndex, chapter);
    }

    @Override
    public void loadChapterHistory(String bookUrl) {
        Subscription subscription = mBookRepository.getChapterHistory(bookUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String history) {
                        if (!TextUtils.isEmpty(history)) {
                            String[] historyInfos = history.split("-");
                            mBookDetailView.setHistory(Integer.parseInt(historyInfos[0]),
                                    historyInfos[1], Integer.parseInt(historyInfos[2]));
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void saveReadHistory(History history) {
        Subscription subscription = mBookRepository.saveHistory(history)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
        mSubscriptions.add(subscription);
    }
}
