package com.life360.android.protomap.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.life360.android.protomap.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thomas on 4/30/17.
 */

public class BottomBarActivity extends FragmentActivity {

    public static void start(Context c) {
        c.startActivity(new Intent(c, BottomBarActivity.class));
    }

    BottomBar bottomBar;

    View tab1;
    @BindView(R.id.tab2)
    View tab2;
    @BindView(R.id.tab3)
    View tab3;
    @BindView(R.id.tab4)
    View tab4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        ButterKnife.bind(this);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switchTab(tabId);
            }
        });

        // If you want to listen for reselection events, here's how you do it:
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switchTab(tabId);
            }
        });

        setupMapFragment();
    }

    private void switchTab(int tabId) {
        if (tab1 != null) tab1.setVisibility(View.GONE);
        tab2.setVisibility(View.GONE);
        tab3.setVisibility(View.GONE);
        tab4.setVisibility(View.GONE);


        switch (tabId) {
            case R.id.tab_people:
                if (tab1 != null) tab1.setVisibility(View.VISIBLE);
                break;
            case R.id.tab_places:
                tab2.setVisibility(View.VISIBLE);
                break;
            case R.id.tab_safety:
                tab3.setVisibility(View.VISIBLE);
                break;
            case R.id.tab_profile:
                tab4.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setupMapFragment() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Will asynchronously invoke onMapReady
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                tab1 = mapFragment.getView();

                googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int i) {
                        bottomBar.setVisibility(View.GONE);
                    }
                });

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        bottomBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
}