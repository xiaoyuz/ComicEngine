package com.xiaoyuz.comicengine.fragment.navigation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.ui.widget.slidemenu.interfaces.ScreenShotable;

/**
 * Created by zhangxiaoyu on 2017/1/6.
 */
public abstract class BaseChildFragment extends BaseFragment implements ScreenShotable {

    protected View rootView;
    protected Bitmap screenShotBitmap;

    public void setRootView(View view) {
        rootView = view;
    }

    @Override
    public void takeScreenShot() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(),
                        rootView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                rootView.draw(canvas);
                screenShotBitmap = bitmap;
            }
        };

        thread.start();
    }

    @Override
    public Bitmap getBitmap() {
        return screenShotBitmap;
    }
}
