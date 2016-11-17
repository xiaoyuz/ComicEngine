package com.xiaoyuz.comicengine.db.source;

import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.history.History;

import java.util.List;

import rx.Observable;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public interface BookDataSource {

    Observable<List<BaseSearchResult>> getSearchResults(String keyword, int page);

    Observable<BaseBookDetail> getBookDetail(BaseSearchResult searchResult);

    Observable<String> getHtml(String url);

    Observable<BasePage> getPage(String html);

    Observable<String> getChapterHistory(String chapterUrl);

    Observable<Object> saveHtml(String url, String html);

    Observable<Object> saveChapterHistory(String bookUrl, int chapterIndex,
                                          String chapterTitle, int position);

    Observable<Object> saveHistory(History history);

    Observable<List<History>> getHistories();
}
