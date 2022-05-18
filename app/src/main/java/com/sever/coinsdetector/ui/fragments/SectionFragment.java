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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sever.coinsdetector.databinding.SectionFragmentBinding;
import com.sever.coinsdetector.ui.adapters.SectionListAdapter;
import com.sever.coinsdetector.ui.viewmodels.CoinCollectionsViewModel;

public class SectionFragment extends Fragment {
    private SectionFragmentBinding binding;
    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            this.title = getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = SectionFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.header.setText(title);
        binding.header.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        CoinCollectionsViewModel viewModel = new ViewModelProvider(this).get(CoinCollectionsViewModel.class);
        viewModel.getCollectionByName(title).observe(getViewLifecycleOwner(), items -> {
            SectionListAdapter adapter = new SectionListAdapter(items, false);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setClipToPadding(false);
            binding.recyclerView.setItemViewCacheSize(100);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(SectionFragment.this).popBackStack();
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
