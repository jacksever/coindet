package com.sever.coinsdetector.ui.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sever.coinsdetector.App;
import com.sever.coinsdetector.R;
import com.sever.coinsdetector.database.Coin;
import com.sever.coinsdetector.entities.CoinCollection;

import java.util.ArrayList;
import java.util.List;

public class CoinCollectionsViewModel extends ViewModel {
    private MutableLiveData<List<CoinCollection>> mutableLiveData;

    @NonNull
    public MutableLiveData<List<CoinCollection>> getCollectionSorting(@NonNull Context context) {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();

        getData(context);
        return mutableLiveData;
    }

    @NonNull
    public MutableLiveData<List<CoinCollection>> getCollectionByName(@NonNull String name) {
        if (mutableLiveData == null)
            mutableLiveData = new MutableLiveData<>();

        getDataByName(name);
        return mutableLiveData;
    }

    private void getData(@NonNull Context context) {
        List<CoinCollection> items = new ArrayList<>();
        items.add(new CoinCollection(context.getString(R.string.kop_1), context.getString(R.string.kop_1_years), R.drawable.kop_1, null, null));
        items.add(new CoinCollection(context.getString(R.string.kop_5), context.getString(R.string.kop_5_years), R.drawable.kop_5, null, null));
        items.add(new CoinCollection(context.getString(R.string.kop_10), context.getString(R.string.kop_10_years), R.drawable.kop_10, null, null));
        items.add(new CoinCollection(context.getString(R.string.kop_50), context.getString(R.string.kop_50_years), R.drawable.kop_50, null, null));
        items.add(new CoinCollection(context.getString(R.string.rub_1), context.getString(R.string.rub_1_years), R.drawable.rub_1, null, null));
        items.add(new CoinCollection(context.getString(R.string.rub_2), context.getString(R.string.rub_2_years), R.drawable.rub_2, null, null));
        items.add(new CoinCollection(context.getString(R.string.rub_3), context.getString(R.string.rub_3_years), R.drawable.rub_3, null, null));
        items.add(new CoinCollection(context.getString(R.string.rub_5), context.getString(R.string.rub_5_years), R.drawable.rub_5, null, null));
        items.add(new CoinCollection(context.getString(R.string.rub_10), context.getString(R.string.rub_10_years), R.drawable.rub_10, null, null));
        items.add(new CoinCollection(context.getString(R.string.rub_25), context.getString(R.string.rub_25_years), R.drawable.rub_25, null, null));

        mutableLiveData.postValue(items);
    }

    private void getDataByName(@NonNull String name) {
        List<Coin> resultDb = App.getInstance().getDatabase().coinDao().getByName(name);
        List<CoinCollection> coins = new ArrayList<>();

        if (resultDb == null)
            return;

        resultDb.forEach(item -> coins.add(new CoinCollection(item.name, item.year, -1, item.urlAvers, item.urlRevers)));
        mutableLiveData.postValue(coins);
    }
}
