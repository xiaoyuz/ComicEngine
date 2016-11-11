package com.xiaoyuz.comicengine.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.contract.presenter.ComicImagePresenter;
import com.xiaoyuz.comicengine.db.source.local.BookLocalDataSource;
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
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(App.getContext());
        View pageView = layoutInflater.inflate(R.layout.page_layout, null);
        ComicImageView comicImageView
                = (ComicImageView) pageView.findViewById(R.id.comic_image_view);
        new ComicImagePresenter(
                BookRepository.getInstance(BookLocalDataSource.getInstance(),
                        BookRemoteDataSource.getInstance()), comicImageView)
                .loadHtmlPage(App.getEntityFactory().getPageUrlPrefix()
                        + mPageUrls.get(position));
        collection.addView(pageView, 0);
        return pageView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {

        ((ComicImageView) ((View) view).findViewById(R.id.comic_image_view)).recycle();
        collection.removeView((View) view);
    }
}
