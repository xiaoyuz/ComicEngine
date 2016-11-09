package com.xiaoyuz.comicengine.model.entity.mh57;

import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.utils.App;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class Mh57BookDetail extends BaseBookDetail {

    public Mh57BookDetail(Element element) {
        super(element);
        description = element.select("div#intro-all p").text();
        chapters = App.getEntityFactory().createChapterEntity(element);
    }
}
