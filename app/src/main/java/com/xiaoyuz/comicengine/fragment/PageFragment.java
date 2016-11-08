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
import com.xiaoyuz.comicengine.event.PageDestroyEvent;
import com.xiaoyuz.comicengine.ui.adapter.PageAdapter;
import com.xiaoyuz.comicengine.utils.Constants;

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
            }
        }
    }

    class OnPageChange implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position,
                                   float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                StringBuffer pageNumInfoSB = new StringBuffer()
                        .append(mViewPager.getCurrentItem() + 1)
                        .append("/").append(mPageUrls.size());
                mPageNumView.setText(pageNumInfoSB.toString());
            }
        }
    }

    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private RelativeLayout mHeader;
    private RelativeLayout mBottom;
    private TextView mPageNumView;

    private String mBookUrl;
    private ArrayList<String> mPageUrls;
    private int mChapterIndex;
    private int mHistoryPosition;
    private String mChapterTitle;

    private EventHandler mEventHandler;

    private PageContract.Presenter mPresenter;

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        mBookUrl = bundle.getString(Constants.Bundle.PAGE_FRAGMENT_BOOK_URL);
        mPageUrls = bundle.getStringArrayList(Constants.Bundle.PAGE_FRAGMENT_PAGE_URLS);
        mChapterIndex = bundle.getInt(Constants.Bundle.PAGE_FRAGMENT_CHAPTER_INDEX);
        mHistoryPosition = bundle.getInt(Constants.Bundle.PAGE_FRAGMENT_HISTORY_POSITION);
        mChapterTitle = bundle.getString(Constants.Bundle.PAGE_FRAGMENT_CHAPTER_TITLE);
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
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setVisibility(View.INVISIBLE);
        mViewPager.addOnPageChangeListener(new OnPageChange());
        if (mHistoryPosition != 0) {
            jump2HistoryPage(mHistoryPosition);
        } else {
            mViewPager.setVisibility(View.VISIBLE);
        }

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
        mPresenter.saveChapterHistory(mBookUrl, mChapterIndex, mChapterTitle,
                mViewPager.getCurrentItem());
        EventDispatcher.post(new PageDestroyEvent(mBookUrl));
    }

    @Override
    public void jump2HistoryPage(int position) {
        mViewPager.setCurrentItem(position, false);
        mViewPager.setVisibility(View.VISIBLE);
    }
}
