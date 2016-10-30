package com.xiaoyuz.comicengine;

import com.xiaoyuz.comicengine.base.BaseFragment;

/**
 * Created by zhangxiaoyu on 16/10/30.
 */
public class GotoFragmentOperation {

    private BaseFragment mFragment;
    private boolean isNeedBack;

    public GotoFragmentOperation(BaseFragment fragment, boolean needBack) {
        mFragment = fragment;
        isNeedBack = needBack;
    }

    public BaseFragment getFragment() {
        return mFragment;
    }

    public boolean isNeedBack() {
        return isNeedBack;
    }
}
