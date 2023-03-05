package com.example.mavydroid3;

import android.net.Uri;

public class Horoscope {
    private int resource;
    private String uriString;
    private String name;

    public Horoscope(int resource, String name) {
        this.resource = resource;
        this.name = name;
    }

    public Horoscope(Uri uri, String name) {
        this.uriString = uri.toString();
        this.name = name;
    }

    public int getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }

    public String getUriString() {
        return uriString;
    }

    public boolean hasUriAvailable() {
        return uriString != null;
    }
}
