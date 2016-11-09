package com.xiaoyuz.comicengine.entity.factory;

import com.xiaoyuz.comicengine.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.entity.base.BaseEntity;
import com.xiaoyuz.comicengine.entity.base.BasePage;
import com.xiaoyuz.comicengine.entity.base.BaseSearchResult;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BaseEntityFactory {

    public BaseEntity createEntity(Class <? extends BaseEntity> clazz) {
        return null;
    }

    public abstract BaseSearchResult createSearchResultEntity();

    public abstract BaseBookDetail createBookDetailEntity();

    public abstract BaseChapter createChapterEntity();

    public abstract BasePage createPageEntity();

}
