package com.xiaoyuz.comicengine.db.source.repository;

import com.xiaoyuz.comicengine.db.source.BookDataSource;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistory;

import java.util.List;

import rx.Observable;

/**
 * Search result should not be cached, so here only use remote data source.
 * Created by zhangxiaoyu on 16-10-28.
 */
public class BookRepository implements BookDataSource {

    private static BookRepository sInstance;

    private BookDataSource mBookLocalDataSource;
    private BookDataSource mBookRemoteDataSource;

    private BookRepository(BookDataSource bookLocalDataSource,
                           BookDataSource bookRemoteDataSource) {
        mBookLocalDataSource = bookLocalDataSource;
        mBookRemoteDataSource = bookRemoteDataSource;
    }

    public static BookRepository getInstance(
            BookDataSource bookLocalDataSource, BookDataSource bookRemoteDataSource) {
        if (sInstance == null) {
            sInstance = new BookRepository(bookLocalDataSource, bookRemoteDataSource);
        }
        return sInstance;
    }

    @Override
    public Observable<List<BaseSearchResult>> getSearchResults(String keyword, int page) {
        return mBookRemoteDataSource.getSearchResults(keyword, page);
    }

    @Override
    public Observable<BaseBookDetail> getBookDetail(BaseSearchResult searchResult) {
        return mBookRemoteDataSource.getBookDetail(searchResult);
    }

    @Override
    public Observable<String> getHtml(String url) {
        // If no local html, no need to load remotely,
        // just return empty string so presenter will handle it.
        // Because webview need to load url remotely so we must do like this.
        return mBookLocalDataSource.getHtml(url);
    }

    @Override
    public Observable<BasePage> getPage(String html) {
        return mBookRemoteDataSource.getPage(html);
    }

    @Override
    public Observable<String> getChapterHistory(String bookUrl) {
        return mBookLocalDataSource.getChapterHistory(bookUrl);
    }

    @Override
    public Observable<Object> saveHtml(String url, String html) {
        return mBookLocalDataSource.saveHtml(url, html);
    }

    @Override
    public Observable<Object> saveChapterHistory(String bookUrl, int chapterIndex,
                                                 String chapterTitle, int position) {
        return mBookLocalDataSource.saveChapterHistory(bookUrl, chapterIndex,
                chapterTitle, position);
    }

    @Override
    public Observable<Object> saveHistory(BaseHistory history) {
        return mBookLocalDataSource.saveHistory(history);
    }

    @Override
    public Observable<List<BaseHistory>> getHistories() {
        return mBookLocalDataSource.getHistories();
    }

    @Override
    public Observable<Object> offlineBookDetail(BaseBookDetail bookDetail) {
        return mBookLocalDataSource.offlineBookDetail(bookDetail);
    }

    @Override
    public Observable<Object> addOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter) {
        return mBookLocalDataSource.addOfflineChapter(bookDetail, chapter);
    }

    @Override
    public Observable<Object> deleteOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter) {
        return mBookLocalDataSource.deleteOfflineChapter(bookDetail, chapter);
    }

    @Override
    public Observable<List<BaseChapter>> getOfflineChapters(BaseBookDetail bookDetail) {
        return mBookLocalDataSource.getOfflineChapters(bookDetail);
    }
}
