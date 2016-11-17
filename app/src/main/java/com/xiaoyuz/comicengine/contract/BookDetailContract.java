package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistory;

/**
 * Created by zhangxiaoyu on 16/10/29.
 */
public interface BookDetailContract {

    interface View extends BaseView<Presenter> {

        void showBookDetail(BaseBookDetail bookDetail);

        void showNoChapter();

        void showChapter(int chapterIndex, BaseChapter chapter);

        void setHistory(int chapterIndex, String chapterTitle, int position);
    }

    interface Presenter extends BasePresenter {

        void loadBookDetail(BaseSearchResult searchResult);

        void openChapter(BaseChapter chapter, int chapterIndex);

        void loadChapterHistory(String bookUrl);

        void saveReadHistory(BaseHistory history);

        void offlineChapter(BaseBookDetail bookDetail);
    }
}
