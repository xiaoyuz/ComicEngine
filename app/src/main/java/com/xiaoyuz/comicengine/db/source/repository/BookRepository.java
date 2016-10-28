package com.xiaoyuz.comicengine.db.source.repository;

import com.xiaoyuz.comicengine.db.source.BookDataSource;
import com.xiaoyuz.comicengine.entity.BookDetail;
import com.xiaoyuz.comicengine.entity.SearchResult;

import java.util.List;

import rx.Observable;

/**
 * Search result should not be cached, so here only use remote data source.
 * Created by zhangxiaoyu on 16-10-28.
 */
public class BookRepository implements BookDataSource {

    private static BookRepository sInstance;

    private BookDataSource mBookRemoteDataSource;

    private BookRepository(BookDataSource bookRemoteDataSource) {
        mBookRemoteDataSource = bookRemoteDataSource;
    }

    public static BookRepository getInstance(
            BookDataSource bookRemoteDataSource) {
        if (sInstance == null) {
            sInstance = new BookRepository(bookRemoteDataSource);
        }
        return sInstance;
    }

    @Override
    public Observable<List<SearchResult>> getSearchResults(String keyword) {
        return mBookRemoteDataSource.getSearchResults(keyword);
    }

    @Override
    public Observable<BookDetail> getBookDetail(String url) {
        return mBookRemoteDataSource.getBookDetail(url);
    }
}
