package com.sever.coinsdetector.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sever.coinsdetector.R;

public class ViewPort extends ViewGroup {

    public ViewPort(@NonNull Context context) {
        this(context, null);
    }

    public ViewPort(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPort(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        super.dispatchDraw(canvas);

        int viewportCornerRadius = 0;
        Paint eraser = new Paint();
        eraser.setAntiAlias(true);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        float width = getContext().getResources().getDimension(R.dimen.test);
        float height = getContext().getResources().getDimension(R.dimen.test);

        RectF rect = new RectF(
                (float) getWidth() / 2 - width / 2,
                (float) getHeight() / 2 - height / 2,
                (float) getWidth() / 2 + width / 2,
                (float) getHeight() / 2 + height / 2);
        canvas.drawRoundRect(rect, (float) viewportCornerRadius, (float) viewportCornerRadius, eraser);
    }
}
