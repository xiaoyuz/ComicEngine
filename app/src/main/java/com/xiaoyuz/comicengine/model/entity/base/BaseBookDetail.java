package com.xiaoyuz.comicengine.model.entity.base;

import org.jsoup.nodes.Element;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public abstract class BaseBookDetail extends BaseEntity implements Serializable {

    public static final long serialVersionUID = 536871008;
    protected String url;
    protected BaseSearchResult searchResult;
    protected String description;
    protected List<BaseChapter> chapters;

    public BaseBookDetail(){
    }

    public BaseBookDetail(Element element) {

    }

    public void injectSearchResult(BaseSearchResult searchResult) {
        this.url = searchResult.getUrl();
        this.searchResult = searchResult;
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

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BaseSearchResult getSearchResult() {
        return this.searchResult;
    }

    public void setSearchResult(BaseSearchResult searchResult) {
        this.searchResult = searchResult;
    }
}
