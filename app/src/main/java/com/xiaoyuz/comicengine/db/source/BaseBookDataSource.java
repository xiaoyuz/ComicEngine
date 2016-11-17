package com.xiaoyuz.comicengine.db.source;

import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.history.History;

import java.util.List;

import rx.Observable;

/**
 * Created by zhangxiaoyu on 16-11-17.
 */
public class BaseBookDataSource implements BookDataSource {

    @Override
    public Observable<List<BaseSearchResult>> getSearchResults(final String keyword,
                                                               final int page) {
        return null;
    }

    @Override
    public Observable<BaseBookDetail> getBookDetail(final String url) {
        return null;
    }

    @Override
    public Observable<String> getHtml(String url) {
        return null;
    }

    @Override
    public Observable<BasePage> getPage(final String html) {
        return null;
    }

    @Override
    public Observable<String> getChapterHistory(String bookUrl) {
        return null;
    }

    @Override
    public Observable<Object> saveHtml(String url, String html) {
        return null;
    }

    @Override
    public Observable<Object> saveChapterHistory(String bookUrl, int chapterIndex,
                                                 String chapterTitle, int position) {
        return null;
    }

    @Override
    public Observable<Object> saveHistory(History history) {
        return null;
    }

    @Override
    public Observable<List<History>> getHistories() {
        return null;
    }
}
