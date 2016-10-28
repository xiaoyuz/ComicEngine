package com.xiaoyuz.comicengine.db.source.repository;

import com.xiaoyuz.comicengine.db.source.SearchResultDataSource;
import com.xiaoyuz.comicengine.entity.SearchResult;

import java.util.List;

import rx.Observable;

/**
 * Search result should not be cached, so here only use remote data source.
 * Created by zhangxiaoyu on 16-10-28.
 */
public class SearchResultRepository implements SearchResultDataSource {

    private static SearchResultRepository sInstace;

    private SearchResultDataSource mSearchResultRemoteDataSource;

    private SearchResultRepository(SearchResultDataSource searchResultRemoteDataSource) {
        mSearchResultRemoteDataSource = searchResultRemoteDataSource;
    }

    public static SearchResultRepository getInstace(
            SearchResultDataSource searchResultRemoteDataSource) {
        if (sInstace == null) {
            sInstace = new SearchResultRepository(searchResultRemoteDataSource);
        }
        return sInstace;
    }

    @Override
    public Observable<List<SearchResult>> getSearchResults(String keyword) {
        return mSearchResultRemoteDataSource.getSearchResults(keyword);
    }
}
