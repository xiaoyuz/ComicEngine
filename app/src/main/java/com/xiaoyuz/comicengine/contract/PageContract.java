package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.entity.Page;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public interface PageContract {

    interface View extends BaseView<Presenter> {

        void showPage(Page page);

        void showPage(String url);
    }

    interface Presenter extends BasePresenter {

        void loadPage(String url);

    }
}
