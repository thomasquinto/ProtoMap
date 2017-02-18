package com.life360.android.protomap;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private LatLng startLocation = new LatLng(59.145833, 10.223611);
    private int mapHeight;

    private static final int SLIDE_PANEL_RESTING_HEIGHT_DP = 48;
    private static final float SLIDE_PANEL_ANCHOR_POINT_PERCENTAGE = 0.25f;
    private static final int SLIDE_PANEL_PARALLAX_OFFSET = 500;
    private static final int STATUS_BAR_COLOR = R.color.primary_main_grape_500;

    @BindView(R.id.slide_panel)
    SlidingUpPanelLayout slidePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        ButterKnife.bind(this);

        setupMapFragment();
        setupSlidePanel();
    }

    private void setupMapFragment() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Will asynchronously invoke onMapReady
        mapFragment.getMapAsync(this);

        final ViewTreeObserver viewTreeObserver = mapFragment.getView().getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mapFragment.getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mapHeight = mapFragment.getView().getHeight() - SLIDE_PANEL_PARALLAX_OFFSET;
                    System.out.println("MAP HEIGHT: " + mapHeight);
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(startLocation).title("Start"));
        map.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
    }

    private void setupSlidePanel() {
        slidePanel.setPanelHeight(dpToPixels(SLIDE_PANEL_RESTING_HEIGHT_DP));
        slidePanel.setAnchorPoint(SLIDE_PANEL_ANCHOR_POINT_PERCENTAGE);
        slidePanel.setParallaxOffset(SLIDE_PANEL_PARALLAX_OFFSET);
        //slidePanel.setOverlayed(true);
        slidePanel.setCoveredFadeColor(android.R.color.transparent); // disable scroll fading

        setStatusBarTransparent();
        final AtomicBoolean statusBarState = new AtomicBoolean(false);
        slidePanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                System.out.println("SLIDE OFFSET: " + slideOffset);
                if (slideOffset == 1.0f) {
                    if (statusBarState.get()) {
                        statusBarState.set(false);
                        setStatusBarColor();
                    }
                } else {
                    if (!statusBarState.get()) {
                        statusBarState.set(true);
                        setStatusBarTransparent();
                    }
                    int paddingBottom = (int) (slideOffset * mapHeight);
                    map.setPadding(0, 0, 0, paddingBottom);
                    System.out.println("PADDING BOTTOM: " + paddingBottom);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });

        //TODO: Set slidePanel top margin programmatically to Status Bar height instead of hard-coded "24dp" in layout

        /* To Turn off transparent status bar effect:
         * 1) Set panel layout_marginTop to 0dp in map_activity.xml layout resource file
         * 2) Remove status bar color changes above
         */
    }

    private void setStatusBarTransparent() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void setStatusBarColor() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //TODO: Requires API Level 21
        window.setStatusBarColor(ContextCompat.getColor(this, STATUS_BAR_COLOR));
    }

    private int dpToPixels(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

}
