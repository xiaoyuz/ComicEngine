package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.entity.mh57.Page;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public interface ComicImageContract {

    interface View extends BaseView<Presenter> {

        void showPage(Page page);

        void loadUrlByWebView(String url);
    }

    interface Presenter extends BasePresenter {

        void loadHtmlPage(String url);

        void loadPage(String html);

        void saveHtmlToLocal(String url, String html);
    }
}
