package com.jpac.hackernews.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class FontUtils {

    protected Map<String, Typeface> fontCache;

    private static FontUtils fontUtils = new FontUtils();

    private FontUtils() {
        fontCache = new HashMap<String, Typeface>();
    }

    public Typeface load(Context context, String path) {
        if (fontCache.containsKey(path)) {
            return fontCache.get(path);
        }

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);
        fontCache.put(path, typeface);

        return typeface;
    }

    public static Typeface open(Context context, String path) {
        return fontUtils.load(context, path);
    }
}
