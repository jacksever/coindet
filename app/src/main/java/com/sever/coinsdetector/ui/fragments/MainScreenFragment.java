package com.sever.coinsdetector.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.sever.coinsdetector.R;
import com.sever.coinsdetector.databinding.MainScreenFragmentBinding;
import com.sever.coinsdetector.ui.adapters.MainScreenAdapter;
import com.sever.coinsdetector.ui.adapters.SpacesItemDecoration;
import com.sever.coinsdetector.ui.viewmodels.CoinCollectionsViewModel;

public class MainScreenFragment extends Fragment {
    private MainScreenFragmentBinding binding;
    private SpacesItemDecoration decoration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = MainScreenFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        decoration = new SpacesItemDecoration(
                (int) getResources().getDimension(R.dimen.spacing_baseline),
                (int) getResources().getDimension(R.dimen.spacing_bottom),
                0);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        binding.list.invalidateItemDecorations();
        binding.list.addItemDecoration(decoration);
        binding.list.setLayoutManager(layoutManager);
        binding.list.setClipToPadding(false);
        binding.list.setItemViewCacheSize(100);

        CoinCollectionsViewModel viewModel = new ViewModelProvider(this).get(CoinCollectionsViewModel.class);
        viewModel.getCollectionSorting(requireContext()).observe(getViewLifecycleOwner(), items -> {
            MainScreenAdapter adapter = new MainScreenAdapter(items);
            decoration.setElementSize(items.size());
            binding.list.setAdapter(adapter);
        });

        binding.camera.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_mainScreenFragment_to_cameraFragment));

        binding.settings.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_mainScreenFragment_to_settingsFragment));

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding.list.invalidateItemDecorations();
        binding = null;
        decoration = null;
        super.onDestroyView();
    }
}
