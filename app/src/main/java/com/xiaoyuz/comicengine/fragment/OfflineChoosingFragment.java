package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.OfflineChoosingContract;
import com.xiaoyuz.comicengine.event.OfflineChoosingEvent;
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.ui.adapter.OfflineChapterAdapter;
import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.Constants;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-18.
 */
public class OfflineChoosingFragment extends BaseFragment implements
        OfflineChoosingContract.View {

    private class EventHandler {
        @Subscribe
        public void onOfflineChoosing(OfflineChoosingEvent event) {
            int index = mChapters.indexOf(event.getChapter());
            mChapters.get(index).setOfflined(!mChapters.get(index).isOfflined());
            if (mChapters.get(index).isOfflined()) {
                mPresenter.addOfflineChapter(mBookDetail, mChapters.get(index));
            } else {
                mPresenter.deleteOfflineChapter(mBookDetail, mChapters.get(index));
            }
            mAdapter.notifyItemChanged(index);
        }
    }

    private RecyclerView mRecyclerView;
    private OfflineChapterAdapter mAdapter;

    private OfflineChoosingContract.Presenter mPresenter;
    private List<BaseChapter> mChapters;
    private BaseBookDetail mBookDetail;

    private EventHandler mEventHandler;

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        mChapters = (List<BaseChapter>) bundle.getSerializable(Constants
                .Bundle.PAGE_FRAGMENT_OFFLINE_CHAPTER);
        mBookDetail = (BaseBookDetail) bundle.getSerializable(Constants
                .Bundle.PAGE_FRAGMENT_OFFLINE_DETAIL);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offline_choosing_fragment,
                container, false);

        mPresenter.subscribe();
        mAdapter = new OfflineChapterAdapter(mChapters, mPresenter);
        mEventHandler = new EventHandler();

        initBackableToolBar(view, "Offline");
        EventDispatcher.register(mEventHandler);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter);

        view.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        return view;
    }

    @Override
    protected void loadData() {
        mPresenter.loadOfflineChapters(mBookDetail);
    }

    @Override
    public void showOfflineChapters(List<BaseChapter> offlinedChapters) {
        for (int i = 0; i < mChapters.size(); i++) {
            if (offlinedChapters.contains(mChapters.get(i))) {
                mChapters.get(i).setOfflined(true);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(OfflineChoosingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventDispatcher.unregister(mEventHandler);
        mPresenter.unsubscribe();
    }
}
