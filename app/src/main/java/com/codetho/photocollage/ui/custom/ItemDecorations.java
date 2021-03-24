package com.codetho.photocollage.ui.custom;

import android.support.v7.widget.RecyclerView;



import android.graphics.Rect;
import android.view.View;



public class ItemDecorations extends RecyclerView.ItemDecoration {

    private int mSizeGridSpacingPx;
    private int mGridSize;
    private int centerCounter = 1;
    private int rightCounter = 2;
    private int leftLargeCounter = 1;
    private boolean isLeft = false;

    public ItemDecorations(int gridSpacingPx, int gridSize) {
        mSizeGridSpacingPx = gridSpacingPx;
        mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) mSizeGridSpacingPx * (mGridSize - 1)) / mGridSize);
        int padding = parent.getWidth() / mGridSize - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        if (itemPosition == 0) {
            centerCounter = 1;
            rightCounter = 2;
            leftLargeCounter = 1;
            isLeft = false;
        }

        int left = 0, top = 0, right = 0, bottom = 0;
        if (centerCounter == itemPosition || centerCounter + 9 == itemPosition) {
            //MOVE FROM CENTER TO RIGHT
            outRect.top = mSizeGridSpacingPx;
            outRect.right = 0;
            outRect.left = padding;
            centerCounter += 9;
            left = outRect.left;
        } else if (rightCounter == itemPosition || rightCounter + 18 == itemPosition) {
            //MOVE FROM RIGHT TO LEFT
            outRect.top = mSizeGridSpacingPx;
            outRect.left = 0;
            outRect.right = padding;
            rightCounter += 18;
            right = outRect.right;
        } else if (itemPosition % mGridSize == 0) {
            outRect.top = mSizeGridSpacingPx;
            outRect.left = 0;
            outRect.right = padding;
            right = outRect.right;
        } else if ((itemPosition + 1) % mGridSize == 0) {
            outRect.top = mSizeGridSpacingPx;
            outRect.right = 0;
            outRect.left = padding;
            left = outRect.left;
        } else {
            outRect.top = mSizeGridSpacingPx;
            outRect.left = mSizeGridSpacingPx - padding;
            if ((itemPosition + 2) % mGridSize == 0) {
                outRect.right = mSizeGridSpacingPx - padding;
            } else {
                outRect.right = mSizeGridSpacingPx / 2;
            }
            left = outRect.left;
            right = outRect.right;
        }
        outRect.bottom = 0;
        int temp = leftLargeCounter + (isLeft ? 8 : 10);
        if (itemPosition == 1 || temp == itemPosition) {
            outRect.top = mSizeGridSpacingPx;
            if (isLeft) {
                outRect.left = 0;
                outRect.right = mSizeGridSpacingPx - padding;
                right = outRect.right;
            } else {
                outRect.left = mSizeGridSpacingPx - padding;
                outRect.right = 0;
            }
            if (itemPosition > 1)
                leftLargeCounter = temp;
            isLeft = !isLeft;
            left = outRect.left;
        }

        //Rect create issue for items, so it is set to zero
        //But used in calculations, so in future when have some problem in padding
        //you can use the Rect
        outRect.top = mSizeGridSpacingPx + padding + padding + padding;
        outRect.left = outRect.right = outRect.top = outRect.bottom = 0;

        view.setPadding(left, mSizeGridSpacingPx, right, 0);

    }
}
