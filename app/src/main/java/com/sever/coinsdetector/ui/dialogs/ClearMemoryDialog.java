package com.sever.coinsdetector.ui.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sever.coinsdetector.R;
import com.sever.coinsdetector.ui.components.CustomTextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ClearMemoryDialog {
    private final Context context;
    private final BottomSheetDialog dialog;
    private final CardView positiveButton, negativeButton;
    private final CustomTextView weight;

    public ClearMemoryDialog(@NonNull Context context) {
        this.dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialog_clear_memory);

        this.positiveButton = dialog.findViewById(R.id.cancel);
        this.negativeButton = dialog.findViewById(R.id.clear);
        this.weight = dialog.findViewById(R.id.weight);

        this.context = context;
    }

    public void openDialog() {
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        File cache = context.getCacheDir();
        weight.setText(context.getString(R.string.clear_memory_subtitle,
                android.text.format.Formatter.formatShortFileSize(context, (cache.canRead() ? FileUtils.sizeOfDirectory(cache) : 0))));

        positiveButton.setOnClickListener(v -> dialog.dismiss());
        negativeButton.setOnClickListener(v -> {
            if (cache.canRead()) {
                try {
                    FileUtils.deleteDirectory(cache);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
        });

        show();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
