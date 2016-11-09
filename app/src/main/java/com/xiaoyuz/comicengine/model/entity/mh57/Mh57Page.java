package com.xiaoyuz.comicengine.model.entity.mh57;

import com.xiaoyuz.comicengine.model.entity.base.BasePage;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public class Mh57Page extends BasePage {

    public Mh57Page(Element element) {
        super(element);
        imageUrl = element.select("img#manga").attr("src");
    }
}
