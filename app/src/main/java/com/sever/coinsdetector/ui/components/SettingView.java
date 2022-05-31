package com.sever.coinsdetector.ui.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.sever.coinsdetector.R;
import com.sever.coinsdetector.enums.FontEnum;

/**
 * Custom implementation of part of the settings with <b>switchCompat</b>
 * <p>
 * Ability to add and remove explanatory messages <b>subtitle</b>
 *
 * @version 1.2
 */
public final class SettingView extends RelativeLayout {
    private CustomTextView title, subtitle;
    private SwitchCompat switchCompat;
    private ImageView imageButton;
    private String titleData, subtitleData;

    private boolean isSubtitleUsed = false;
    private boolean switchEnable;

    /**
     * If <b>View</b> has no attributes, then initialize an empty one <b>switchCompat</b>
     *
     * @param context is non null
     */
    public SettingView(@NonNull Context context) {
        super(context);
        init();
    }

    /**
     * If <b>View</b> has attributes, then initialize <b>title</b> and <b>subtitle</b>.
     *
     * @param context is non null
     * @param attrs   is nullable
     */
    public SettingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomSettingView);

        if (attributes.getString(R.styleable.CustomSettingView_title) != null)
            titleData = attributes.getString(R.styleable.CustomSettingView_title);

        if (attributes.getString(R.styleable.CustomSettingView_subtitle) != null) {
            isSubtitleUsed = true;
            subtitleData = attributes.getString(R.styleable.CustomSettingView_subtitle);
        }

        switchEnable = attributes.getBoolean(R.styleable.CustomSettingView_switchEnable, false);

        init();
        attributes.recycle();
    }

    /**
     * This method initializes the <b>View</b>.
     */
    private void init() {
        title = new CustomTextView(getContext());
        title.setId(View.generateViewId());
        this.addView(title);
        initialTitle(titleData);

        if (switchEnable) {
            switchCompat = new SwitchCompat(getContext());
            switchCompat.setId(View.generateViewId());
            this.addView(switchCompat);
            initialSwitch();
            addOnClickListenerSwitch();
        } else {
            imageButton = new ImageView(getContext());
            imageButton.setId(View.generateViewId());
            this.addView(imageButton);
            initialImageButton();
            setClickStyle();
        }

        if (isSubtitleUsed) {
            subtitle = new CustomTextView(getContext());
            this.addView(subtitle);
            initialSubtitle(subtitleData);
        }
    }

    /**
     * This method initializes the <b>title</b>.
     *
     * @param data is title string
     */
    private void initialTitle(@NonNull String data) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) title.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        if (!isSubtitleUsed)
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        title.setLayoutParams(params);

        title.setTextColor(getContext().getColor(R.color.black));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        title.setFont(FontEnum.BOLD);
        title.setText(data);
    }

    /**
     * This method initializes the <b>subtitle</b>.
     *
     * @param data is subtitle string
     */
    private void initialSubtitle(@NonNull String data) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) subtitle.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0,
                (int) getContext().getResources().getDimension(R.dimen.customSettingView_marginTop),
                (int) getContext().getResources().getDimension(R.dimen.customSettingView_marginEnd),
                0);
        params.addRule(RelativeLayout.BELOW, title.getId());
        params.addRule(RelativeLayout.START_OF, switchEnable ? switchCompat.getId() : imageButton.getId());
        subtitle.setLayoutParams(params);

        subtitle.setTextColor(getContext().getColor(R.color.grey_dark));
        subtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        subtitle.setFont(FontEnum.MEDIUM);
        subtitle.setText(data);
    }

    /**
     * This method initializes the <b>switch</b>.
     */
    private void initialSwitch() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) switchCompat.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = (int) getContext().getResources().getDimension(R.dimen.switchSize);
        params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        switchCompat.setLayoutParams(params);

        switchCompat.setThumbResource(R.drawable.thumb_selector);
        switchCompat.setTrackResource(R.drawable.switch_track);
    }

    /**
     * This method initializes the <b>imageButton</b>.
     */
    private void initialImageButton() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageButton.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        imageButton.setLayoutParams(params);

        imageButton.setImageResource(R.drawable.ic_arrow_right);
    }

    /**
     * This method sets the text in the title
     *
     * @param text - non null
     */
    public void setTitle(@NonNull String text) {
        this.title.setText(text);
    }

    /**
     * This method sets the text in the subtitle
     *
     * @param text - nullable
     */
    public void setSubtitle(String text) {
        if (text == null)
            this.subtitle.setVisibility(GONE);
        else
            this.subtitle.setText(text);
    }

    /**
     * This method sets the click visibility for the button
     */
    private void setClickStyle() {
        this.setBackgroundResource(R.drawable.ripple_button);
    }

    /**
     * This method sets the internal listener to click by <b>switch</b>.
     */
    private void addOnClickListenerSwitch() {
        this.setOnClickListener(v -> switchCompat.setChecked(!switchCompat.isChecked()));
    }

    /**
     * @return <b>switchCompat</b>.
     */
    @NonNull
    public SwitchCompat getSwitch() {
        return switchCompat;
    }

    /**
     * @return checked state of switch.
     */
    public boolean isChecked() {
        return switchCompat.isChecked();
    }

    /**
     * This methods sets checking state for switch.
     *
     * @param checked - state for switch.
     */
    public void setChecked(boolean checked) {
        switchCompat.setChecked(checked);
    }
}
