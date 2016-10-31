package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.activity.BookInfoActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.contract.SearchResultContract;
import com.xiaoyuz.comicengine.contract.presenter.BookDetailPresenter;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.entity.SearchResult;
import com.xiaoyuz.comicengine.ui.adapter.SearchResultsAdapter;
import com.xiaoyuz.comicengine.utils.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class SearchResultsFragment extends BaseFragment
        implements SearchResultContract.View {

    private RecyclerView mRecyclerView;
    private List<SearchResult> mSearchResults;
    private SearchResultsAdapter mAdapter;
    private TextView mLoadingView;

    private String mKeyword;

    private SearchResultContract.Presenter mSearchResultPresenter;

    private LazyInstance<BookDetailFragment> mLazyBookDetailFragment =
            new LazyInstance<>(
                    new LazyInstance.InstanceCreator<BookDetailFragment>() {
                        @Override
                        public BookDetailFragment createInstance() {
                            return new BookDetailFragment();
                        }
                    });

    @Override
    protected void initVariables() {
        mKeyword = getArguments().getString("keyword");
        mSearchResultPresenter.subscribe();
        mSearchResults = new ArrayList<>();
        mAdapter = new SearchResultsAdapter(mSearchResults, mSearchResultPresenter);
    }

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results_fragment,
                container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mLoadingView = (TextView) view.findViewById(R.id.loading);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
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
    public void openBookDetail(SearchResult searchResult) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("searchResult", searchResult);
        BookDetailFragment fragment = mLazyBookDetailFragment.get();
        fragment.setArguments(bundle);
        new BookDetailPresenter(BookRepository.getInstance(
                BookRemoteDataSource.getInstance()),
                fragment);
        EventDispatcher.post(new BookInfoActivity.GotoFragmentOperation(fragment));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSearchResultPresenter.unsubscribe();
    }
}
