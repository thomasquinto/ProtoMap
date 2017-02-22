package com.life360.android.protomap.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.life360.android.protomap.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thomas on 2/21/17.
 */

public class VerticalCardsFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Context context;
    private RecyclerView.Adapter adapter;

    public VerticalCardsFragment() {}

    public VerticalCardsFragment(Context context, RecyclerView.Adapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vertical_card_fragment, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);
        System.out.println("B");

        return view;
    }

}
