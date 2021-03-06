package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.activity.ComicActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.SearchResultContract;
import com.xiaoyuz.comicengine.contract.presenter.BookDetailPresenter;
import com.xiaoyuz.comicengine.db.source.local.BookLocalDataSource;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.ui.adapter.SearchResultsAdapter;
import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class SearchResultsFragment extends BaseFragment
        implements SearchResultContract.View, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private SearchResultsAdapter mAdapter;
    private TextView mLoadingView;

    private List<BaseSearchResult> mSearchResults;
    private List<BaseSearchResult> mLastLoadedResults;
    private boolean mIsLoading;
    private boolean mNoMoreResult;
    private int mNextPage = 1;
    private String mKeyword;
    private SearchResultContract.Presenter mSearchResultPresenter;

    @Override
    protected void initVariables() {
        mKeyword = getArguments().getString(Constants.Bundle.SEARCH_RESULTS_FRAGMENT_KEYWORD);
        mSearchResults = new ArrayList<>();
        mLastLoadedResults = new ArrayList<>();
    }

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results_fragment,
                container, false);
        mSearchResultPresenter.subscribe();
        mAdapter = new SearchResultsAdapter(mSearchResults, mSearchResultPresenter);

        initBackableToolBar(view, "Search Results: " + mKeyword);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mLoadingView = (TextView) view.findViewById(R.id.loading);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(App.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mNoMoreResult) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {
                        if (!mIsLoading && !mSwipeRefreshLayout.isRefreshing()) {
                            mIsLoading = true;
                            loadData();
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    protected void loadData() {
        mSearchResultPresenter.loadSearchResults(mKeyword, mNextPage);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void setPresenter(SearchResultContract.Presenter presenter) {
        mSearchResultPresenter = presenter;
    }

    @Override
    public void showSearchResults(List<BaseSearchResult> searchResults) {
        mLoadingView.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        if (!sameSearchResults(mLastLoadedResults, searchResults)) {
            mSearchResults.addAll(searchResults);
            mAdapter.notifyDataSetChanged();
            mLastLoadedResults = searchResults;
            mNextPage++;
        } else {
            mNoMoreResult = true;
        }
        mSwipeRefreshLayout.setRefreshing(false);
        mIsLoading = false;
    }

    @Override
    public void showNoResult() {
        mLoadingView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mLoadingView.setText("No Result");
    }

    @Override
    public void openBookDetail(BaseSearchResult searchResult) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.Bundle.BOOK_DETAIL_FRAGMENT_SEARCH_RESULT, searchResult);
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(bundle);
        new BookDetailPresenter(BookRepository.getInstance(BookLocalDataSource.getInstance(),
                BookRemoteDataSource.getInstance()),
                fragment);
        EventDispatcher.post(new ComicActivity.GotoFragmentOperation(fragment));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSearchResultPresenter.unsubscribe();
    }

    @Override
    public void onRefresh() {
        mSearchResults.clear();
        mLastLoadedResults.clear();
        mNextPage = 1;
        mNoMoreResult = false;
        loadData();
    }

    private boolean sameSearchResults(List<BaseSearchResult> searchResults1,
                                      List<BaseSearchResult> searchResults2) {
        if (searchResults1.size() != searchResults2.size()) {
            return false;
        }
        for (int i = 0; i < searchResults1.size(); i++) {
            if (!searchResults1.get(i).equals(searchResults2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
