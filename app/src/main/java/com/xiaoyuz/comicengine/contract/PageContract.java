package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;

/**
 * Created by zhangxiaoyu on 16-11-5.
 */
public interface PageContract {

    interface View extends BaseView<Presenter> {

        void jump2HistoryPage(int position);

    }

    interface Presenter extends BasePresenter {

        void saveChapterHistory(String chapterUrl, int position);

        void loadChapterHistory(String chapterUrl);
    }
}
