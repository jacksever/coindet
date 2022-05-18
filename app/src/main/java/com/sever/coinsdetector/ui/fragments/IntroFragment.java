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

import com.sever.coinsdetector.R;
import com.sever.coinsdetector.utils.Storage;
import com.sever.coinsdetector.databinding.IntroFragmentBinding;

public class IntroFragment extends Fragment {
    private IntroFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = IntroFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.catalog.setOnClickListener(v -> {
            Storage.SETTINGS.edit().putBoolean(Storage.INTRO, true).apply();
            NavHostFragment.findNavController(this).navigate(R.id.action_introFragment_to_mainScreenFragment);
        });

        binding.search.setOnClickListener(v -> {
            Storage.SETTINGS.edit().putBoolean(Storage.INTRO, true).apply();
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_introFragment_to_cameraFragment);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
