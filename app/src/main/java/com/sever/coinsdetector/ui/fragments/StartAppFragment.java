package com.sever.coinsdetector.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sever.coinsdetector.R;
import com.sever.coinsdetector.utils.Storage;
import com.sever.coinsdetector.databinding.StartFragmentBinding;

public class StartAppFragment extends Fragment implements Animation.AnimationListener {
    private StartFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = StartFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Animation bounceAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_anim1);
        bounceAnimation.setAnimationListener(this);

        binding.logo.setVisibility(View.VISIBLE);
        binding.logo.startAnimation(bounceAnimation);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    private void openNextFragment() {
        if (Storage.SETTINGS != null)
            if (Storage.SETTINGS.getBoolean(Storage.INTRO, false))
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_startAppFragment_to_mainScreenFragment);
            else
                // to Intro
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_startAppFragment_to_introFragment);
        else
            // to Intro
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_startAppFragment_to_introFragment);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        binding.logo.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.bounce_anim2));
        new Handler().postDelayed(this::openNextFragment, 200);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
