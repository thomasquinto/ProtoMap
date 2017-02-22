package com.life360.android.protomap.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;
import com.life360.android.protomap.R;
import com.life360.android.protomap.util.LocationUtils;
import com.life360.android.protomap.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 2/21/17.
 */

public class Member extends Locatable {

    public static final int ICON_SIZE_DP = 55;

    private int iconResId;
    public Member(String name, LatLng latLng, int iconResId) {
        setName(name);
        setLatLng(latLng);
        this.iconResId = iconResId;
    }

    public Bitmap getMapIcon(Context context) {
        return UiUtils.getCircularBitmap(BitmapFactory.decodeResource(context.getResources(), iconResId), UiUtils.dpToPixels(context, ICON_SIZE_DP));
    }

    public static List<Locatable> generateDummyData(LatLng latLng, int radius) {
        ArrayList<Locatable> members = new ArrayList<>();

        members.add(new Member("Aaron", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_aaron));
        members.add(new Member("Krystle", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_krystle));
        members.add(new Member("Ryan", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_ryan));
        members.add(new Member("Walrus", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_walrus));
        members.add(new Member("Thomas", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_thomas));
        members.add(new Member("Menka", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_menka));
        members.add(new Member("Viggo", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_viggo));
        members.add(new Member("Mrs. Smith", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_lady));
        members.add(new Member("Luis", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_mustache));
        members.add(new Member("Chewbacca", LocationUtils.getRandomLocation(latLng, radius), R.drawable.member_wooly));

        return members;
    }
}
