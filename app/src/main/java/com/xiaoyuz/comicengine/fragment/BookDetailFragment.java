package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.activity.BookInfoActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.BookDetailContract;
import com.xiaoyuz.comicengine.contract.presenter.PagePresenter;
import com.xiaoyuz.comicengine.db.source.local.BookLocalDataSource;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.entity.BookDetail;
import com.xiaoyuz.comicengine.entity.Chapter;
import com.xiaoyuz.comicengine.entity.SearchResult;
import com.xiaoyuz.comicengine.event.PageDestroyEvent;
import com.xiaoyuz.comicengine.ui.adapter.ChapterAdapter;
import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class BookDetailFragment extends BaseFragment implements
        BookDetailContract.View {

    private class EventHandler {
        @Subscribe
        public void onPageDestroy(PageDestroyEvent event) {
            mPresenter.loadChapterHistory(event.getBookUrl());
        }
    }

    private RecyclerView mRecyclerView;
    private TextView mLoadingView;
    private TextView mHistoryView;
    private Button mReadButton;
    private SearchResult mSearchResult;
    private List<Chapter> mChapters;
    private BookDetailContract.Presenter mPresenter;
    private ChapterAdapter mChapterAdapter;

    private int mHistoryChapterIndex;
    private int mHistoryPosition;

    private EventHandler mEventHandler;

    @Override
    protected void initVariables() {
        mPresenter.subscribe();
        mSearchResult = getArguments().getParcelable(Constants
                .Bundle.BOOK_DETAIL_FRAGMENT_SEARCH_RESULT);
        mChapters = new ArrayList<>();
        mChapterAdapter = new ChapterAdapter(mChapters, mPresenter);
        mEventHandler = new EventHandler();
    }

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_detail_fragment,
                container, false);
        EventDispatcher.register(mEventHandler);
        ((TextView) view.findViewById(R.id.title)).setText(mSearchResult.getTitle());
        ((TextView) view.findViewById(R.id.status)).setText(mSearchResult.getStatus());
        ((TextView) view.findViewById(R.id.last_chapter)).setText(mSearchResult.getLastChapter());
        ((TextView) view.findViewById(R.id.update_time)).setText(mSearchResult.getUpdateTime());

        mHistoryView = (TextView) view.findViewById(R.id.history);
        mReadButton = (Button) view.findViewById(R.id.read);

        Glide.with(App.getContext()).load(mSearchResult.getBookCover())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
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
        mPresenter.loadChapterHistory(mSearchResult.getUrl());
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
        EventDispatcher.unregister(mEventHandler);
        mPresenter.unsubscribe();
    }

    @Override
    public void showChapter(int chapterIndex, Chapter chapter, ArrayList<String> pageUrls) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.PAGE_FRAGMENT_CHAPTER_TITLE, chapter.getTitle());
        bundle.putStringArrayList(Constants.Bundle.PAGE_FRAGMENT_PAGE_URLS, pageUrls);
        bundle.putInt(Constants.Bundle.PAGE_FRAGMENT_CHAPTER_INDEX, chapterIndex);
        bundle.putString(Constants.Bundle.PAGE_FRAGMENT_BOOK_URL, mSearchResult.getUrl());
        bundle.putInt(Constants.Bundle.PAGE_FRAGMENT_HISTORY_POSITION,
                chapterIndex == mHistoryChapterIndex ? mHistoryPosition : 0);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(bundle);
        new PagePresenter(BookRepository.getInstance(BookLocalDataSource.getInstance(),
                BookRemoteDataSource.getInstance()), fragment);
        EventDispatcher.post(new BookInfoActivity.GotoFragmentOperation(fragment));
    }

    @Override
    public void setHistory(int chapterIndex, String chapterTitle, int position) {
        mHistoryChapterIndex = chapterIndex;
        mHistoryPosition = position;
        mHistoryView.setText("Last read: " + chapterTitle
                + ", Page: " + String.valueOf(position + 1));

        mReadButton.setVisibility(View.VISIBLE);
        mReadButton.findViewById(R.id.read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openChapter(mChapters.get(mHistoryChapterIndex), mHistoryChapterIndex);
            }
        });
    }
}
