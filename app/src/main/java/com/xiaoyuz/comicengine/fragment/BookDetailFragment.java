package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.BookDetailContract;
import com.xiaoyuz.comicengine.entity.BookDetail;
import com.xiaoyuz.comicengine.entity.Chapter;
import com.xiaoyuz.comicengine.entity.SearchResult;
import com.xiaoyuz.comicengine.ui.adapter.ChapterAdapter;
import com.xiaoyuz.comicengine.utils.App;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class BookDetailFragment extends BaseFragment implements
        BookDetailContract.View {

    private RecyclerView mRecyclerView;
    private TextView mLoadingView;
    private SearchResult mSearchResult;
    private List<Chapter> mChapters;
    private BookDetailContract.Presenter mPresenter;
    private ChapterAdapter mChapterAdapter;

    @Override
    protected void initVariables() {
        mPresenter.subscribe();
        mSearchResult = getArguments().getParcelable("searchResult");
        mChapters = new ArrayList<>();
        mChapterAdapter = new ChapterAdapter(mChapters);
    }

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_detail_fragment,
                container, false);
        ((TextView) view.findViewById(R.id.title)).setText(mSearchResult.getTitle());

        Glide.with(App.getContext()).load(mSearchResult.getBookCover())
                .into((ImageView) view.findViewById(R.id.image));

        mLoadingView = (TextView) view.findViewById(R.id.loading);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.chapter_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mChapterAdapter);
        return view;
    }

    @Override
    protected void loadData() {
        mPresenter.loadBookDetail(mSearchResult.getUrl());
    }

    @Override
    public void showBookDetail(BookDetail bookDetail) {
        mLoadingView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mChapters.addAll(bookDetail.getChapters());
        mChapterAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoChapter() {
        mLoadingView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.setText("No Result");
    }

    @Override
    public void setPresenter(BookDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
