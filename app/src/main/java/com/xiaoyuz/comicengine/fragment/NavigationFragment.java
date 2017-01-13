package com.xiaoyuz.comicengine.fragment;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.activity.ComicActivity;
import com.xiaoyuz.comicengine.base.BaseActivity;
import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.contract.presenter.HistoryPresenter;
import com.xiaoyuz.comicengine.contract.presenter.OfflinePresenter;
import com.xiaoyuz.comicengine.db.source.local.BookLocalDataSource;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.fragment.navigation.BaseChildFragment;
import com.xiaoyuz.comicengine.fragment.navigation.DefaultFragment;
import com.xiaoyuz.comicengine.fragment.navigation.HistoryFragment;
import com.xiaoyuz.comicengine.fragment.navigation.OfflineFragment;
import com.xiaoyuz.comicengine.fragment.navigation.SearchEngineFragment;
import com.xiaoyuz.comicengine.ui.widget.slidemenu.interfaces.Resourceble;
import com.xiaoyuz.comicengine.ui.widget.slidemenu.interfaces.ScreenShotable;
import com.xiaoyuz.comicengine.ui.widget.slidemenu.model.SlideMenuItem;
import com.xiaoyuz.comicengine.ui.widget.slidemenu.util.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public class NavigationFragment extends BaseChildFragment
        implements ViewAnimator.ViewAnimatorListener {

    private DrawerLayout mDrawerLayout;
    private LinearLayout mLeftDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<SlideMenuItem> mSlideMenuItems = new ArrayList<>();
    private ViewAnimator viewAnimator;

    private Toolbar mToolbar;
    private LazyInstance<SearchEngineFragment> mLazySearchEngineFragment;
    private LazyInstance<HistoryFragment> mLazyHistoryFragment;
    private LazyInstance<OfflineFragment> mLazyOfflineFragment;

    @Override
    protected void initVariables() {
        mLazySearchEngineFragment =
                new LazyInstance<>(new LazyInstance
                        .InstanceCreator<SearchEngineFragment>() {
                    @Override
                    public SearchEngineFragment createInstance() {
                        return new SearchEngineFragment();
                    }
                });
        mLazyHistoryFragment =
                new LazyInstance<>(new LazyInstance.InstanceCreator<HistoryFragment>() {
                    @Override
                    public HistoryFragment createInstance() {
                        return new HistoryFragment();
                    }
                });
        mLazyOfflineFragment =
                new LazyInstance<>(new LazyInstance.InstanceCreator<OfflineFragment>() {
                    @Override
                    public OfflineFragment createInstance() {
                        return new OfflineFragment();
                    }
                });
    }

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.navigation_fragment,
                container, false));
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((ComicActivity) getActivity()).setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mLeftDrawer = (LinearLayout) rootView.findViewById(R.id.left_drawer);
        mLeftDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        setActionBar();

//        selectNavItem(0);
        createMenuList();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mLazySearchEngineFragment.get())
                .commit();

        viewAnimator = new ViewAnimator<>((BaseActivity) getActivity(),
                mSlideMenuItems, mLazySearchEngineFragment.get(),
                mDrawerLayout, this);

        return rootView;
    }

    private void setActionBar() {
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(), mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mLeftDrawer.removeAllViews();
                mLeftDrawer.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && mLeftDrawer.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    protected void loadData() {

    }

    private void createMenuList() {
        mSlideMenuItems.add(new SlideMenuItem(getString(R.string.nav_close),
                R.drawable.ic_menu_close));
        mSlideMenuItems.add(new SlideMenuItem(getString(R.string.nav_search),
                R.drawable.ic_menu_search));
        mSlideMenuItems.add(new SlideMenuItem(getString(R.string.nav_search),
                R.drawable.ic_menu_recent_history));
        mSlideMenuItems.add(new SlideMenuItem(getString(R.string.nav_offline),
                R.drawable.ic_menu_offline));
    }

    @Override
    public void disableHomeButton() {
        ((BaseActivity) getActivity()).getSupportActionBar()
                .setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        ((BaseActivity) getActivity())
                .getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        mLeftDrawer.addView(view);
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem,
                                   ScreenShotable screenShotable, int position) {
        BaseChildFragment fragment = null;
        switch (slideMenuItem.getImageRes()) {
            case R.drawable.ic_menu_search:
                fragment = mLazySearchEngineFragment.get();
                break;
            case R.drawable.ic_menu_recent_history:
                fragment = mLazyHistoryFragment.get();
                new HistoryPresenter(BookRepository.getInstance(BookLocalDataSource.getInstance(),
                        BookRemoteDataSource.getInstance()), (HistoryFragment) fragment);
                break;
            case R.drawable.ic_menu_offline:
                fragment = mLazyOfflineFragment.get();
                new OfflinePresenter(BookRepository.getInstance(BookLocalDataSource.getInstance(),
                        BookRemoteDataSource.getInstance()), (OfflineFragment) fragment);
                break;
            default:
                break;
        }
        if (fragment == null) {
            return screenShotable;
        }
        replaceFragment(fragment, position);
        return fragment;
    }

    private void replaceFragment(BaseChildFragment baseChildFragment,
                                           int topPosition) {
        View view = rootView.findViewById(R.id.fragment_container);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils
                .createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        rootView.findViewById(R.id.content_overlay).setBackgroundDrawable(
                new BitmapDrawable(getResources(), baseChildFragment.getBitmap()));
        animator.start();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, baseChildFragment).commit();
    }
}
