package com.xiaoyuz.comicengine.entity.mh57;

import com.xiaoyuz.comicengine.entity.base.BaseBookDetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class BookDetail extends BaseBookDetail {

    public BookDetail(Element element) {
        super(element);
        description = element.select("div#intro-all p").text();
        chapters = new ArrayList<>();
        Elements uls = element.select("div#chpater-list-1 ul");
        for (Element ul : uls) {
            Elements lis = ul.select("li");
            // Reverse the chapters.
            Stack<Chapter> liStack = new Stack<>();
            for (Element li : lis) {
                liStack.add(new Chapter(li));
            }
            int stackSize = liStack.size();
            for (int i = 0; i < stackSize; i++) {
                chapters.add(liStack.pop());
            }
        }
    }
}
