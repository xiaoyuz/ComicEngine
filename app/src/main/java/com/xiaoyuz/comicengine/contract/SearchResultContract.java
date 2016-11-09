package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public interface SearchResultContract {

    interface View extends BaseView<Presenter> {

        void showSearchResults(List<BaseSearchResult> searchResults);

        void showNoResult();

        void openBookDetail(BaseSearchResult searchResult);
    }

    interface Presenter extends BasePresenter {

        void loadSearchResults(String keyword, int page);

        void openBookDetail(BaseSearchResult searchResult);
    }
}
