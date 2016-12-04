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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.activity.ComicActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.BookDetailContract;
import com.xiaoyuz.comicengine.contract.presenter.OfflineChoosingPresenter;
import com.xiaoyuz.comicengine.contract.presenter.PagePresenter;
import com.xiaoyuz.comicengine.db.source.local.BookLocalDataSource;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.event.PageTurningEvent;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.base.BaseSearchResult;
import com.xiaoyuz.comicengine.event.PageDestroyEvent;
import com.xiaoyuz.comicengine.ui.adapter.ChapterAdapter;
import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.Constants;

import java.io.Serializable;
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

        @Subscribe
        public void onPageTurningEvent(PageTurningEvent event) {
            int chapterIndex = event.getChaperIndex();
            if (event.isNext()) {
                if (chapterIndex + 1 >= mChapters.size()) {
                    Toast.makeText(getContext(), "No more chapter.", Toast.LENGTH_SHORT).show();
                    return;
                }
                showChapter(chapterIndex + 1, mChapters.get(chapterIndex + 1));
            } else {
                showChapter(chapterIndex - 1, mChapters.get(chapterIndex - 1));
            }
        }
    }

    private RecyclerView mRecyclerView;
    private TextView mLoadingView;
    private TextView mHistoryView;
    private TextView mOfflineView;
    private Button mReadButton;
    private ExpandableTextView mDescriptionView;
    private BaseSearchResult mSearchResult;
    private List<BaseChapter> mChapters;
    private BaseBookDetail mBookDetail;
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
        initBackableToolBar(view, mSearchResult.getTitle());
        EventDispatcher.register(mEventHandler);
        ((TextView) view.findViewById(R.id.title)).setText(mSearchResult.getTitle());
        ((TextView) view.findViewById(R.id.status)).setText(mSearchResult.getStatus());
        ((TextView) view.findViewById(R.id.last_chapter)).setText(mSearchResult.getLastChapter());
        ((TextView) view.findViewById(R.id.update_time)).setText(mSearchResult.getUpdateTime());

        mHistoryView = (TextView) view.findViewById(R.id.history);
        mReadButton = (Button) view.findViewById(R.id.continue_read);
        mDescriptionView = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        mOfflineView = (TextView) view.findViewById(R.id.offline);
        mOfflineView.setVisibility(Constants.Build.OFFLINE_ACTIVITED ? View.VISIBLE : View.GONE);
        mOfflineView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfflineChoosingFragment fragment = new OfflineChoosingFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.Bundle.PAGE_FRAGMENT_OFFLINE_CHAPTER,
                        (Serializable) mChapters);
                bundle.putSerializable(Constants.Bundle.PAGE_FRAGMENT_OFFLINE_DETAIL, mBookDetail);
                fragment.setArguments(bundle);
                new OfflineChoosingPresenter(BookRepository.getInstance
                        (BookLocalDataSource.getInstance(),
                                BookRemoteDataSource.getInstance()), fragment);
                EventDispatcher.post(new ComicActivity.GotoFragmentOperation(fragment));
            }
        });

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
        mPresenter.loadBookDetail(mSearchResult);
        mPresenter.loadChapterHistory(mSearchResult.getUrl());
        mPresenter.saveReadHistory(mSearchResult.toHistory());
    }

    @Override
    public void showBookDetail(BaseBookDetail bookDetail) {
        mBookDetail = bookDetail;
        mDescriptionView.setText(bookDetail.getDescription());
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
    public void showChapter(int chapterIndex, BaseChapter chapter) {
        ArrayList<String> pageUrls = chapter.createPageUrlList();
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
        EventDispatcher.post(new ComicActivity.GotoFragmentOperation(fragment));
    }

    @Override
    public void setHistory(int chapterIndex, String chapterTitle, int position) {
        mHistoryChapterIndex = chapterIndex;
        mHistoryPosition = position;
        mHistoryView.setText("Last read: " + chapterTitle
                + ", Page: " + String.valueOf(position + 1));

        mReadButton.setVisibility(View.VISIBLE);
        mReadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChapters.isEmpty()) {
                    Toast.makeText(App.getContext(), R.string.chapter_list_not_ready,
                            Toast.LENGTH_SHORT).show();
                } else {
                    showChapter(mHistoryChapterIndex, mChapters.get(mHistoryChapterIndex));
                }
            }
        });
    }
}
