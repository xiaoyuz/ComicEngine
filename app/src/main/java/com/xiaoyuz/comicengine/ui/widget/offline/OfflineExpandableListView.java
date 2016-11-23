package com.xiaoyuz.comicengine.ui.widget.offline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by zhangxiaoyu on 16-11-22.
 */
public class OfflineExpandableListView extends RecyclerView {

    private Context mContext;

    public OfflineExpandableListView(Context context) {
        super(context);
        mContext = context;
    }

    public OfflineExpandableListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mContext = context;
    }
}
