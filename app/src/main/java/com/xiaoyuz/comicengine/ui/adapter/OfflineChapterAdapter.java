package com.xiaoyuz.comicengine.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoyuz.comicengine.EventDispatcher;
import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.contract.OfflineChoosingContract;
import com.xiaoyuz.comicengine.event.OfflineChoosingEvent;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16/10/29.
 */
public class OfflineChapterAdapter extends
        RecyclerView.Adapter<OfflineChapterAdapter.ChapterViewHolder> {

    class ChapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView chapterInfoTextView;

        public ChapterViewHolder(View view){
            super(view);
            chapterInfoTextView = (TextView) view.findViewById(R.id.chapter_info);
            chapterInfoTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventDispatcher.post(new OfflineChoosingEvent((BaseChapter) v.getTag()));
        }
    }

    private List<BaseChapter> mChapters;
    private OfflineChoosingContract.Presenter mPresenter;

    public OfflineChapterAdapter(List<BaseChapter> chapters,
                                 OfflineChoosingContract.Presenter presenter) {
        mChapters = chapters;
        mPresenter = presenter;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offline_chapter_item,
                parent,false);
        return new ChapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mChapters.size();
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        String chapterInfo = mChapters.get(position).getTitle() + " "
                + mChapters.get(position).getPageInfo();
        holder.chapterInfoTextView.setText(chapterInfo);
        holder.chapterInfoTextView.setTag(mChapters.get(position));
        // Remember, when using viewholder to resuse item displaying,
        // there is an "if", there must be an "else"!
        holder.chapterInfoTextView.setTextColor(mChapters.get(position).isOfflined() ?
                Color.GREEN : Color.GRAY);
    }
}
