package com.xiaoyuz.comicengine.entity.mh57;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiaoyuz.comicengine.entity.base.BaseSearchResult;

import org.jsoup.nodes.Element;

/**
 * Created by zhangxiaoyu on 16/10/27.
 */
public class SearchResult extends BaseSearchResult implements Parcelable {

    public static final Parcelable.Creator<SearchResult> CREATOR = new Creator(){

        @Override
        public SearchResult createFromParcel(Parcel source) {
            SearchResult searchResult = new SearchResult();
            searchResult.setBookCover(source.readString());
            searchResult.setStatus(source.readString());
            searchResult.setUrl(source.readString());
            searchResult.setTitle(source.readString());
            searchResult.setUpdateTime(source.readString());
            searchResult.setLastChapter(source.readString());
            return searchResult;
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    public SearchResult() {
    }

    public SearchResult(Element element) {
        super(element);
        bookCover = element.select(".book-cover .bcover img").attr("src");
        status = element.select(".book-detail dd.tags.status>span>span")
                .first().text();
        url = element.select(".book-detail dl>dt>a").attr("href");
        title = element.select(".book-detail dl>dt>a").attr("title");
        updateTime = element.select(".book-detail dd.tags.status>span>span").get(1).text();
        lastChapter = element.select(".book-detail dd.tags.status>span>a").text();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookCover);
        dest.writeString(status);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeString(updateTime);
        dest.writeString(lastChapter);
    }
}
