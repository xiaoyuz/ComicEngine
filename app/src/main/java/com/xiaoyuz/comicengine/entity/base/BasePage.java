package com.xiaoyuz.comicengine.entity.base;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BasePage extends BaseEntity {
    protected String imageUrl;

    public BasePage(Element element) {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
