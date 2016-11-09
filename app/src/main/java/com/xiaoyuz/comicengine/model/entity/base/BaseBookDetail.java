package com.xiaoyuz.comicengine.model.entity.base;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BaseBookDetail extends BaseEntity {

    protected String description;
    protected List<BaseChapter> chapters;

    public BaseBookDetail(Element element) {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BaseChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<BaseChapter> chapters) {
        this.chapters = chapters;
    }
}
