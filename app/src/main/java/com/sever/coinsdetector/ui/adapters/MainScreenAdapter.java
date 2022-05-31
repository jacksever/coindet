package com.sever.coinsdetector.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sever.coinsdetector.databinding.MainScreenRowBinding;
import com.sever.coinsdetector.entities.CoinCollection;
import com.sever.coinsdetector.ui.fragments.MainScreenFragmentDirections;

import java.util.List;

public class MainScreenAdapter extends RecyclerView.Adapter<MainScreenAdapter.MainScreenViewHolder> {
    private final List<CoinCollection> models;

    public MainScreenAdapter(@NonNull List<CoinCollection> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public MainScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainScreenViewHolder(MainScreenRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainScreenViewHolder holder, int position) {
        holder.bind(models.get(position));

        holder.binding.getRoot().setOnClickListener(v -> {
            MainScreenFragmentDirections.ActionMainScreenFragmentToSectionFragment action =
                    MainScreenFragmentDirections.actionMainScreenFragmentToSectionFragment(models.get(position).getName());

            Navigation.findNavController(holder.itemView).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    protected static class MainScreenViewHolder extends RecyclerView.ViewHolder {
        private final MainScreenRowBinding binding;

        public MainScreenViewHolder(@NonNull MainScreenRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull CoinCollection item) {
            binding.name.setText(item.getName());
            binding.years.setText(item.getYears());

            Glide.with(binding.getRoot().getContext())
                    .load(item.getImage())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.image);
        }
    }
}
