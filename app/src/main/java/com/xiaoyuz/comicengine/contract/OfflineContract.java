package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.model.entity.base.BaseOfflineBook;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-22.
 */
public interface OfflineContract {

    interface View extends BaseView<Presenter> {

        void showOfflineBooks(List<BaseOfflineBook> offlineBooks);
    }

    interface Presenter extends BasePresenter {

        void loadOfflineBooks();
    }
}
