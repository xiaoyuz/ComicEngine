package com.xiaoyuz.comicengine.entity;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public class Page {

    private String imageUrl;

    public Page(Element element) {
        imageUrl = element.select("img#manga").attr("src").toString();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
