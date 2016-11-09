package com.xiaoyuz.comicengine.model.factory;

import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BaseEntityFactory {

    public abstract List<BaseSearchResult> createSearchResultEntity(String keyword, int page);

    public abstract BaseBookDetail createBookDetailEntity(String url);

    public abstract List<BaseChapter> createChapterEntity(Element element);

    public abstract BasePage createPageEntity(String html);

    public abstract String getPageUrlPrefix();

}
