package com.xiaoyuz.comicengine.ui.helper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xiaoyuz.comicengine.model.entity.base.BaseChapter;
import com.xiaoyuz.comicengine.model.entity.base.BaseOfflineBook;
import com.xiaoyuz.comicengine.ui.adapter.OfflineExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxiaoyu on 16-11-23.
 */
public class OfflineExpandableHelper implements OfflineExpandableAdapter.SectionClickListener {

    private List<OfflineExpandableAdapter.Meta> mMetaObjects;

    private List<BaseOfflineBook> mOfflineBooks;
    private Map<OfflineExpandableAdapter.Meta, List<OfflineExpandableAdapter.Meta>> mMetaMap;

    private OfflineExpandableAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public OfflineExpandableHelper(Context context, RecyclerView recyclerView,
                                   List<BaseOfflineBook> offlineBook) {
        mMetaMap = new HashMap<>();
        mMetaObjects = new ArrayList<>();
        mRecyclerView = recyclerView;
        mOfflineBooks = offlineBook;
        initMetaMap();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new OfflineExpandableAdapter(mMetaObjects, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initMetaMap() {
        for (BaseOfflineBook offlineBook : mOfflineBooks) {
            OfflineExpandableAdapter.Meta section =
                    new OfflineExpandableAdapter.Meta(offlineBook.getBookDetail(),
                            OfflineExpandableAdapter.VIEW_TYPE_SECTION);
            List<OfflineExpandableAdapter.Meta> itemMetas = new ArrayList<>();
            for (BaseChapter chapter : offlineBook.getChapters()) {
                itemMetas.add(new OfflineExpandableAdapter.Meta(chapter,
                        OfflineExpandableAdapter.VIEW_TYPE_ITEM));
            }
            mMetaMap.put(section, itemMetas);
        }
    }

    public void notifyDataSetChanged() {
        generateDataList();
        mAdapter.notifyDataSetChanged();
    }

    private void generateDataList() {
        mMetaObjects.clear();
        for (Map.Entry<OfflineExpandableAdapter.Meta, List<OfflineExpandableAdapter.Meta>> entry :
                mMetaMap.entrySet()) {
            OfflineExpandableAdapter.Meta key;
            mMetaObjects.add((key = entry.getKey()));
            if (key.isExpanded()) {
                mMetaObjects.addAll(entry.getValue());
            }
        }
    }

    @Override
    public void onClick(OfflineExpandableAdapter.Meta meta) {
        meta.setExpanded(!meta.isExpanded());
        notifyDataSetChanged();
    }
}
