package com.jpac.hackernews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

public class RobotoTextView extends TextView {

    protected static final String XML_NAMESPACE = "http://schemas.android.com/apk/res/android";

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            int[] attribs = new int[]{android.R.attr.textStyle, android.R.attr.fontFamily};

            TypedArray typedArray = context.obtainStyledAttributes(attrs, attribs);

            String textStyle = typedArray.getString(0);
            String fontFamily = typedArray.getString(1);

            typedArray.recycle();

            setRobotoTypeface(textStyle, fontFamily);
        }

    }

    private void setRobotoTypeface(String textStyle, String fontFamily) {
        textStyle = (textStyle == null) ? "normal" : textStyle;
        fontFamily = (fontFamily == null) ? "sans-serif" : fontFamily;

        // TODO: build typeface filename to load
    }
}
