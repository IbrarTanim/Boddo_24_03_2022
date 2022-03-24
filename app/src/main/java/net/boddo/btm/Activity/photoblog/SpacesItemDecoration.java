package net.boddo.btm.Activity.photoblog;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int spaceTop;
    private final int spaceItems;

    public SpacesItemDecoration(int spaceBetweenItems ,int space) {
        this.spaceTop = space;
        this.spaceItems = spaceBetweenItems;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = spaceItems;
        outRect.right = spaceItems;
        outRect.bottom = spaceItems;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 1)
            outRect.top = spaceTop;

    }
}