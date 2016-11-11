package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.model.entity.history.History;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-11.
 */
public interface HistoryContract {

    interface View extends BaseView<Presenter> {

        void showHistories(List<History> histories);

        void openBookDetail(History history);
    }

    interface Presenter extends BasePresenter {

        void loadHistories();

        void openBookDetail(History history);
    }
}
