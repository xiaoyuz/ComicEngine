package com.xiaoyuz.comicengine.db.source.remote;

import com.xiaoyuz.comicengine.db.source.SearchResultDataSource;
import com.xiaoyuz.comicengine.entity.SearchResult;
import com.xiaoyuz.comicengine.net.JsoupParser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class SearchResultRemoteDataSource implements SearchResultDataSource {

    private static SearchResultRemoteDataSource sInstance;

    private SearchResultRemoteDataSource() {

    }

    public static SearchResultRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new SearchResultRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public Observable<List<SearchResult>> getSearchResults(final String keyword) {
        return Observable.create(new Observable.OnSubscribe<List<SearchResult>>() {
            @Override
            public void call(Subscriber<? super List<SearchResult>> subscriber) {
                Document doc = JsoupParser
                        .getDocument("http://www.57mh.com/search/q_" + keyword);
                if (doc != null) {
                    Elements elements = doc.select(".book-result .cf");
                    List<SearchResult> searchResults = new ArrayList<>();
                    for (Element element : elements) {
                        searchResults.add(new SearchResult(element));
                    }
                    subscriber.onNext(searchResults);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new NullPointerException());
                }
            }
        });
    }
}
