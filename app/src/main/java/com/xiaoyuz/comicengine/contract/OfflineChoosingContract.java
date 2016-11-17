package com.xiaoyuz.comicengine.contract;

import com.xiaoyuz.comicengine.base.BasePresenter;
import com.xiaoyuz.comicengine.base.BaseView;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-18.
 */
public interface OfflineChoosingContract {

    interface View extends BaseView<Presenter> {

        void showOfflineChapters(List<BaseChapter> chapters);
    }

    interface Presenter extends BasePresenter {

        void addOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter);

        void deleteOfflineChapter(BaseBookDetail bookDetail, BaseChapter chapter);

        void loadOfflineChapters(BaseBookDetail bookDetail);
    }
}
