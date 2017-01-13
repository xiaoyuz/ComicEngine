package com.xiaoyuz.comicengine.ui.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xiaoyuz.comicengine.R;

/**
 * Created by zhangxiaoyu on 16-12-13.
 */
public class FragmentHelper {

    private FragmentManager mFragmentManager;

    public FragmentHelper(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        int backStackCount = backStackCount();
        if (backStackCount > 0) {
            Fragment fromFragment = mFragmentManager.findFragmentByTag(mFragmentManager
                    .getBackStackEntryAt(backStackCount - 1).getName());
            ft.hide(fromFragment);
        }
        ft.add(R.id.activity_fragment_container, fragment, fragment.getClass().getSimpleName());
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.commit();
        mFragmentManager.executePendingTransactions();
    }

    public void popFragment() {
        mFragmentManager.popBackStack();
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment preFragment = mFragmentManager.findFragmentByTag(mFragmentManager
                .getBackStackEntryAt(backStackCount() - 1).getName());
        ft.show(preFragment);
        ft.commit();
        mFragmentManager.executePendingTransactions();
    }

    public int backStackCount() {
        return mFragmentManager.getBackStackEntryCount();
    }
}
