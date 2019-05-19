package com.example.rauan1.mystat;

import android.graphics.drawable.Drawable;

public class ElementData {
    String name;
    int UsingTime;
    Drawable icon;
    public ElementData(String name, int usingTime,Drawable icon) {
        this.name = name;
        UsingTime = usingTime;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public int getUsingTime() {
        return UsingTime;
    }
}
