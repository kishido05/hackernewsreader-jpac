package com.jpac.hackernews.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class FontUtils {

    protected Context context;
    protected Map<String, Typeface> fontCache;

    private static FontUtils fontUtils;

    private FontUtils(Context context) {
        this.context = context;
        fontCache = new HashMap<String, Typeface>();
    }

    public static void init(Context context) {
        if (fontUtils == null) {
            fontUtils = new FontUtils(context);
        } else {
            fontUtils.context = context;
        }
    }

    public Typeface load(String path) {
        if (fontCache.containsKey(path)) {
            return fontCache.get(path);
        }

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);
        fontCache.put(path, typeface);

        return typeface;
    }

    public static Typeface open(String path) {
        return fontUtils.load(path);
    }
}
