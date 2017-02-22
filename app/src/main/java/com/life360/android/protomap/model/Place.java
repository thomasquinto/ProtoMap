package com.life360.android.protomap.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;
import com.life360.android.protomap.R;
import com.life360.android.protomap.util.LocationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 2/21/17.
 */

public class Place extends Locatable {

    private int iconResId;

    public Place(String name, LatLng latLng, int iconResId) {
        setName(name);
        setLatLng(latLng);
        this.iconResId = iconResId;
    }

    public Bitmap getIcon(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), iconResId);
    }

    public static List<Place> generateDummyData(LatLng latLng, int radius) {
        ArrayList<Place> places = new ArrayList<>();

        places.add(new Place("Default", LocationUtils.getRandomLocation(latLng, radius), R.drawable.places_mapicon_default));
        places.add(new Place("Home", LocationUtils.getRandomLocation(latLng, radius), R.drawable.places_mapicon_home));
        places.add(new Place("Park", LocationUtils.getRandomLocation(latLng, radius), R.drawable.places_mapicon_park));
        places.add(new Place("School", LocationUtils.getRandomLocation(latLng, radius), R.drawable.places_mapicon_school));
        places.add(new Place("Shop", LocationUtils.getRandomLocation(latLng, radius), R.drawable.places_mapicon_shop));
        places.add(new Place("Sport", LocationUtils.getRandomLocation(latLng, radius), R.drawable.places_mapicon_sport));
        places.add(new Place("Work", LocationUtils.getRandomLocation(latLng, radius), R.drawable.places_mapicon_work));

        return places;
    }
}
