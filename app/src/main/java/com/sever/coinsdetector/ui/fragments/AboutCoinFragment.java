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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sever.coinsdetector.App;
import com.sever.coinsdetector.R;
import com.sever.coinsdetector.database.Coin;
import com.sever.coinsdetector.databinding.AboutCoinFragmentBinding;
import com.sever.coinsdetector.ui.adapters.SectionListAdapter;
import com.sever.coinsdetector.ui.viewmodels.CoinCollectionsViewModel;

public class AboutCoinFragment extends Fragment {
    private AboutCoinFragmentBinding binding;
    private AboutCoinFragmentArgs data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = AboutCoinFragmentArgs.fromBundle(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = AboutCoinFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.header.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        Coin coin = App.getInstance().getDatabase().coinDao().getByNameAndYear(data.getName(), data.getYear());
        if (coin != null) {
            Glide.with(binding.getRoot().getContext())
                    .load(coin.urlAvers)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.avers);

            Glide.with(binding.getRoot().getContext())
                    .load(coin.urlRevers)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.revers);

            binding.name.setText(requireContext().getString(R.string.info_name, data.getName(), data.getYear()));
            binding.year.setSubtitle(coin.year);
            binding.country.setSubtitle(coin.country);
            binding.diametersAndThickness.setSubtitle(requireContext().getString(R.string.info_diameter, coin.diameter, coin.thickness));
            binding.material.setSubtitle(coin.material);
            binding.edge.setSubtitle(coin.edge);
            binding.weight.setSubtitle(requireContext().getString(R.string.info_weight, coin.weight));
            setPrice(coin);
        }

        CoinCollectionsViewModel viewModel = new ViewModelProvider(this).get(CoinCollectionsViewModel.class);
        viewModel.getCollectionByName(data.getName()).observe(getViewLifecycleOwner(), items -> {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.recyclerView.setAdapter(new SectionListAdapter(items, true));
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(AboutCoinFragment.this).popBackStack();
            }
        });
    }

    private void setPrice(@NonNull Coin coin) {
        if (coin.priceMsk != 0 && coin.priceSpb != 0) {
            binding.price.setSubtitle(coin.priceMsk + " / " + coin.priceSpb + " руб.");
            return;
        }

        if (coin.priceMsk == 0 && coin.priceSpb != 0) {
            binding.price.setSubtitle("Номинал" + " / " + coin.priceSpb + " руб.");
            return;
        }

        if (coin.priceMsk != 0 && coin.priceSpb == 0) {
            binding.price.setSubtitle(coin.priceMsk + " руб. / " + "Номинал");
            return;
        }

        binding.price.setSubtitle("Номинал / Номинал");
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
