package com.xiaoyuz.comicengine.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {

    class SearchResultsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView;

        public SearchResultsViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            titleTextView = (TextView) view.findViewById(R.id.title);
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
    public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results_item,
                parent,false);
        return new SearchResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultsViewHolder holder, int position) {
        String imageUrl = mSearchResults.get(position).getBookCover();
        String title = mSearchResults.get(position).getTitle();
        holder.titleTextView.setText(title);
//        holder.itemView.setTag(imageUrl);
        Glide.with(App.getContext()).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }
}
