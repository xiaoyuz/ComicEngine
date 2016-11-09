package com.xiaoyuz.comicengine.model.entity.mh57;

import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class Mh57Chapter extends BaseChapter {

    public Mh57Chapter(Element element) {
        super(element);
        title = element.select("a").attr("title");
        url = element.select("a").attr("href");
        pageInfo = element.select("a span i").text();
        maxPageNum = Integer.parseInt(pageInfo.split("p")[0]);
    }

    @Override
    public ArrayList<String> createPageUrlList() {
        ArrayList<String> pageUrls = new ArrayList<>();
        for (int i = 1; i <= maxPageNum; i++) {
            pageUrls.add(url + "?p=" + i);
        }
        return pageUrls;
    }
}
