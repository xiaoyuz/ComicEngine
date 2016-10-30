package com.xiaoyuz.comicengine.net;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class JsoupParser {

    public static Document getDocument(String url) {
        try {
            Log.d("aaaaa", url);
            Document doc = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; U;" +
                            "Intel Mac OS X 10.4; en-US; rv:1.9.2.2)" +
                            " Gecko/20100316 Firefox/3.6.2").timeout(10000)
                    .get();
            Log.d("aaaaa", doc.body().toString());
            return doc;
        } catch (IOException e) {
            return null;
        }

    }
}
