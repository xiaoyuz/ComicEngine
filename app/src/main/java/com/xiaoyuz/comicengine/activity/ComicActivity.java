package com.xiaoyuz.comicengine.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.fragment.NavigationFragment;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public class ComicActivity extends BaseActivity {

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

    private EventHandler mEventHandler;
    private LazyInstance<NavigationFragment> mLazyNavigationFragment;

    @Override
    protected void initVariables() {
        mEventHandler = new EventHandler();
        mLazyNavigationFragment = new LazyInstance<>(
                new LazyInstance.InstanceCreator<NavigationFragment>() {
            @Override
            public NavigationFragment createInstance() {
                return new NavigationFragment();
            }
        });
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.comic_activity);

        EventDispatcher.register(mEventHandler);
        EventDispatcher.post(new GotoFragmentOperation(mLazyNavigationFragment.get()));
    }

    @Override
    protected void loadData() {

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
