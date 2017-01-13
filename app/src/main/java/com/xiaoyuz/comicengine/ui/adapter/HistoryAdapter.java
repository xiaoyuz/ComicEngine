package com.xiaoyuz.comicengine.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.swipe.SwipeLayout;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.contract.HistoryContract;
import com.xiaoyuz.comicengine.model.entity.base.BaseHistory;
import com.xiaoyuz.comicengine.utils.App;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-10-28.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    class HistoryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        SwipeLayout swipeLayout;
        RelativeLayout layout;
        ImageView imageView;
        TextView titleTextView;
        Button deleteButton;

        public HistoryViewHolder(View view){
            super(view);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
            layout = (RelativeLayout) view.findViewById(R.id.layout);
            imageView = (ImageView) view.findViewById(R.id.image);
            titleTextView = (TextView) view.findViewById(R.id.title);
            deleteButton = (Button) view.findViewById(R.id.delete);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BaseHistory history = (BaseHistory) v.getTag();
            mPresenter.openBookDetail(history);
        }
    }

    private List<BaseHistory> mHistories;

    private HistoryContract.Presenter mPresenter;

    public HistoryAdapter(List<BaseHistory> histories, HistoryContract.Presenter presenter) {
        mHistories = histories;
        mPresenter = presenter;
    }

    public void deleteHistory(int position) {
        mHistories.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mHistories.size());
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item,
                        parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        String imageUrl = mHistories.get(position).getBookCover();

        holder.titleTextView.setText(mHistories.get(position).getTitle());

        holder.layout.setTag(mHistories.get(position));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.deleteHistory(position, mHistories.get(position));
            }
        });
        Glide.with(App.getContext()).load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mHistories.size();
    }
}
