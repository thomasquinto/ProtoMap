package com.life360.android.protomap.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.life360.android.protomap.model.Locatable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 2/28/17.
 */

public class LocatablePagerAdapter extends FragmentPagerAdapter {

    private Map<Integer,Fragment> fragmentMap = new HashMap<>();
    private List<? extends Locatable> locatables;

    public LocatablePagerAdapter(FragmentManager fragmentManager, List<? extends Locatable> locatables) {
        super(fragmentManager);
        this.locatables = locatables;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentMap.get(position);
        if (fragment == null) {
            fragment = LocatableFragment.newInstance(locatables.get(position));
            fragmentMap.put(position, fragment);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return locatables.size();
    }
}
