package com.example.letsquiz;

import android.text.Html;
import android.text.Spanned;

public class Utils {

    protected static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
    protected static Spanned htmlFormat(String string){

        return Html.fromHtml(string);

    }
}
