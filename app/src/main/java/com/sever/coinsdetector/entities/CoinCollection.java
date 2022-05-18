package com.sever.coinsdetector.entities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CoinCollection {
    @DrawableRes
    private final int image;
    private final String name, years;
    private final String urlAvers, urlRevers;

    public CoinCollection(@NonNull String name, @NonNull String years, @DrawableRes int image, @Nullable String urlAvers, @Nullable String urlRevers) {
        this.name = name;
        this.years = years;
        this.image = image;
        this.urlAvers = urlAvers;
        this.urlRevers = urlRevers;
    }

    public int getImage() {
        return image;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getYears() {
        return years;
    }

    @Nullable
    public String getUrlAvers() {
        return urlAvers;
    }

    @Nullable
    public String getUrlRevers() {
        return urlRevers;
    }
}
