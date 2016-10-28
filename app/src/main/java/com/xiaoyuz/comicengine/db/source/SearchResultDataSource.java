package com.xiaoyuz.comicengine.db.source;

import com.xiaoyuz.comicengine.entity.SearchResult;

import java.util.List;

import rx.Observable;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public interface SearchResultDataSource {

    Observable<List<SearchResult>> getSearchResults(String keyword);
}
