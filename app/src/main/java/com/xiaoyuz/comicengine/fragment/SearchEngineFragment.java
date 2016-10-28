package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseEntity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.contract.SearchResultContract;
import com.xiaoyuz.comicengine.contract.presenter.SearchResultPresenter;
import com.xiaoyuz.comicengine.db.source.remote.SearchResultRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.SearchResultRepository;
import com.xiaoyuz.comicengine.entity.SearchResult;
import com.xiaoyuz.comicengine.net.JsoupParser;
import com.xiaoyuz.comicengine.utils.App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangxiaoyu on 16/10/27.
 */
public class SearchEngineFragment extends BaseFragment {

    private LazyInstance<SearchResultsFragment> mLazySearchResultsFragment;

    @Override
    protected void initVariables() {
        mLazySearchResultsFragment = new LazyInstance<SearchResultsFragment>(
                new LazyInstance.InstanceCreator<SearchResultsFragment>() {
            @Override
            public SearchResultsFragment createInstance() {
                return new SearchResultsFragment();
            }
        });
    }

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
                String keyword = keywordEditText.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("keyword", keyword);
                mLazySearchResultsFragment.get().setArguments(bundle);
                new SearchResultPresenter(SearchResultRepository.getInstace(
                        SearchResultRemoteDataSource.getInstance()),
                        mLazySearchResultsFragment.get());
                replaceFragment(mLazySearchResultsFragment.get());
            }
        });
        return view;
    }

    @Override
    protected void loadData() {

    }
}
