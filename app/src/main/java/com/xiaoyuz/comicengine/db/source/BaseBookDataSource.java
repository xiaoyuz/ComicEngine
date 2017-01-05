package com.xiaoyuz.comicengine.db.source;

import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.base.BaseOfflineBook;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistory;

import java.util.List;

import rx.Observable;

/**
 * Created by zhangxiaoyu on 16-11-17.
 */
public class BaseBookDataSource implements BookDataSource {

    @Override
    public Observable<BaseSearchResult> getSearchResult(final String keyword,
                                                              final int page) {
        return Observable.empty();
    }

    @Override
    public Observable<BaseBookDetail> getBookDetail(final BaseSearchResult searchResult) {
        return Observable.empty();
    }

    @Override
    public Observable<String> getHtml(String url) {
        return Observable.empty();
    }

    @Override
    public Observable<BasePage> getPage(final String html) {
        return Observable.empty();
    }

    @Override
    public Observable<String> getChapterHistory(String bookUrl) {
        return Observable.empty();
    }

    @Override
    public Observable<Object> saveHtml(String url, String html) {
        return Observable.empty();
    }

    @Override
    public Observable<Object> saveChapterHistory(String bookUrl, int chapterIndex,
                                                 String chapterTitle, int position) {
        return Observable.empty();
    }

    @Override
    public Observable<Object> saveHistory(BaseHistory history) {
        return Observable.empty();
    }

    @Override
    public Observable<List<BaseHistory>> getHistories() {
        return Observable.empty();
    }

    @Override
    public Observable<Boolean> deleteHistory(BaseHistory history) {
        return Observable.empty();
    }

    @Override
    public Observable<Object> addOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter) {
        return Observable.empty();
    }

    @Override
    public Observable<Object> deleteOfflineChapter(BaseBookDetail bookDetail,
                                                   BaseChapter chapter) {
        return Observable.empty();
    }

    @Override
    public Observable<List<BaseChapter>> getOfflineChapters(BaseBookDetail bookDetail) {
        return Observable.empty();
    }

    @Override
    public Observable<List<BaseOfflineBook>> getAllOfflineBooks() {
        return Observable.empty();
    }
}
