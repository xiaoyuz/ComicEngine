package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.activity.ComicActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.fragment.navigation.DefaultFragment;
import com.xiaoyuz.comicengine.fragment.navigation.SearchEngineFragment;

/**
 * Created by zhangxiaoyu on 16-11-9.
 */
public class NavigationFragment extends BaseFragment
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private View mBaseView;
    private LazyInstance<SearchEngineFragment> mLazySearchEngineFragment;
    private LazyInstance<DefaultFragment> mLazyDefaultFragment;

    @Override
    protected void initVariables() {
        mLazyDefaultFragment =
                new LazyInstance<>(new LazyInstance
                        .InstanceCreator<DefaultFragment>() {
                    @Override
                    public DefaultFragment createInstance() {
                        return new DefaultFragment();
                    }
                });
        mLazySearchEngineFragment =
                new LazyInstance<>(new LazyInstance
                        .InstanceCreator<SearchEngineFragment>() {
                    @Override
                    public SearchEngineFragment createInstance() {
                        return new SearchEngineFragment();
                    }
                });
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.navigation_fragment, container, false);

        Toolbar toolbar = (Toolbar) mBaseView.findViewById(R.id.toolbar);
        ((ComicActivity) getActivity()).setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) mBaseView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) mBaseView.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) mBaseView.findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        selectNavItem(0);

        return mBaseView;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        switch (id) {
            case R.id.nav_camera:
                fragment = mLazySearchEngineFragment.get();
                break;
            default:
                fragment = mLazyDefaultFragment.get();
                break;
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commitAllowingStateLoss();

        DrawerLayout drawer = (DrawerLayout) mBaseView.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectNavItem(int pos) {
        onNavigationItemSelected(mNavigationView.getMenu().getItem(pos).setChecked(true));
    }
}
