package com.xiaoyuz.comicengine.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
import com.xiaoyuz.comicengine.net.JsoupParser;
import com.xiaoyuz.comicengine.utils.App;
import com.xiaoyuz.comicengine.utils.Contants;

import org.jsoup.nodes.Document;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Layout for image type detail view, just show image. Used in viewpager.
 */
public class ComicImageView extends RelativeLayout implements PageContract.View {

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(final String html) {
            Log.d("HTML", html);

            Observable.create(
                    new Observable.OnSubscribe<Page>() {
                        @Override
                        public void call(Subscriber<? super Page> subscriber) {
                            Document doc = JsoupParser.getDocumentByCode(html);
                            subscriber.onNext(new Page(doc));
                            subscriber.onCompleted();
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Page>() {
                        @Override
                        public void call(Page page) {
                            loadComic(page);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }
    }

    final class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("WebView", "onPageFinished ");
            view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                    "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            super.onPageFinished(view, url);
        }
    }

    protected ImageView mImageView;
    protected Context mContext;
    protected ImageView mLoadingView;
    private PageContract.Presenter mPresenter;

    private WebView mWebView;

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

        // WebView
        mWebView = new WebView(App.getContext());
        LayoutParams webViewParams = new LayoutParams(0, 0);
        mWebView.setLayoutParams(webViewParams);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.loadUrl("http://m.57mh.com/282/0469.html");
        mWebView.setVisibility(INVISIBLE);
        this.addView(mWebView);
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

    @Override
    public void showPage(String url) {
        mWebView.loadUrl(Contants.MOBILE_URL_DOMAIN + url);
    }

    private void loadComic(Page page) {
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
}
