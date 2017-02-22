package com.life360.android.protomap.model;

/**
 * Created by thomas on 2/21/17.
 */

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public abstract class Locatable  {

    private String name;
    private String address;
    private LatLng latLng;
    private long lastUpdated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public abstract Bitmap getIcon(Context context);

    public static List<Marker> addMarkersToMap(Context context, List<? extends Locatable> locatables, GoogleMap map) {
        ArrayList<Marker> markers = new ArrayList<>();

        for (Locatable locatable : locatables) {
            markers.add(map.addMarker(new MarkerOptions().position(locatable.getLatLng()).icon(
                    BitmapDescriptorFactory.fromBitmap(locatable.getIcon(context)))));
        }

        return markers;
    }
}
