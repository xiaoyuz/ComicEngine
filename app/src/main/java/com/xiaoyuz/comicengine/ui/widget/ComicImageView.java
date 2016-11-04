package com.xiaoyuz.comicengine.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.xiaoyuz.comicengine.utils.Contants;

import uk.co.senab.photoview.PhotoView;

/**
 * Layout for image type detail view, just show image. Used in viewpager.
 */
public class ComicImageView extends RelativeLayout implements PageContract.View {

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(final String url, final String html) {
            mPresenter.saveHtmlToLocal(url, html);
            mPresenter.loadPage(html);
        }
    }

    final class MyWebViewClient extends WebViewClient {
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
    private PageContract.Presenter mPresenter;

    private WebView mWebView;

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

        mLoadingView = new ImageView(App.getContext());
        LayoutParams loadingParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        loadingParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mLoadingView.setLayoutParams(loadingParams);

        // WebView
        mWebView = new WebView(App.getContext());
        LayoutParams webViewParams = new LayoutParams(0, 0);
        mWebView.setLayoutParams(webViewParams);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setVisibility(INVISIBLE);

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
        this.addView(mWebView);
        typedArray.recycle();
    }

    @Override
    public void setPresenter(PageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showPage(Page page) {
        Glide.with(getContext()).load(page.getImageUrl()).into(
                new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mLoadingView.clearAnimation();
                        mLoadingView.setVisibility(GONE);
                        mImageView.setImageDrawable(resource);
                        mImageView.setVisibility(VISIBLE);
                    }
                });
    }

    @Override
    public void loadUrlByWebView(String url) {
        mWebView.loadUrl(url);
    }
}
