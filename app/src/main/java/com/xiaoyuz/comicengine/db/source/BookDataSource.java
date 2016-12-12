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
 * Created by zhangxiaoyu on 16-10-28.
 */
public interface BookDataSource {

    Observable<BaseSearchResult> getSearchResult(String keyword, int page);

    Observable<BaseBookDetail> getBookDetail(BaseSearchResult searchResult);

    Observable<String> getHtml(String url);

    Observable<BasePage> getPage(String html);

    Observable<String> getChapterHistory(String chapterUrl);

    Observable<Object> saveHtml(String url, String html);

    Observable<Object> saveChapterHistory(String bookUrl, int chapterIndex,
                                          String chapterTitle, int position);

    Observable<Object> saveHistory(BaseHistory history);

    Observable<List<BaseHistory>> getHistories();

    Observable<Object> addOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter);

    Observable<Object> deleteOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter);

    Observable<List<BaseChapter>> getOfflineChapters(BaseBookDetail bookDetail);

    Observable<List<BaseOfflineBook>> getAllOfflineBooks();
}
