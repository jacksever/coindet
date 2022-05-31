package com.sever.coinsdetector.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sever.coinsdetector.R;
import com.sever.coinsdetector.databinding.SectionRowBinding;
import com.sever.coinsdetector.entities.CoinCollection;
import com.sever.coinsdetector.ui.fragments.SectionFragmentDirections;

import java.util.List;

public class SectionListAdapter extends RecyclerView.Adapter<SectionListAdapter.SectionListViewHolder> {
    private final List<CoinCollection> models;
    private final boolean isBottomSheet;

    public SectionListAdapter(@NonNull List<CoinCollection> models, boolean isBottomSheet) {
        this.models = models;
        this.isBottomSheet = isBottomSheet;
    }

    @NonNull
    @Override
    public SectionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SectionListViewHolder(SectionRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SectionListViewHolder holder, int position) {
        holder.bind(models.get(position), isBottomSheet);

        holder.binding.getRoot().setOnClickListener(v -> {
            SectionFragmentDirections.ActionSectionFragmentToAboutCoinFragment action =
                    SectionFragmentDirections.actionSectionFragmentToAboutCoinFragment(models.get(position).getName(), models.get(position).getYears());

            if (isBottomSheet)
                Navigation.findNavController(holder.binding.getRoot()).popBackStack();

            Navigation.findNavController(holder.binding.getRoot()).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    protected static class SectionListViewHolder extends RecyclerView.ViewHolder {
        private final SectionRowBinding binding;

        public SectionListViewHolder(@NonNull SectionRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull CoinCollection item, boolean isBottomSheet) {
            binding.name.setText(item.getName());
            binding.years.setText(binding.getRoot().getContext().getString(R.string.info_year, item.getYears()));

            if (isBottomSheet) {
                binding.arrow.setColorFilter(binding.getRoot().getContext().getColor(R.color.white));
                binding.name.setTextColor(binding.getRoot().getContext().getColor(R.color.white));
                binding.years.setTextColor(binding.getRoot().getContext().getColor(R.color.grey_light));
            }

            Glide.with(binding.getRoot().getContext())
                    .load(item.getUrlAvers())
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.image);
        }
    }
}
