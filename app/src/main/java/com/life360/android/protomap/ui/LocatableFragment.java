package com.life360.android.protomap.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.life360.android.protomap.R;
import com.life360.android.protomap.model.Locatable;
import com.life360.android.protomap.model.Member;
import com.life360.android.protomap.model.Place;

/**
 * Created by thomas on 2/28/17.
 */

public class LocatableFragment extends Fragment {

    private Locatable locatable;

    public static LocatableFragment newInstance(Locatable locatable) {
        LocatableFragment fragment = new LocatableFragment();
        fragment.locatable = locatable;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = container.getContext();

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_locatable, container, false);
        viewGroup.setBackgroundColor(Color.parseColor("#FAFAFA"));//ContextCompat.getColor(container.getContext(), R.color.main_blueberry_500));

        View view;
        if (locatable instanceof Member) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_member, viewGroup, false);
            viewGroup.addView(view);

            ((TextView) view.findViewById(R.id.member_name)).setText(locatable.getName());
            ((ImageView) view.findViewById(R.id.member_icon)).setImageBitmap(locatable.getIcon(context));

        } else if (locatable instanceof Place) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_place, viewGroup, false);
            viewGroup.addView(view);

            ((TextView) view.findViewById(R.id.place_name)).setText(locatable.getName());
            ((ImageView) view.findViewById(R.id.place_icon)).setImageBitmap(locatable.getIcon(context));
        }

        return viewGroup;
    }
}
