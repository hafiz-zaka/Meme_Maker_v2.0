package com.mememaker.mememakerpro.creatememe.model;
/*
 *   Developer Name : Rafaqat Mehmood
 *   Whatsapp Number : 0310-1025532
 *   Designation : Sr.Android Developer
 */
public class ImageModel {
    public ImageModel(String title, String path, long size) {
        this.title = title;
        this.path = path;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    private String title,path;


    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    private double size;
}
