package com.xiaoyuz.comicengine.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View view =  initView(inflater, container, savedInstanceState);
        view.setClickable(true);
        loadData();
        return view;
    }

    protected abstract void initVariables();

    protected abstract View initView(LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState);

    protected abstract void loadData();
}
