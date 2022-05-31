package com.sever.coinsdetector.ui.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sever.coinsdetector.R;

public class NotFoundDialog {
    private final Context context;
    private final BottomSheetDialog dialog;
    private final CardView close;

    public NotFoundDialog(@NonNull Context context) {
        this.dialog = new BottomSheetDialog(context, R.style.BottomSheetDialog);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.dialog.setContentView(R.layout.dialog_not_found);

        this.close = dialog.findViewById(R.id.close);
        this.context = context;
    }

    public void openDialog() {
        dialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        close.setOnClickListener(v -> dismiss());
        dialog.setOnDismissListener(dialogInterface -> Navigation.findNavController((FragmentActivity) context, R.id.fragmentView).popBackStack());
        show();
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
