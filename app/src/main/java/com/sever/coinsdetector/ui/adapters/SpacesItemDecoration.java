package com.sever.coinsdetector.ui.adapters;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int spaceBaseline;
    private final int spaceBottom;
    private int count;

    public SpacesItemDecoration(int spaceBaseline, int spaceBottom, int count) {
        this.spaceBaseline = spaceBaseline;
        this.spaceBottom = spaceBottom;
        this.count = count;
    }

    public void setElementSize(int count) {
        this.count = count;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1)
            outRect.top = spaceBaseline;

        if (parent.getChildAdapterPosition(view) % 2 != 0)
            outRect.left = spaceBaseline / 2;
        else
            outRect.right = spaceBaseline / 2;

        if (parent.getChildAdapterPosition(view) == count - 1 || parent.getChildAdapterPosition(view) == count - 2)
            outRect.bottom = spaceBottom;
    }
}
