package com.xiaoyuz.comicengine.fragment.navigation;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.OfflineContract;
import com.xiaoyuz.comicengine.model.entity.base.BaseOfflineBook;
import com.xiaoyuz.comicengine.ui.helper.OfflineExpandableHelper;
import com.xiaoyuz.comicengine.utils.App;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-18.
 */
public class OfflineFragment extends BaseFragment implements OfflineContract.View {

    private OfflineContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private OfflineExpandableHelper mHelper;

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offline_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        return view;
    }

    @Override
    protected void initVariables() {
    }

    @Override
    protected void loadData() {
        mPresenter.loadOfflineBooks();
    }

    @Override
    public void showOfflineBooks(List<BaseOfflineBook> offlineBooks) {
        mHelper = new OfflineExpandableHelper(App.getContext(), mRecyclerView, offlineBooks);
        mHelper.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(OfflineContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
