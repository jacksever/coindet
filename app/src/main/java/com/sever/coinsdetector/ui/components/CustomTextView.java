package com.sever.coinsdetector.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sever.coinsdetector.R;
import com.sever.coinsdetector.utils.Utils;
import com.sever.coinsdetector.enums.FontEnum;

public final class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {
    private FontEnum font;

    public CustomTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        if (attributes.hasValue(R.styleable.CustomTextView_customFont))
            this.font = FontEnum.values()[Integer.parseInt(attributes.getString(R.styleable.CustomTextView_customFont))];
        else
            this.font = FontEnum.BOLD;

        init();
        attributes.recycle();
    }

    private void init() {
        this.setTypeface(font == FontEnum.BOLD ? Utils.getFont(getContext(), "bold.ttf") :
                Utils.getFont(getContext(), "medium.ttf"));
    }

    public void setFont(@NonNull FontEnum font) {
        this.font = font;
        init();
    }
}
