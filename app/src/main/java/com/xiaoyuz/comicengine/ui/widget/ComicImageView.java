package com.xiaoyuz.comicengine.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.contract.PageContract;
import com.xiaoyuz.comicengine.entity.Page;
import com.xiaoyuz.comicengine.utils.App;

/**
 * Layout for image type detail view, just show image. Used in viewpager.
 */
public class ComicImageView extends RelativeLayout implements PageContract.View{

    protected ImageView mImageView;
    protected Context mContext;
    protected ImageView mLoadingView;
    private PageContract.Presenter mPresenter;

    public ComicImageView(Context ctx) {
        super(ctx);
        mContext = ctx;
        init();
    }

    public ComicImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }

    public ImageView getImageView() {
        return mImageView;
    }

    protected void init() {
        mImageView = new ImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(params);
        mImageView.setBackgroundColor(0xff000000);
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setVisibility(GONE);
        this.addView(mImageView);

        mLoadingView = new ImageView(App.getContext());
        LayoutParams loadingParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        loadingParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLoadingView.setLayoutParams(loadingParams);
        mLoadingView.setImageResource(R.drawable.loading);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                App.getContext(), R.anim.loading_animation);
        mLoadingView.startAnimation(hyperspaceJumpAnimation);
        this.addView(mLoadingView);
    }

    @Override
    public void setPresenter(PageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPage(Page page) {
        Glide.with(getContext()).load(page.getImageUrl()).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource,
                                        GlideAnimation<? super GlideDrawable> glideAnimation) {
                mLoadingView.clearAnimation();
                mLoadingView.setVisibility(GONE);
                mImageView.setImageDrawable(resource);
                mImageView.setVisibility(VISIBLE);
            }
        });
    }
}
