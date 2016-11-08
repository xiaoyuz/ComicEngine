package com.xiaoyuz.comicengine.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.contract.presenter.SearchResultPresenter;
import com.xiaoyuz.comicengine.db.source.local.BookLocalDataSource;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.fragment.SearchResultsFragment;

/**
 * Search result list and book detail.
 * Created by zhangxiaoyu on 16/10/31.
 */
public class BookInfoActivity extends BaseActivity {

    public static class GotoFragmentOperation {

        private BaseFragment mFragment;

        public GotoFragmentOperation(BaseFragment fragment) {
            mFragment = fragment;
        }

        public BaseFragment getFragment() {
            return mFragment;
        }
    }

    private class EventHandler {
        @Subscribe
        public void onGotoFragmentOperation(GotoFragmentOperation operation) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.fragment_container, operation.getFragment(),
                    operation.getFragment().getClass().getSimpleName());
            ft.addToBackStack(operation.getFragment().getClass().getSimpleName());
            ft.commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
    }

    private LazyInstance<SearchResultsFragment> mLazySearchResultsFragment;
    private EventHandler mEventHandler;

    @Override
    protected void initVariables() {
        mLazySearchResultsFragment = new LazyInstance<>(
                new LazyInstance.InstanceCreator<SearchResultsFragment>() {
                    @Override
                    public SearchResultsFragment createInstance() {
                        return new SearchResultsFragment();
                    }
                });
        mEventHandler = new EventHandler();
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.book_info_layout);

        EventDispatcher.register(mEventHandler);

        mLazySearchResultsFragment.get().setArguments(getIntent().getExtras());
        new SearchResultPresenter(BookRepository.getInstance(BookLocalDataSource.getInstance(),
                BookRemoteDataSource.getInstance()),
                mLazySearchResultsFragment.get());
        EventDispatcher.post(new GotoFragmentOperation(mLazySearchResultsFragment.get()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            }
            return getSupportFragmentManager().getBackStackEntryCount() >= 1;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventDispatcher.unregister(mEventHandler);
    }
}
