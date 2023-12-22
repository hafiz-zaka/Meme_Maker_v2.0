package com.mememaker.mememakerpro.creatememe.model;

public class Font {

    public Font(int colorImage, String name) {
        this.colorImage = colorImage;
        this.name = name;
    }

    public int getColorImage() {
        return colorImage;
    }

    public void setColorImage(int colorImage) {
        this.colorImage = colorImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int colorImage;
    String name;


}
