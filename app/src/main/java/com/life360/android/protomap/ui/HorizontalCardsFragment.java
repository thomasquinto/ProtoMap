package com.life360.android.protomap.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.life360.android.protomap.R;
import com.life360.android.protomap.model.Locatable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thomas on 2/28/17.
 */

public class HorizontalCardsFragment extends Fragment {

    @BindView(R.id.locatable_pager)
    ViewPager locatablePager;

    private Context context;
    private List<? extends Locatable> locatables;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public HorizontalCardsFragment() {}

    public HorizontalCardsFragment(Context context, List<? extends Locatable> locatables, ViewPager.OnPageChangeListener onPageChangeListener) {
        this.context = context;
        this.locatables = locatables;
        this.onPageChangeListener = onPageChangeListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horizontal_cards, container, false);
        ButterKnife.bind(this, view);

        locatablePager.setAdapter(new LocatablePagerAdapter(getChildFragmentManager(), locatables));
        if (onPageChangeListener != null) {
            System.out.println("SETTING PAGE LISTENER");
            locatablePager.addOnPageChangeListener(onPageChangeListener);
        }

        return view;
    }

}
