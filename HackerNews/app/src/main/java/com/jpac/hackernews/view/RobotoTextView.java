package com.jpac.hackernews.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jpac.hackernews.utils.FontUtils;

public class RobotoTextView extends TextView {

    protected static final String[] ROBOTO_FONT = new String[] {
        "Roboto-Regular.ttf",
        "Roboto-Medium.ttf",
        "Roboto-Thin.ttf",
        "Roboto-Light.ttf",
        "Roboto-Italic.ttf",
        "Roboto-MediumItalic.ttf",
        "Roboto-ThinItalic.ttf",
        "Roboto-LightItalic.ttf"
    };

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int style = attrs.getAttributeIntValue("http://schemas.android.com/apk/res-auto", "fontStyle", 0);

        Typeface tf = FontUtils.open(ROBOTO_FONT[style]);
        setTypeface(tf);
    }
}
