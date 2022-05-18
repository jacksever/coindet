package com.sever.coinsdetector.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sever.coinsdetector.utils.Storage;
import com.sever.coinsdetector.databinding.SettingsBinding;
import com.sever.coinsdetector.ui.dialogs.ClearMemoryDialog;

public class SettingsFragment extends Fragment {
    private SettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = SettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.gallery.setChecked(Storage.SETTINGS.getBoolean(Storage.GALLERY, true));
        binding.files.setChecked(Storage.SETTINGS.getBoolean(Storage.FILE_MANAGER, false));

        binding.memory.setOnClickListener(v -> new ClearMemoryDialog(requireContext()).openDialog());

        binding.gallery.getSwitch().setOnCheckedChangeListener((compoundButton, b) -> {
            if (binding.gallery.isChecked()) {
                binding.files.setChecked(false);
                Storage.SETTINGS.edit()
                        .putBoolean(Storage.GALLERY, true)
                        .putBoolean(Storage.FILE_MANAGER, false)
                        .apply();
            }
        });

        binding.files.getSwitch().setOnCheckedChangeListener((compoundButton, b) -> {
            if (binding.files.isChecked()) {
                binding.gallery.setChecked(false);
                Storage.SETTINGS.edit()
                        .putBoolean(Storage.GALLERY, false)
                        .putBoolean(Storage.FILE_MANAGER, true)
                        .apply();
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(SettingsFragment.this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
