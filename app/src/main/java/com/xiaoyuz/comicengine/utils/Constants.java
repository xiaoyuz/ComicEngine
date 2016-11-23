package com.xiaoyuz.comicengine.utils;

/**
 * Created by zhangxiaoyu on 16/10/30.
 */
public class Constants {

    public class Build {
        public static final boolean OFFLINE_ACTIVITED = false;
        public static final int JSOUP_TIMEOUT = 10000;
    }

    public class Net {
        public static final String MH57_URL_DOMAIN = "http://www.57mh.com";
        public static final String MH57_MOBILE_URL_DOMAIN = "http://m.57mh.com";
        public static final String MH57_SEARCH_URL_PREFIX = "http://www.57mh.com/search/q_";
    }

    public class Bundle {
        // PageFragment
        public static final String PAGE_FRAGMENT_BOOK_URL = "bookUrl";
        public static final String PAGE_FRAGMENT_PAGE_URLS = "urls";
        public static final String PAGE_FRAGMENT_CHAPTER_INDEX = "chapterIndex";
        public static final String PAGE_FRAGMENT_HISTORY_POSITION = "history";
        public static final String PAGE_FRAGMENT_CHAPTER_TITLE = "chapterTitle";
        public static final String PAGE_FRAGMENT_OFFLINE_CHAPTER = "offline_chapter";
        public static final String PAGE_FRAGMENT_OFFLINE_DETAIL = "offline_detail";

        // BookDetailFragment
        public static final String BOOK_DETAIL_FRAGMENT_SEARCH_RESULT = "searchResult";

        // SearchResultsFragment
        public static final String SEARCH_RESULTS_FRAGMENT_KEYWORD = "keyword";

        // BookInfoActivity
        public static final String BOOK_INFO_ACTIVITY_KEYWORD = "keyword";
    }

    public class Database {
        public static final int MAX_HISTORY_COUNT = 10;
    }
}
