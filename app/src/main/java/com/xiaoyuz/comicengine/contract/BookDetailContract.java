package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.entity.BookDetail;

/**
 * Created by zhangxiaoyu on 16/10/29.
 */
public interface BookDetailContract {

    interface View extends BaseView<Presenter> {

        void showBookDetail(BookDetail bookDetail);

        void showNoChapter();

    }

    interface Presenter extends BasePresenter {

        void loadBookDetail(String url);

    }

}
