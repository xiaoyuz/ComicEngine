package com.xiaoyuz.comicengine.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.contract.presenter.SearchResultPresenter;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.fragment.SearchResultsFragment;

/**
 * Search result list and book detail.
 * Created by zhangxiaoyu on 16/10/31.
 */
public class SearchResultActivity extends BaseActivity {

    public static class GotoFragmentOperation {

        private BaseFragment mFragment;
        private boolean mNeedHide;

        public GotoFragmentOperation(BaseFragment fragment, boolean needHide) {
            mFragment = fragment;
            mNeedHide = needHide;
        }

        public BaseFragment getFragment() {
            return mFragment;
        }

        public boolean isNeedHide() {
            return mNeedHide;
        }
    }

    private class EventHandler {
        @Subscribe
        public void onGotoFragmentOperation(GotoFragmentOperation operation) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if (fm.getBackStackEntryCount() != 0) {
                String fragmentTag =
                        fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName();
                Fragment topmostFragment = fm.findFragmentByTag(fragmentTag);
                if (topmostFragment != null) {
                    ft.hide(topmostFragment);
                }
            }
            ft.add(R.id.fragment_container, operation.getFragment(),
                    operation.getFragment().getClass().getSimpleName());
            ft.show(operation.getFragment());
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
        new SearchResultPresenter(BookRepository.getInstance(
                BookRemoteDataSource.getInstance()),
                mLazySearchResultsFragment.get());
        EventDispatcher.post(new GotoFragmentOperation(mLazySearchResultsFragment.get(), false));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventDispatcher.unregister(mEventHandler);
    }
}
