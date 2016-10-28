package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.entity.SearchResult;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public interface SearchResultContract {

    interface View extends BaseView<Presenter> {

        void showSearchResults(List<SearchResult> searchResults);

        void showNoResult();
    }

    interface Presenter extends BasePresenter {

        void loadSearchResults(String keyword);
    }
}
