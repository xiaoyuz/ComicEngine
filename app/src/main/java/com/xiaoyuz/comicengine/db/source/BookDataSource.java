package com.xiaoyuz.comicengine.db.source;

import com.xiaoyuz.comicengine.entity.BookDetail;
import com.xiaoyuz.comicengine.entity.Page;
import com.xiaoyuz.comicengine.entity.SearchResult;

import java.util.List;

import rx.Observable;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public interface BookDataSource {

    Observable<List<SearchResult>> getSearchResults(String keyword, int page);

    Observable<BookDetail> getBookDetail(String url);

    Observable<String> getHtml(String url);

    Observable<Page> getPage(String html);

    Observable<String> getChapterHistory(String chapterUrl);

    Observable<Object> saveHtml(String url, String html);

    Observable<Object> saveChapterHistory(String bookUrl, int chapterIndex,
                                          String chapterTitle, int position);
}
