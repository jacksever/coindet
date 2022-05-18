package com.sever.coinsdetector.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import androidx.annotation.NonNull;

import java.util.Hashtable;

public class Utils {
    private static final Hashtable<String, Typeface> cacheFonts = new Hashtable<>();

    public static Bitmap cropBitmap(@NonNull Bitmap source) {
        return Bitmap.createBitmap(source, source.getWidth() / 2 - 600, source.getHeight() / 2 - 600, 1200, 1200);
    }

    /**
     * This method returns the desired font from the <b>Hashtable</b>
     *
     * @param context - non null
     * @param name    - non null
     * @return <b>Typeface</b>
     */
    public static Typeface getFont(@NonNull Context context, @NonNull String name) {
        synchronized (cacheFonts) {
            if (!cacheFonts.containsKey(name)) {
                String path = "fonts/" + name;
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), path);
                    cacheFonts.put(name, t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return cacheFonts.get(name);
        }
    }
}
