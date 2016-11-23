package com.xiaoyuz.comicengine.net;

import com.xiaoyuz.comicengine.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class JsoupParser {

    public static Document getDocument(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; U;" +
                            "Intel Mac OS X 10.4; en-US; rv:1.9.2.2)" +
                            " Gecko/20100316 Firefox/3.6.2").timeout(Constants.Build.JSOUP_TIMEOUT)
                    .get();
            return doc;
        } catch (IOException e) {
            return null;
        }
    }

    public static Document getDocumentByCode(String code) {
        Document doc = Jsoup.parse(code);
        return doc;
    }
}
