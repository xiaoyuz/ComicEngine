package com.xiaoyuz.comicengine.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoyuz.comicengine.R;
import com.xiaoyuz.comicengine.contract.BookDetailContract;
import com.xiaoyuz.comicengine.entity.Chapter;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16/10/29.
 */
public class ChapterAdapter extends
        RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    class ChapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        LinearLayout layout;
        TextView chapterInfoTextView;

        public ChapterViewHolder(View view){
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            chapterInfoTextView = (TextView) view.findViewById(R.id.chapter_info);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Chapter chapter = (Chapter) v.getTag();
            mPresenter.openChapter(chapter);
        }
    }

    private List<Chapter> mChapters;
    private BookDetailContract.Presenter mPresenter;

    public ChapterAdapter(List<Chapter> chapters, BookDetailContract.Presenter presenter) {
        mChapters = chapters;
        mPresenter = presenter;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item,
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
        holder.itemView.setTag(mChapters.get(position));
    }
}
