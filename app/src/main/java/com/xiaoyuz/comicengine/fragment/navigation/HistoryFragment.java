package com.xiaoyuz.comicengine.fragment.navigation;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.activity.ComicActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.HistoryContract;
import com.xiaoyuz.comicengine.contract.presenter.BookDetailPresenter;
import com.xiaoyuz.comicengine.db.source.local.BookLocalDataSource;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.fragment.BookDetailFragment;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistory;
import com.xiaoyuz.comicengine.model.entity.mh57.Mh57SearchResult;
import com.xiaoyuz.comicengine.ui.adapter.HistoryAdapter;
import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-18.
 */
public class HistoryFragment extends BaseChildFragment implements HistoryContract.View {

    private RecyclerView mRecyclerView;
    private HistoryContract.Presenter mPresenter;
    private HistoryAdapter mAdapter;
    private List<BaseHistory> mHistoryList;

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.history_fragment, container, false));

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(App.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    protected void initVariables() {
        mHistoryList = new ArrayList<>();
        mAdapter = new HistoryAdapter(mHistoryList, mPresenter);
    }

    @Override
    protected void loadData() {
        mPresenter.loadHistories();
    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showHistories(List<BaseHistory> histories) {
        mHistoryList.addAll(histories);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHistoryDeleted(int position) {
        mAdapter.deleteHistory(position);
    }

    @Override
    public void openBookDetail(BaseHistory history) {
        // TODO: Use global class not extended class.
        BaseSearchResult searchResult = new Mh57SearchResult();
        searchResult.fromHistory(history);

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
        mPresenter.unsubscribe();
    }
}
