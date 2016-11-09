package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.entity.mh57.BookDetail;
import com.xiaoyuz.comicengine.entity.mh57.Chapter;

import java.util.ArrayList;

/**
 * Created by zhangxiaoyu on 16/10/29.
 */
public interface BookDetailContract {

    interface View extends BaseView<Presenter> {

        void showBookDetail(BookDetail bookDetail);

        void showNoChapter();

        void showChapter(int chapterIndex, Chapter chapter, ArrayList<String> pageUrls);

        void setHistory(int chapterIndex, String chapterTitle, int position);
    }

    interface Presenter extends BasePresenter {

        void loadBookDetail(String url);

        void openChapter(Chapter chapter, int chapterIndex);

        void loadChapterHistory(String bookUrl);

    }
}
