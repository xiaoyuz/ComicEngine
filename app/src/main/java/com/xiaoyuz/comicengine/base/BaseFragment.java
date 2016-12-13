package com.xiaoyuz.comicengine.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.ui.helper.FragmentHelper;

/**
 * Created by zhangxiaoyu on 16-10-11.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = initView(inflater, container, savedInstanceState);
        view.setClickable(true);
        loadData();
        return view;
    }

    protected void back() {
        FragmentHelper helper = new FragmentHelper(getActivity().getSupportFragmentManager());
        if (helper.backStackCount() == 1) {
            getActivity().finish();
        } else {
            helper.popFragment();
        }
    }

    protected void initBackableToolBar(View view, String title) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        toolbar.setTitle(title);
    }

    protected abstract void initVariables();

    protected abstract View initView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState);

    protected abstract void loadData();
}
