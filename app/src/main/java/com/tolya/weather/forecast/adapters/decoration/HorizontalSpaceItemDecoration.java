package com.tolya.weather.forecast.adapters.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Tolik on 24.01.2017.
 */

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int horizontalRightSpace;
    private int horizontalLeftSpaceFromFirstRow;

    public HorizontalSpaceItemDecoration(int horizontalRightSpace, int horizontalLeftSpaceFromFirstRow) {
        this.horizontalRightSpace = horizontalRightSpace;
        this.horizontalLeftSpaceFromFirstRow = horizontalLeftSpaceFromFirstRow;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = horizontalLeftSpaceFromFirstRow;
        }
        outRect.right = horizontalRightSpace;
    }
}
