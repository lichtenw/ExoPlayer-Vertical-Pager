package com.lichtenw.android.exoplayer.model;


public class Video {

    public String description;
    public String thumb;
    public String title;
    public String subtitle;
    public String sources[];

    @Override
    public String toString() {
        return title;
    }
}
