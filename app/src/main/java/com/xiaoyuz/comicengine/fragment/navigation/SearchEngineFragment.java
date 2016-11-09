package com.xiaoyuz.comicengine.fragment.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.activity.ComicActivity;
import com.xiaoyuz.comicengine.base.BaseFragment;
import com.xiaoyuz.comicengine.contract.presenter.SearchResultPresenter;
import com.xiaoyuz.comicengine.db.source.local.BookLocalDataSource;
import com.xiaoyuz.comicengine.db.source.remote.BookRemoteDataSource;
import com.xiaoyuz.comicengine.db.source.repository.BookRepository;
import com.xiaoyuz.comicengine.fragment.SearchResultsFragment;
import com.xiaoyuz.comicengine.utils.Constants;

/**
 * Created by zhangxiaoyu on 16/10/27.
 */
public class SearchEngineFragment extends BaseFragment {

    @Override
    protected void initVariables() {
    }

    @Override
    protected View initView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_engine_fragment,
                container, false);
        final EditText keywordEditText = (EditText) view.findViewById(R.id.keyword);
        Button searchButton = (Button) view.findViewById(R.id.search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = keywordEditText.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.Bundle.BOOK_INFO_ACTIVITY_KEYWORD, keyword);
                SearchResultsFragment fragment = new SearchResultsFragment();
                fragment.setArguments(bundle);
                new SearchResultPresenter(BookRepository.getInstance(BookLocalDataSource
                        .getInstance(), BookRemoteDataSource.getInstance()), fragment);
                EventDispatcher.post(new ComicActivity.GotoFragmentOperation(fragment));
//                Intent intent = new Intent(getActivity(), BookInfoActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    protected void loadData() {

    }
}
