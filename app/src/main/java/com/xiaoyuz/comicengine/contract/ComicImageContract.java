package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public interface ComicImageContract {

    interface View extends BaseView<Presenter> {

        void setUrl(String url);

        void showPage(BasePage page);

        void loadUrlByWebView(String url);

        void showNetError();
    }

    interface Presenter extends BasePresenter {

        void loadHtmlPage(String url);

        void loadPage(boolean isRemote, String url, String html);
    }
}
