package com.life360.android.protomap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by thomas on 2/19/17.
 */

public class TabPageFragment extends Fragment {
    public static final String ARG_TAB = "ARG_PAGE";

    private int mPage;

    public static TabPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_TAB, page);
        TabPageFragment fragment = new TabPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_TAB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_page_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText("Fragment #" + mPage);
        return view;
    }
}