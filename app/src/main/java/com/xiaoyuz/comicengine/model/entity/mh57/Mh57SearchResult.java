package com.xiaoyuz.comicengine.model.entity.mh57;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16/10/27.
 */
public class Mh57SearchResult extends BaseSearchResult {

    public static final Parcelable.Creator<Mh57SearchResult> CREATOR = new Creator(){

        @Override
        public Mh57SearchResult createFromParcel(Parcel source) {
            Mh57SearchResult searchResult = new Mh57SearchResult();
            searchResult.setBookCover(source.readString());
            searchResult.setStatus(source.readString());
            searchResult.setUrl(source.readString());
            searchResult.setTitle(source.readString());
            searchResult.setUpdateTime(source.readString());
            searchResult.setLastChapter(source.readString());
            return searchResult;
        }

        @Override
        public Mh57SearchResult[] newArray(int size) {
            return new Mh57SearchResult[size];
        }
    };

    public Mh57SearchResult() {
    }

    public Mh57SearchResult(Element element) {
        super(element);
        bookCover = element.select(".book-cover .bcover img").attr("src");
        status = element.select(".book-detail dd.tags.status>span>span")
                .first().text();
        url = element.select(".book-detail dl>dt>a").attr("href");
        title = element.select(".book-detail dl>dt>a").attr("title");
        updateTime = element.select(".book-detail dd.tags.status>span>span").get(1).text();
        lastChapter = element.select(".book-detail dd.tags.status>span>a").text();
    }
}
