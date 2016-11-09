package com.xiaoyuz.comicengine.fragment.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;

/**
 * Created by zhangxiaoyu on 16-10-18.
 */
public class DefaultFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_fragment, container, false);
        return view;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void loadData() {

    }
}
