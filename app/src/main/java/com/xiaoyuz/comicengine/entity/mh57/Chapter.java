package com.xiaoyuz.comicengine.entity.mh57;

import com.xiaoyuz.comicengine.entity.base.BaseChapter;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class Chapter extends BaseChapter {

    public Chapter(Element element) {
        super(element);
        title = element.select("a").attr("title");
        url = element.select("a").attr("href");
        pageInfo = element.select("a span i").text();
    }
}
