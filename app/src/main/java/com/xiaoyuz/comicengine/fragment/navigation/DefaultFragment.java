package com.xiaoyuz.comicengine.fragment.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.R;

/**
 * Created by zhangxiaoyu on 16-10-18.
 */
public class DefaultFragment extends BaseChildFragment {

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.default_fragment, container, false));
        return rootView;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void loadData() {

    }
}
