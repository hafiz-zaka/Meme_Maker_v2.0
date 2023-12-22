package com.mememaker.mememakerpro.creatememe;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFont {
    public static final int acme =   0;
    public static final int akaya_telivigala =   1;
    public static final int aldrich =   2;
    public static final int bungee_shade =   3;
    public static final int creepster =   4;
    public static final int faster_one =   5;
    public static final int homemade_apple =   6;
    public static final int monoton =   7;

    private static final int NUM_OF_CUSTOM_FONTS = 7;

    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[3];

    private static String[] fontPath = {
            "font/acme.ttf",
            "font/akaya_telivigala.ttf",
            "font/aldrich.ttf",
            "font/bungee_shade.ttf",
            "font/creepster.ttf",
            "font/faster_one.ttf",
            "font/homemade_apple.ttf",
            "font/monoton.ttf"
    };
    public static Typeface getTypeface(Context context, int fontIdentifier) {
        if (!fontsLoaded) {
            loadFonts(context);
        }
        return fonts[fontIdentifier];
    }


    private static void loadFonts(Context context) {
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        fontsLoaded = true;

    }
}
