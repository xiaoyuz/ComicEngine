package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.entity.SearchResult;
import com.xiaoyuz.comicengine.utils.App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by zhangxiaoyu on 16/10/27.
 */
public class SearchEngineFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_engine_fragment,
                container, false);
        final EditText keywordEditText = (EditText) view.findViewById(R.id.keyword);
        Button searchButton = (Button) view.findViewById(R.id.search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String keyword = keywordEditText.getText().toString();
                    Document doc = Jsoup.connect("http://www.57mh.com/search/q_" + keyword)
                            .header("User-Agent", "Mozilla/5.0 (Macintosh; U;" +
                                    "Intel Mac OS X 10.4; en-US; rv:1.9.2.2)" +
                                    " Gecko/20100316 Firefox/3.6.2")
                            .get();
                    Elements elements = doc.select(".book-result .cf");
                    Toast.makeText(App.getContext(), new SearchResult(elements.first()).toString(),
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(App.getContext(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void loadData() {

    }
}
