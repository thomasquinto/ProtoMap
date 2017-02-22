package com.life360.android.protomap.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;

import com.life360.android.protomap.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * Created by thomas on 2/19/17.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    public static final int TAB_COLOR_RES_ID = R.color.neutral_400;
    public static final int TAB_SELECTED_COLOR_RES_ID = R.color.primary_main_grape_500;
    public static final int TAB_ICON_FONT_SIZE_DP = 28;
    public static final int TAB_ICON_SELECTED_FONT_SIZE_DP = 32;
    public static final float TAB_UNSELECTED_FONT_SIZE_RATIO = 0.9f;
    public static final boolean NO_TITLE = true;

    public static String tabTitles[] = new String[] { "People", "Places", "Safety", "Settings" };

    public static GoogleMaterial.Icon[] tabIcons = {
            GoogleMaterial.Icon.gmd_account_circle,
            GoogleMaterial.Icon.gmd_place,
            GoogleMaterial.Icon.gmd_warning,
            GoogleMaterial.Icon.gmd_settings
    };

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

    private static Drawable getIconDrawable(Context context, int position, boolean selected) {
        int fontSizeDp = selected ? TAB_ICON_SELECTED_FONT_SIZE_DP : TAB_ICON_FONT_SIZE_DP;

        Drawable image = new IconicsDrawable(context)
                .icon(tabIcons[position])
                .color(getColor(context, selected))
                .sizeDp(fontSizeDp);

        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        return image;
    }

    private static int getColor(Context context, boolean selected) {
        int color = selected ? TAB_SELECTED_COLOR_RES_ID : TAB_COLOR_RES_ID;
        return ContextCompat.getColor(context, color);
    }


    public static CharSequence getTabText(Context context, int position, boolean selected) {
        // Get drawable image from font
        Drawable image = getIconDrawable(context, position, selected);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());

        // Replace blank spaces with image icon
        SpannableString span;
        if (NO_TITLE) {
            span = new SpannableString(" ");
        } else {
            span = new SpannableString(" \n" + tabTitles[position]);
        }

        // Image Span
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        span.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (NO_TITLE) {
            return span;
        }

        // Color Span
        int color = getColor(context, selected);
        span.setSpan(new ForegroundColorSpan(color), 1, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // If unselected, decrease size of font size by TAB_UNSELECTED_FONT_SIZE_RATIO
        if (!selected) {
            span.setSpan(new RelativeSizeSpan(TAB_UNSELECTED_FONT_SIZE_RATIO), 1, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return span;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTabText(context, position, position == 0);
    }

}
