package com.mememaker.mememakerpro.creatememe;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    public ItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // for left and right spacing
        outRect.left = mSpace;
        outRect.right = mSpace;

        outRect.bottom = mSpace;
        // Add top margin only for the first item to avoid double space between items
//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.top = mSpace;
//        }
    }
}