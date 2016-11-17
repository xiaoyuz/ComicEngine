package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistory;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-11.
 */
public interface HistoryContract {

    interface View extends BaseView<Presenter> {

        void showHistories(List<BaseHistory> histories);

        void openBookDetail(BaseHistory history);
    }

    interface Presenter extends BasePresenter {

        void loadHistories();

        void openBookDetail(BaseHistory history);
    }
}
