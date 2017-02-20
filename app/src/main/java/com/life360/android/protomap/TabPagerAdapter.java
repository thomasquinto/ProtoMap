package com.life360.android.protomap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by thomas on 2/19/17.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "People", "Places", "Safety", "Settings" };
    private Context context;

    public TabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        return TabPageFragment.newInstance(position + 1);
    }

    public static GoogleMaterial.Icon[] tabIcons = {
            GoogleMaterial.Icon.gmd_account_circle,
            GoogleMaterial.Icon.gmd_place,
            GoogleMaterial.Icon.gmd_warning,
            GoogleMaterial.Icon.gmd_settings
    };

    @Override
    public CharSequence getPageTitle(int position) {

        Drawable image = new IconicsDrawable(context)
                .icon(tabIcons[position])
                .color(ContextCompat.getColor(context, R.color.primary_main_grape_500))
                .sizeDp(24);

        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString(" \n" + tabTitles[position] );
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

}
