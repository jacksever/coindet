package com.sever.coinsdetector.ui.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sever.coinsdetector.R;
import com.sever.coinsdetector.databinding.CameraCompleteFragmentBinding;
import com.sever.coinsdetector.utils.Utils;

import java.io.File;

public class CameraCompleteFragment extends Fragment {
    private CameraCompleteFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = CameraCompleteFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.number1.setTypeface(Utils.getFont(requireContext(), "bold"));
        binding.number2.setTypeface(Utils.getFont(requireContext(), "bold"));
        binding.number3.setTypeface(Utils.getFont(requireContext(), "bold"));
        binding.number4.setTypeface(Utils.getFont(requireContext(), "bold"));

        File avers = new File(requireActivity().getFilesDir(), "avers.jpg");
        File revers = new File(requireActivity().getFilesDir(), "revers.jpg");

        if (avers.canRead() && revers.canRead()) {
            Glide.with(requireContext())
                    .load(avers)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.aversImage);

            Glide.with(requireContext())
                    .load(revers)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.reversImage);
        }

        binding.number1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.number2.requestFocus();
                    binding.number1.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.black)));
                } else
                    binding.number1.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });

        binding.number2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.number3.requestFocus();
                    binding.number2.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.black)));
                } else {
                    binding.number1.requestFocus();
                    binding.number2.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });

        binding.number3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.number4.requestFocus();
                    binding.number3.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.black)));
                } else {
                    binding.number2.requestFocus();
                    binding.number3.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });

        binding.number4.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.number4.clearFocus();
                    binding.number4.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.black)));
                } else {
                    binding.number3.requestFocus();
                    binding.number4.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });

        binding.number4.setOnFocusChangeListener((view1, focused) -> {
            InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!focused && !binding.number3.isFocused())
                keyboard.hideSoftInputFromWindow(binding.number4.getWindowToken(), 0);
        });

        binding.search.setOnClickListener(v -> {
            StringBuilder builder = new StringBuilder();
            builder.append(binding.number1.getText().toString());
            builder.append(binding.number2.getText().toString());
            builder.append(binding.number3.getText().toString());
            builder.append(binding.number4.getText().toString());

            if (builder.toString().length() < 4) {
                if (binding.number1.getText().toString().length() < 1)
                    binding.number1.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));

                if (binding.number2.getText().toString().length() < 1)
                    binding.number2.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));

                if (binding.number3.getText().toString().length() < 1)
                    binding.number3.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));

                if (binding.number4.getText().toString().length() < 1)
                    binding.number4.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
            } else {
                if (Integer.parseInt(builder.toString()) >= 1997 && Integer.parseInt(builder.toString()) <= 2020) {
                    Bundle bundle = new Bundle();
                    bundle.putString("year", builder.toString());

                    NavHostFragment.findNavController(this).navigate(R.id.action_cameraCompleteFragment_to_searchingFragment, bundle);
                } else {
                    binding.number1.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
                    binding.number2.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
                    binding.number3.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
                    binding.number4.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.red)));
                }
            }
        });

        binding.avers.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("state", "avers");

            NavHostFragment.findNavController(this).navigate(R.id.cameraFragment, bundle);
        });

        binding.revers.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("state", "revers");

            NavHostFragment.findNavController(this).navigate(R.id.cameraFragment, bundle);
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
