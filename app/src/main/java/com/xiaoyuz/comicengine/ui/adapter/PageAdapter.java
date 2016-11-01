package com.xiaoyuz.comicengine.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.contract.PageContract;
import com.xiaoyuz.comicengine.contract.presenter.PagePresenter;
import com.xiaoyuz.comicengine.db.source.BookDataSource;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.ui.widget.ComicImageView;
import com.xiaoyuz.comicengine.utils.App;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-1.
 */
public class PageAdapter extends PagerAdapter {

    private final List<String> mPageUrls;
    private int mCurrentPosition = -1;
    private View mCurrentView;

    public PageAdapter(List<String> pageUrls) {
        this.mPageUrls = pageUrls;
    }

    @Override
    public int getCount() {
        return mPageUrls.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, final int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentView = (View) object;
        if (mCurrentPosition == position) return;
        mCurrentPosition = position;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final ComicImageView iv = new ComicImageView(App.getContext());
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        PageContract.Presenter presenter = new PagePresenter(
                BookRepository.getInstance(BookRemoteDataSource.getInstance()), iv);
        presenter.loadPage(mPageUrls.get(position));
        collection.addView(iv, 0);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
