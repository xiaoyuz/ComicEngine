package com.xiaoyuz.comicengine.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zhangxiaoyu on 16-10-11.
 * Base activity that divide life cycle.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    protected abstract void initVariables();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void loadData();
}