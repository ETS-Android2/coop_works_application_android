package com.example.coopapp20.zOtherFiles;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.icu.text.RelativeDateTimeFormatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.state.State;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterSwipeHelper extends androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback {

    private Drawable LeftIcon;
    private final Drawable LeftBackground;
    private onSwipeListener LeftListener;
    private Drawable RightIcon;
    private final Drawable RightBackground;
    private onSwipeListener RightListener;
    private DisplayMetrics Metrics;

    public AdapterSwipeHelper(onSwipeListener leftListener, Drawable leftIcon, Drawable leftBackground, onSwipeListener rightListener, Drawable rightIcon, Drawable rightBackground, DisplayMetrics metrics) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.LeftListener = leftListener;
        this.LeftIcon = leftIcon;
        this.LeftBackground = leftBackground;
        this.RightListener = rightListener;
        this.RightIcon = rightIcon;
        this.RightBackground = rightBackground;
        Metrics = metrics;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == ItemTouchHelper.LEFT && LeftListener != null){
            LeftListener.onSwipe(viewHolder.getAdapterPosition());
        }else if (direction == ItemTouchHelper.RIGHT && RightListener != null){
            RightListener.onSwipe(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 100;

        //Drawing the icon behind the item being swiped
        int iconPadding = (int) Metrics.density*8;
        int iconTop = itemView.getTop() + iconPadding;
        int iconBottom = itemView.getBottom() - iconPadding;

        if (dX > 0) { // Swiping to the right
            //Log.e("AdapterSwipeHelper","Dragging right");
            if(RightIcon != null && RightBackground != null) {
                int iconRight = itemView.getLeft() - (itemView.getTop() - itemView.getBottom()) - iconPadding;
                int iconLeft = itemView.getLeft() + iconPadding;
                RightIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                RightBackground.setBounds(itemView.getLeft(),itemView.getTop(), itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                RightBackground.draw(c);
                RightIcon.draw(c);
            }

        } else if (dX < 0) { // Swiping to the left
            if(LeftIcon != null && LeftBackground != null) {
                int iconRight = itemView.getRight() - iconPadding;
                int iconLeft = itemView.getRight() + (itemView.getTop() - itemView.getBottom()) + iconPadding;
                LeftIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                LeftBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                LeftBackground.draw(c);
                LeftIcon.draw(c);
            }
        }
    }

    public interface onSwipeListener{
        void onSwipe(int position);
    }
}
