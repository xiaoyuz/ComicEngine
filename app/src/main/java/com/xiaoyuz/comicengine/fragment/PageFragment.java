package com.xiaoyuz.comicengine.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.PageContract;
import com.xiaoyuz.comicengine.event.ComicPageControlEvent;
import com.xiaoyuz.comicengine.ui.adapter.PageAdapter;

import java.util.ArrayList;

/**
 * Created by zhangxiaoyu on 16-10-18.
 */
public class PageFragment extends BaseFragment implements PageContract.View {

    private class EventHandler {
        @Subscribe
        public void onComicPageClicked(ComicPageControlEvent event) {
            switch (event.getType()) {
                case ComicPageControlEvent.SINGLE_CLICK_TYPE:
                    if (mHeader.getVisibility() == View.GONE) {
                        mHeader.setVisibility(View.VISIBLE);
                        mBottom.setVisibility(View.VISIBLE);
                    } else {
                        mHeader.setVisibility(View.GONE);
                        mBottom.setVisibility(View.GONE);
                    }
                    break;
                case ComicPageControlEvent.FLIP_TYPE:
                    mCurrentPosition = event.getPosition();
                    StringBuffer pageNumInfoSB = new StringBuffer().append(mCurrentPosition + 1)
                            .append("/").append(mPageUrls.size());
                    mPageNumView.setText(pageNumInfoSB.toString());
                    break;
            }
        }
    }

    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private RelativeLayout mHeader;
    private RelativeLayout mBottom;
    private TextView mPageNumView;

    private String mChapterUrl;
    private ArrayList<String> mPageUrls;
    private int mCurrentPosition;

    private EventHandler mEventHandler;

    private PageContract.Presenter mPresenter;

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        mChapterUrl = bundle.getString("chapterUrl");
        mPageUrls = bundle.getStringArrayList("urls");
        mEventHandler = new EventHandler();
        EventDispatcher.register(mEventHandler);
    }

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fragment, container, false);

        mViewPager = (ViewPager) view.findViewById(R.id.viewer);
        mPageAdapter = new PageAdapter(mPageUrls);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.setVisibility(View.INVISIBLE);
        mPresenter.loadChapterHistory(mChapterUrl);

        mHeader = (RelativeLayout) view.findViewById(R.id.header);
        mBottom = (RelativeLayout) view.findViewById(R.id.bottom);
        mPageNumView = (TextView) view.findViewById(R.id.page_num);
        return view;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void setPresenter(PageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventDispatcher.unregister(mEventHandler);
        mPresenter.saveChapterHistory(mChapterUrl, mCurrentPosition);
    }

    @Override
    public void jump2HistoryPage(int position) {
        mViewPager.setCurrentItem(position, false);
        mViewPager.setVisibility(View.VISIBLE);
    }
}
