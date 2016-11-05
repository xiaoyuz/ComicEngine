package com.xiaoyuz.comicengine.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.contract.SearchResultContract;
import com.xiaoyuz.comicengine.entity.SearchResult;
import com.xiaoyuz.comicengine.utils.App;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class SearchResultsAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    class SearchResultViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        RelativeLayout layout;
        ImageView imageView;
        TextView titleTextView;

        public SearchResultViewHolder(View view){
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.layout);
            imageView = (ImageView) view.findViewById(R.id.image);
            titleTextView = (TextView) view.findViewById(R.id.title);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SearchResult searchResult = (SearchResult) v.getTag();
            mPresenter.openBookDetail(searchResult);
        }
    }

    private List<SearchResult> mSearchResults;

    private SearchResultContract.Presenter mPresenter;

    public SearchResultsAdapter(List<SearchResult> searchResults,
                           SearchResultContract.Presenter presenter) {
        mSearchResults = searchResults;
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_item,
                        parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchResultViewHolder) {
            String imageUrl = mSearchResults.get(position).getBookCover();
            String title = mSearchResults.get(position).getTitle();
            ((SearchResultViewHolder) holder).titleTextView.setText(title);
            holder.itemView.setTag(mSearchResults.get(position));
            Glide.with(App.getContext()).load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(((SearchResultViewHolder) holder).imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }
}
