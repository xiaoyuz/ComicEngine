package com.xiaoyuz.comicengine.entity.mh57;

import com.xiaoyuz.comicengine.entity.base.BasePage;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public class Page extends BasePage {

    public Page(Element element) {
        super(element);
        imageUrl = element.select("img#manga").attr("src").toString();
    }
}
