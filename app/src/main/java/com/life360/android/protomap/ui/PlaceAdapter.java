package com.life360.android.protomap.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.life360.android.protomap.R;
import com.life360.android.protomap.model.Place;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thomas on 2/21/17.
 */

public class PlaceAdapter extends RecyclerView.Adapter {

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        View view;

        @BindView(R.id.place_icon)
        ImageView placeIcon;
        @BindView(R.id.place_name)
        TextView placeName;
        @BindView(R.id.place_address)
        TextView placeAddress;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    private Context context;
    private List<Place> places;
    public PlaceAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_place, viewGroup, false);
        PlaceViewHolder viewHolder = new PlaceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Place place = places.get(i);

        PlaceViewHolder placeViewHolder = (PlaceViewHolder) viewHolder;
        placeViewHolder.placeName.setText(place.getName());
        placeViewHolder.placeAddress.setText(place.getAddress());
        placeViewHolder.placeIcon.setImageBitmap(place.getIcon((context)));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
