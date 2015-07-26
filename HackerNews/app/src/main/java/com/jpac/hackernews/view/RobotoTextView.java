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

        int fontStyle = attrs.getAttributeIntValue("http://schemas.android.com/apk/" + getContext().getPackageName(), "fontStyle", 0);
        int textStyle = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textStyle", 0);

        Typeface tf = FontUtils.open(ROBOTO_FONT[fontStyle]);
        setTypeface(tf, textStyle);
    }
}
