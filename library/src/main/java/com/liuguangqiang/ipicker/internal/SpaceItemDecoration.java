package com.liuguangqiang.ipicker.internal;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Eric on 16/9/12.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    private int columnCount = 4;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.top = space;

        int position = parent.getChildLayoutPosition(view);

        if (position % columnCount == 0) {
            outRect.left = 0;
        }

        if (position < columnCount) {
            outRect.top = 0;
        }
    }
}
