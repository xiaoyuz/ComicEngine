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
import com.xiaoyuz.comicengine.contract.HistoryContract;
import com.xiaoyuz.comicengine.model.entity.history.History;
import com.xiaoyuz.comicengine.utils.App;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    class HistoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        RelativeLayout layout;
        ImageView imageView;
        TextView titleTextView;

        public HistoryViewHolder(View view){
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.layout);
            imageView = (ImageView) view.findViewById(R.id.image);
            titleTextView = (TextView) view.findViewById(R.id.title);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            History history = (History) v.getTag();
            mPresenter.openBookDetail(history);
        }
    }

    private List<History> mHistories;

    private HistoryContract.Presenter mPresenter;

    public HistoryAdapter(List<History> histories, HistoryContract.Presenter presenter) {
        mHistories = histories;
        mPresenter = presenter;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item,
                        parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        String imageUrl = mHistories.get(position).getBookCover();

        holder.titleTextView.setText(mHistories.get(position).getTitle());

        holder.itemView.setTag(mHistories.get(position));
        Glide.with(App.getContext()).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mHistories.size();
    }
}
