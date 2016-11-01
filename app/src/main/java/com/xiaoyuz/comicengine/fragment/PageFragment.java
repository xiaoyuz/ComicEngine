package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.ui.adapter.PageAdapter;

import java.util.ArrayList;

/**
 * Created by zhangxiaoyu on 16-10-18.
 */
public class PageFragment extends BaseFragment {

    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;

    private ArrayList<String> mPageUrls;

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        mPageUrls = bundle.getStringArrayList("urls");
    }

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fragment, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.viewer);
        mPageAdapter = new PageAdapter(mPageUrls);
        mViewPager.setAdapter(mPageAdapter);
        return view;
    }

    @Override
    protected void loadData() {

    }
}
