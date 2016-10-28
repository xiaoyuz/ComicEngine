package com.xiaoyuz.comicengine.entity;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class BookDetail {

    private String description;
    private List<Chapter> chapters;

    public BookDetail(Element element) {
        description = element.select("div#intro-all p").text();
        chapters = new ArrayList<>();
        Elements uls = element.select("div#chpater-list-1 ul");
        for (Element ul : uls) {
            Elements lis = ul.select("li");
            for (Element li : lis) {
                chapters.add(new Chapter(li));
            }
        }
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
