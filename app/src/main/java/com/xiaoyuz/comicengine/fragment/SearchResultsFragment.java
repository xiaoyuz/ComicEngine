package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.SearchResultContract;
import com.xiaoyuz.comicengine.entity.SearchResult;
import com.xiaoyuz.comicengine.ui.adapter.SearchResultsAdapter;
import com.xiaoyuz.comicengine.utils.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class SearchResultsFragment extends BaseFragment implements SearchResultContract.View {

    private RecyclerView mRecyclerView;
    private List<SearchResult> mSearchResults;
    private LinearLayoutManager mLayoutManager;
    private SearchResultsAdapter mAdapter;
    private TextView mLoadingView;

    private String mKeyword;

    private SearchResultContract.Presenter mSearchResultPresenter;

    @Override
    protected void initVariables() {
        mKeyword = getArguments().getString("keyword");
        mSearchResultPresenter.subscribe();
        mLayoutManager = new LinearLayoutManager(App.getContext());
        mSearchResults = new ArrayList<>();
        mAdapter = new SearchResultsAdapter(mSearchResults, mSearchResultPresenter);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results_fragment,
                container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mLoadingView = (TextView) view.findViewById(R.id.loading);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected void loadData() {
        mSearchResultPresenter.loadSearchResults(mKeyword);
    }

    @Override
    public void setPresenter(SearchResultContract.Presenter presenter) {
        mSearchResultPresenter = presenter;
    }

    @Override
    public void showSearchResults(List<SearchResult> searchResults) {
        mLoadingView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mSearchResults.addAll(searchResults);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoResult() {
        mLoadingView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.setText("No Result");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSearchResultPresenter.unsubscribe();
    }
}
