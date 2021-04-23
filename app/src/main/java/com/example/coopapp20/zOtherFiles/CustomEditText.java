package com.example.coopapp20.zOtherFiles;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.material.textfield.TextInputEditText;

public class CustomEditText extends TextInputEditText {

    public CustomEditText(Context context) {
        super(context);
        setDrawableClickListener(target -> {
            if (target == CustomEditText.DrawableClickListener.DrawablePosition.RIGHT) {
                setText("");
            }
        });
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawableClickListener(target -> {
            if (target == CustomEditText.DrawableClickListener.DrawablePosition.RIGHT) {
                setText("");
            }
        });
    }

    private Drawable drawableRight;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableBottom;

    int actionX, actionY;

    private DrawableClickListener clickListener;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Drawable[] icons = getCompoundDrawables();

        if (icons[0] != null) {drawableLeft = icons[0];}
        if (icons[1] != null) {drawableTop = icons[1];}
        if (icons[2] != null) {drawableRight = icons[2];}
        if (icons[3] != null) {drawableBottom = icons[3];}

        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();

            if (drawableBottom != null && drawableBottom.getBounds().contains(actionX, actionY)) {
                clickListener.onClick(DrawableClickListener.DrawablePosition.BOTTOM);
                return super.onTouchEvent(event);
            }

            if (drawableTop != null && drawableTop.getBounds().contains(actionX, actionY)) {
                clickListener.onClick(DrawableClickListener.DrawablePosition.TOP);
                return super.onTouchEvent(event);
            }

            if (drawableLeft != null) {
                bounds = drawableLeft.copyBounds();

                int ExtraClickableAreaPercent = 50;

                int ExtraClickableArea      = ((drawableLeft.getIntrinsicHeight() / 2) * ExtraClickableAreaPercent) / 100;
                int DrawableTopCoordinat    = ((getHeight() / 2) - (drawableLeft.getIntrinsicHeight() / 2)) - ExtraClickableArea;
                int DrawableBottomCoordinat = ((getHeight() / 2) + (drawableLeft.getIntrinsicHeight() / 2)) + ExtraClickableArea;
                int DrawableLeftCoordinat   = (bounds.left  + getPaddingStart()) - ExtraClickableArea;
                int DrawableRightCoordinat  = (bounds.right + getPaddingStart()) + ExtraClickableArea;

                bounds.set(DrawableLeftCoordinat,DrawableTopCoordinat,DrawableRightCoordinat,DrawableBottomCoordinat);
                if (bounds.contains(actionX, actionY) && clickListener != null) {
                    clickListener.onClick(DrawableClickListener.DrawablePosition.LEFT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return true;
                }
            }

            if (drawableRight != null) {
                bounds = drawableRight.copyBounds();

                int ExtraClickableAreaPercent = 50;

                int ExtraClickableArea  = ((drawableRight.getIntrinsicHeight() / 2) * ExtraClickableAreaPercent) / 100;
                int DrawableTopCoordinat    = ((getHeight() / 2) - (drawableRight.getIntrinsicHeight() / 2)) - ExtraClickableArea;
                int DrawableBottomCoordinat = ((getHeight() / 2) + (drawableRight.getIntrinsicHeight() / 2)) + ExtraClickableArea;
                int DrawableLeftCoordinat   = (bounds.left  + getPaddingEnd()) - ExtraClickableArea;
                int DrawableRightCoordinat  = (bounds.right + getPaddingEnd()) + ExtraClickableArea;

                bounds.set(DrawableLeftCoordinat,DrawableTopCoordinat,DrawableRightCoordinat,DrawableBottomCoordinat);
                actionX = getWidth() - actionX;
                if (bounds.contains(actionX, actionY) && clickListener != null) {
                    clickListener.onClick(DrawableClickListener.DrawablePosition.RIGHT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return true;
                }
                return super.onTouchEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        drawableBottom = null;
        drawableLeft = null;
        drawableTop = null;
        super.finalize();
    }

    public void setDrawableClickListener(DrawableClickListener listener) {
        this.clickListener = listener;
    }

    public interface DrawableClickListener {
        enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT }
        void onClick(DrawablePosition target);
    }
}