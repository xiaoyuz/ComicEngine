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
import com.xiaoyuz.comicengine.model.entity.base.BaseBookDetail;
import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.utils.App;

import java.util.List;

/**
 * Created by zhangxiaoyu on 16-11-22.
 */
public class OfflineExpandableAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public interface SectionClickListener {
        void onClick(Meta meta);
    }

    public static class Meta {
        Object object;
        int viewType;
        boolean isExpanded;

        public Meta(Object object, int viewType) {
            this.object = object;
            this.viewType = viewType;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }
    }

    class SectionViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;
        ImageView imageView;
        TextView titleTextView;

        public SectionViewHolder(View view){
            super(view);
            layout = (RelativeLayout) view.findViewById(R.id.layout);
            imageView = (ImageView) view.findViewById(R.id.image);
            titleTextView = (TextView) view.findViewById(R.id.title);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSectionClickListener.onClick((Meta) view.getTag());
                }
            });
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView chapterInfoTextView;

        public ItemViewHolder(View view){
            super(view);
            chapterInfoTextView = (TextView) view.findViewById(R.id.chapter_info);
            chapterInfoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }

    public static final int VIEW_TYPE_SECTION = 1;
    public static final int VIEW_TYPE_ITEM = 2;

    private List<Meta> mMetaObjects;
    private SectionClickListener mSectionClickListener;

    public OfflineExpandableAdapter(List<Meta> metaObjects,
                                    SectionClickListener sectionClickListener) {
        mMetaObjects = metaObjects;
        mSectionClickListener = sectionClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_ITEM) {
            viewHolder = new ItemViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.offline_item, parent, false));
        } else {
            viewHolder = new SectionViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.offline_section, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Meta meta = mMetaObjects.get(position);
        switch (meta.viewType) {
            case VIEW_TYPE_SECTION:
                BaseBookDetail bookDetail = (BaseBookDetail) meta.object;
                String imageUrl = bookDetail.getSearchResult().getBookCover();

                ((SectionViewHolder) holder).titleTextView.setText(bookDetail
                        .getSearchResult().getTitle());

                holder.itemView.setTag(meta);
                Glide.with(App.getContext()).load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(((SectionViewHolder) holder).imageView);
                break;
            default:
                BaseChapter chapter = (BaseChapter) meta.object;
                String chapterInfo = chapter.getTitle() + " " + chapter.getPageInfo();
                ((ItemViewHolder) holder).chapterInfoTextView.setText(chapterInfo);
                ((ItemViewHolder) holder).chapterInfoTextView.setTag(meta);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mMetaObjects.get(position).viewType;
    }

    @Override
    public int getItemCount() {
        return mMetaObjects.size();
    }
}
