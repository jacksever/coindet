package com.sever.coinsdetector.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.sever.coinsdetector.R;
import com.sever.coinsdetector.enums.FontEnum;

public final class HeaderButton extends LinearLayoutCompat {
    private ImageView image;
    private CustomTextView text;
    private String data;

    public HeaderButton(@NonNull Context context) {
        super(context);
        init();
    }

    public HeaderButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HeaderButton);

        if (attributes.hasValue(R.styleable.HeaderButton_textButton))
            this.data = attributes.getString(R.styleable.HeaderButton_textButton);
        else
            this.data = getContext().getString(R.string.setting_title);

        init();
        attributes.recycle();
    }

    private void init() {
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
        image = new ImageView(getContext());
        text = new CustomTextView(getContext());

        generateStyle();
    }

    private void generateStyle() {
        image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        image.setImageResource(R.drawable.ic_arrow);
        this.addView(image);

        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(15);
        text.setText(data);
        text.setTextColor(getContext().getColor(R.color.black));
        text.setAllCaps(true);
        text.setPadding(15, 0, 0, 0);
        text.setLayoutParams(params);
        text.setTextSize(18);
        text.setFont(FontEnum.BOLD);
        this.addView(text);
    }

    public void setText(@NonNull String data) {
        this.text.setText(data);
    }
}
