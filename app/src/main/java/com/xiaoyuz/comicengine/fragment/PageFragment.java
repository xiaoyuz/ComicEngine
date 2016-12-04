package com.xiaoyuz.comicengine.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.cache.ComicEngineCache;
import com.xiaoyuz.comicengine.contract.PageContract;
import com.xiaoyuz.comicengine.event.ComicPageControlEvent;
import com.xiaoyuz.comicengine.event.PageDestroyEvent;
import com.xiaoyuz.comicengine.event.PageTurningEvent;
import com.xiaoyuz.comicengine.ui.adapter.PageAdapter;
import com.xiaoyuz.comicengine.ui.widget.ComicViewPager;
import com.xiaoyuz.comicengine.utils.Constants;
import com.xiaoyuz.comicengine.utils.DeviceUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by zhangxiaoyu on 16-10-18.
 */
public class PageFragment extends BaseFragment implements PageContract.View,
        SeekBar.OnSeekBarChangeListener {

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
                updatePageNum();
                syncSeekBar();
            }
            if(leftEdge != null && !leftEdge.isFinished()) {
                showPageTurningSnackBar(false);
            }
            if(rightEdge != null && !rightEdge.isFinished()) {
                showPageTurningSnackBar(true);
            }
        }
    }

    private ComicViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private RelativeLayout mHeader;
    private RelativeLayout mBottom;
    private TextView mPageNumView;
    private TextView mChapterView;
    private SeekBar mSeekBar;

    private EdgeEffectCompat leftEdge;
    private EdgeEffectCompat rightEdge;

    private String mBookUrl;
    private ArrayList<String> mPageUrls;
    private int mChapterIndex;
    private int mHistoryPosition;
    private String mChapterTitle;

    private EventHandler mEventHandler;

    private PageContract.Presenter mPresenter;
    private int mProgress;

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

        mViewPager = (ComicViewPager) view.findViewById(R.id.viewer);
        mPageAdapter = new PageAdapter(mPageUrls);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setVisibility(View.INVISIBLE);
        mViewPager.addOnPageChangeListener(new OnPageChange());
        if (mHistoryPosition != 0) {
            jump2LastRead(mHistoryPosition);
        } else {
            mViewPager.setVisibility(View.VISIBLE);
        }

        try {
            Field mLeftEdgeField = mViewPager.getClass()
                    .getSuperclass().getDeclaredField("mLeftEdge");
            Field mLightEdgeField = mViewPager.getClass()
                    .getSuperclass().getDeclaredField("mRightEdge");
            if (mLeftEdgeField != null && mLightEdgeField != null) {
                mLeftEdgeField.setAccessible(true);
                mLightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) mLeftEdgeField.get(mViewPager);
                rightEdge = (EdgeEffectCompat) mLightEdgeField.get(mViewPager);
            }
        } catch (Exception e) {
        }

        mHeader = (RelativeLayout) view.findViewById(R.id.header);
        mBottom = (RelativeLayout) view.findViewById(R.id.bottom);
        mPageNumView = (TextView) view.findViewById(R.id.page_num);
        mChapterView = (TextView) view.findViewById(R.id.chapter);
        mChapterView.setText(mChapterTitle);

        mSeekBar = (SeekBar) view.findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(this);

        view.findViewById(R.id.rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateScreen();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DeviceUtils.rotateScreen(getActivity(), ComicEngineCache.getPageOrientation());
    }

    @Override
    protected void loadData() {
        updatePageNum();
        syncSeekBar();
    }

    @Override
    public void setPresenter(PageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPause() {
        super.onPause();
        DeviceUtils.rotateScreen(getActivity(), ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean userControl) {
        mProgress = progress;
        if (userControl) {
            updatePageNumBySeekBarDragging();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int jumpPageNum = (int) (mPageUrls.size() * (mProgress / 100.0));
        mViewPager.setCurrentItem(jumpPageNum, false);
        updatePageNum();
    }

    @Override
    public void jump2LastRead(int position) {
        mViewPager.setCurrentItem(position, false);
        mViewPager.setVisibility(View.VISIBLE);
    }

    private void syncSeekBar() {
        mSeekBar.setProgress((int) (((double) mViewPager.getCurrentItem()
                / mPageUrls.size()) * 100));
    }

    private void updatePageNum() {
        displayPageInfo(mViewPager.getCurrentItem() + 1);
    }

    private void updatePageNumBySeekBarDragging() {
        int jumpPageNum = (int) (mPageUrls.size() * (mProgress / 100.0));
        displayPageInfo(Math.min(jumpPageNum + 1, mPageUrls.size()));
    }

    private void displayPageInfo(int pageNum) {
        StringBuffer pageNumInfoSB = new StringBuffer()
                .append(pageNum)
                .append("/").append(mPageUrls.size());
        mPageNumView.setText(pageNumInfoSB.toString());
    }

    private void rotateScreen() {
        int orientation = getActivity().getRequestedOrientation();
        DeviceUtils.rotateScreen(getActivity(),
                orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ComicEngineCache.putPageOrientation(getActivity().getRequestedOrientation());
    }

    private void showPageTurningSnackBar(boolean isLastPage) {
        Snackbar snackbar = Snackbar.make(getView(), isLastPage ? "This is the last page." :
                "This is the first page.",
                Snackbar.LENGTH_LONG);
        if (!isLastPage) {
            if (mChapterIndex != 0) {
                snackbar.setAction("Prev chapter",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                back();
                                EventDispatcher.post(new PageTurningEvent(false,
                                        mChapterIndex));
                            }
                        });
            }
        } else {
            snackbar.setAction("Next chapter",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            back();
                            EventDispatcher.post(new PageTurningEvent(true, mChapterIndex));
                        }
                    });
        }
        snackbar.show();
    }
}
