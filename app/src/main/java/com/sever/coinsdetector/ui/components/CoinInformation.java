package com.sever.coinsdetector.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.sever.coinsdetector.R;
import com.sever.coinsdetector.enums.FontEnum;

public class CoinInformation extends LinearLayoutCompat {
    private CustomTextView title, subtitle;

    public CoinInformation(@NonNull Context context) {
        super(context);
        init();
    }

    public CoinInformation(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CoinInformation);
        this.title.setText(attributes.getString(R.styleable.CoinInformation_title));
        this.subtitle.setText(attributes.getString(R.styleable.CoinInformation_subtitle));
        attributes.recycle();
    }

    private void init() {
        this.setOrientation(VERTICAL);

        LinearLayoutCompat.LayoutParams titleParams = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.title = new CustomTextView(getContext());
        this.title.setFont(FontEnum.MEDIUM);
        this.title.setTextColor(getContext().getColor(R.color.grey_dark));
        this.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        this.title.setLayoutParams(titleParams);
        this.addView(title);

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 3, 0, 0);

        this.subtitle = new CustomTextView(getContext());
        this.subtitle.setFont(FontEnum.BOLD);
        this.subtitle.setTextColor(getContext().getColor(R.color.black));
        this.subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        this.subtitle.setLayoutParams(params);
        this.addView(subtitle);
    }

    public void setTitle(@NonNull String text) {
        this.title.setText(text);
    }

    public void setSubtitle(@NonNull String text) {
        this.subtitle.setText(text);
    }
}
