package com.xiaoyuz.comicengine.model.factory;

import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.mh57.Mh57BookDetail;
import com.xiaoyuz.comicengine.model.entity.mh57.Mh57Chapter;
import com.xiaoyuz.comicengine.model.entity.mh57.Mh57Page;
import com.xiaoyuz.comicengine.model.entity.mh57.Mh57SearchResult;
import com.xiaoyuz.comicengine.net.JsoupParser;
import com.xiaoyuz.comicengine.utils.Constants;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * www.52mh.com
 * Created by zhangxiaoyu on 16-11-9.
 */
public class Mh57EntityFactory extends BaseEntityFactory {

    public Mh57EntityFactory() {
    }

    @Override
    public BaseBookDetail createBookDetailEntity(BaseSearchResult searchResult) {
        Document doc = JsoupParser.getDocument(Constants.Net.MH57_URL_DOMAIN
                + searchResult.getUrl());
        if (doc != null) {
            BaseBookDetail bookDetail = new Mh57BookDetail(doc);
            bookDetail.injectSearchResult(searchResult);
            return bookDetail;
        }
        return null;
    }

    @Override
    public List<BaseChapter> createChapterEntity(Element element) {
        List<BaseChapter> chapters = new ArrayList<>();
        Elements uls = element.select("div#chpater-list-1 ul");
        for (Element ul : uls) {
            Elements lis = ul.select("li");
            // Reverse the chapters.
            Stack<Mh57Chapter> liStack = new Stack<>();
            for (Element li : lis) {
                liStack.add(new Mh57Chapter(li));
            }
            int stackSize = liStack.size();
            for (int i = 0; i < stackSize; i++) {
                chapters.add(liStack.pop());
            }
        }
        return chapters;
    }

    @Override
    public BasePage createPageEntity(String html) {
        Document doc = JsoupParser.getDocumentByCode(html);
        return new Mh57Page(doc);
    }

    @Override
    public List<BaseSearchResult> createSearchResultEntity(String keyword, int page) {
        String url = Constants.Net.MH57_SEARCH_URL_PREFIX + keyword + "-p-" + page;
        List<BaseSearchResult> searchResults = new ArrayList<>();
        Document doc = JsoupParser.getDocument(url);
        if (doc != null) {
            Elements elements = doc.select(".book-result .cf");
            for (Element element : elements) {
                searchResults.add(new Mh57SearchResult(element));
            }
        }
        return searchResults;
    }

    @Override
    public String getPageUrlPrefix() {
        return Constants.Net.MH57_MOBILE_URL_DOMAIN;
    }
}
