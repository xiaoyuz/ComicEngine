package com.xiaoyuz.comicengine.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.GotoFragmentOperation;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseActivity;
import com.xiaoyuz.comicengine.base.LazyInstance;
import com.xiaoyuz.comicengine.fragment.DefaultFragment;
import com.xiaoyuz.comicengine.fragment.SearchEngineFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private class EventHandler {
        @Subscribe
        public void onGotoFragmentOperation(GotoFragmentOperation operation) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, operation.getFragment());
            if (operation.isNeedBack()) {
                ft.addToBackStack(operation.getFragment().getClass().getSimpleName());
            }
            ft.commitAllowingStateLoss();
        }
    }

    private LazyInstance<SearchEngineFragment> mLazySearchEngineFragment;
    private LazyInstance<DefaultFragment> mLazyDefaultFragment;

    private NavigationView mNavigationView;

    private EventHandler mEventHandler;

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
        mEventHandler = new EventHandler();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        selectNavItem(0);

        EventDispatcher.register(mEventHandler);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        switch (id) {
            case R.id.nav_camera:
                EventDispatcher.post(new GotoFragmentOperation(mLazySearchEngineFragment.get(),
                        false));
                break;
            default:
                EventDispatcher.post(new GotoFragmentOperation(mLazyDefaultFragment.get(),
                        false));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventDispatcher.unregister(mEventHandler);
    }

    private void selectNavItem(int pos) {
        onNavigationItemSelected(mNavigationView.getMenu().getItem(pos).setChecked(true));
    }
}
