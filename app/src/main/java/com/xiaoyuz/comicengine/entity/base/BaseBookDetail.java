package com.xiaoyuz.comicengine.entity.base;

import com.xiaoyuz.comicengine.entity.mh57.Chapter;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BaseBookDetail extends BaseEntity {

    protected String description;
    protected List<Chapter> chapters;

    public BaseBookDetail(Element element) {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
