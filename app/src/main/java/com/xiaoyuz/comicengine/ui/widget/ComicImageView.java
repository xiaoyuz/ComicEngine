package com.xiaoyuz.comicengine.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.contract.ComicImageContract;
import com.xiaoyuz.comicengine.model.entity.base.BasePage;
import com.xiaoyuz.comicengine.event.ComicPageControlEvent;

import java.lang.ref.WeakReference;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Layout for image type detail view, just show image. Used in viewpager.
 */
public class ComicImageView extends RelativeLayout implements ComicImageContract.View {

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(final String url, final String html) {
            mPresenter.loadPage(true, url, html);
        }
    }

    final class ComicWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:window.local_obj.showSource('" + url + "','<head>'+" +
                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            super.onPageFinished(view, url);
        }
    }

    protected PhotoView mImageView;
    protected Context mContext;
    protected ImageView mLoadingView;
    protected TextView mReloadView;
    private ComicImageContract.Presenter mPresenter;

    private WebView mWebView;
    private WebView mWeakWebView;

    private String mUrl;

    public ComicImageView(Context context) {
        super(context);
        mContext = context;
    }

    public ComicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initLayout(attrs);
    }

    public ImageView getImageView() {
        return mImageView;
    }

    protected void initLayout(AttributeSet attrs) {
        mImageView = new PhotoView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(params);
        mImageView.setBackgroundColor(0xff000000);
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setVisibility(GONE);
        mImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                EventDispatcher.post(new ComicPageControlEvent(
                        ComicPageControlEvent.SINGLE_CLICK_TYPE));
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });

        mLoadingView = new ImageView(mContext);
        LayoutParams loadingParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        loadingParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLoadingView.setLayoutParams(loadingParams);

        mReloadView = new TextView(mContext);
        LayoutParams reloadParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        reloadParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mReloadView.setLayoutParams(reloadParams);
        mReloadView.setText("Error! Click to Reload.");
        mReloadView.setTextSize(18);
        mReloadView.setVisibility(GONE);

        // WebView
        mWebView = new WebView(getContext());
        WeakReference<WebView> webViewWeakReference = new WeakReference<>(mWebView);
        mWeakWebView = webViewWeakReference.get();
        LayoutParams webViewParams = new LayoutParams(0, 0);
        mWeakWebView.setLayoutParams(webViewParams);
        mWeakWebView.getSettings().setJavaScriptEnabled(true);
        mWeakWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        mWeakWebView.setWebViewClient(new ComicWebViewClient());
        mWeakWebView.setVisibility(INVISIBLE);

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,
                R.styleable.ComicImageView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ComicImageView_loading_anim:
                    Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                            getContext(), typedArray.getResourceId(
                                    R.styleable.ComicImageView_loading_anim, 0));
                    mLoadingView.startAnimation(hyperspaceJumpAnimation);
                    break;
                case R.styleable.ComicImageView_loading_src:
                    mLoadingView.setImageResource(typedArray.getResourceId(
                            R.styleable.ComicImageView_loading_src, 0));
                    break;
                case R.styleable.ComicImageView_background_color:
                    mImageView.setBackgroundColor(typedArray.getResourceId(
                            R.styleable.ComicImageView_background_color, 0));
                    break;
            }
        }
        this.addView(mImageView);
        this.addView(mLoadingView);
        this.addView(mReloadView);
        this.addView(mWeakWebView);
        typedArray.recycle();
    }

    @Override
    public void setPresenter(ComicImageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPage(BasePage page) {
        final WeakReference<PhotoView> imageViewWeakReference = new WeakReference<>(mImageView);
        final ImageView weakImageView = imageViewWeakReference.get();
        if (weakImageView != null) {
            Glide.with(getContext()).load(page.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(
                    new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource,
                                                    GlideAnimation<? super GlideDrawable>
                                                            glideAnimation) {
                            mLoadingView.clearAnimation();
                            mLoadingView.setVisibility(GONE);
                            weakImageView.setImageDrawable(resource);
                            weakImageView.setVisibility(VISIBLE);
                        }
                    });
        }
    }

    @Override
    public void loadUrlByWebView(String url) {
        mWeakWebView.loadUrl(url);
    }

    @Override
    public void showNetError() {
        final WeakReference<PhotoView> imageViewWeakReference = new WeakReference<>(mImageView);
        final ImageView weakImageView = imageViewWeakReference.get();
        mReloadView.setVisibility(VISIBLE);
        weakImageView.setVisibility(GONE);
        mLoadingView.setVisibility(GONE);

        mReloadView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mReloadView.setVisibility(GONE);
                mLoadingView.setVisibility(VISIBLE);
                mPresenter.loadHtmlPage(mUrl);
            }
        });
    }

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

    public void recycle() {
        mWeakWebView.stopLoading();
        mWebView = null;
        mWeakWebView = null;
        mImageView.setImageDrawable(null);
        mPresenter = null;
    }
}
