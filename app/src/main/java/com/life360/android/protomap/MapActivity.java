package com.life360.android.protomap;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final LatLng MAP_INIT_LOCATION = new LatLng(59.145833, 10.223611);
    private static final int MAP_INIT_ZOOM_LEVEL = 10;
    private GoogleMap map;
    private int mapHeight;

    private static final int SLIDE_PANEL_RESTING_HEIGHT_DP = 48;
    private static final float SLIDE_PANEL_ANCHOR_POINT_PERCENTAGE = 0.25f;
    private static final int SLIDE_PANEL_PARALLAX_OFFSET = 500;
    private static final int STATUS_BAR_COLOR = R.color.primary_main_grape_500;

    @BindView(R.id.slide_panel)
    SlidingUpPanelLayout slidePanel;
    @BindView(R.id.tab_view_pager)
    ViewPager tabViewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        ButterKnife.bind(this);

        setupMapFragment();
        setupSlidePanel();
        setupTabs();
    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Will asynchronously invoke onMapReady
        mapFragment.getMapAsync(this);

        final View view = mapFragment.getView();
        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mapHeight = view.getHeight() - SLIDE_PANEL_PARALLAX_OFFSET;
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(MAP_INIT_LOCATION));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MAP_INIT_LOCATION, MAP_INIT_ZOOM_LEVEL));
    }

    private void setupSlidePanel() {
        slidePanel.setPanelHeight(dpToPixels(SLIDE_PANEL_RESTING_HEIGHT_DP));
        slidePanel.setAnchorPoint(SLIDE_PANEL_ANCHOR_POINT_PERCENTAGE);
        slidePanel.setParallaxOffset(SLIDE_PANEL_PARALLAX_OFFSET);
        //slidePanel.setOverlayed(true);
        slidePanel.setCoveredFadeColor(android.R.color.transparent); // disable scroll fading

        //setStatusBarTransparent();
        //final AtomicBoolean slideToggle = new AtomicBoolean(false);
        slidePanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                /*
                if (slideOffset == 1.0f) {
                    if (slideToggle.get()) {
                        slideToggle.set(false);
                        setStatusBarColor();
                    }
                } else {
                    if (!slideToggle.get()) {
                        slideToggle.set(true);
                        setStatusBarTransparent();
                    }
                }
                */

                if (slideOffset <= SLIDE_PANEL_ANCHOR_POINT_PERCENTAGE) {
                    int paddingBottom = (int) (slideOffset * mapHeight);
                    map.setPadding(0, 0, 0, paddingBottom);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
    }

    private void setStatusBarTransparent() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    private void setStatusBarColor() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //TODO: Requires API Level 21
        getWindow().setStatusBarColor(ContextCompat.getColor(this, STATUS_BAR_COLOR));
    }

    private int dpToPixels(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private void setupTabs() {
        tabViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), this));
        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(slidePanel.getPanelState()) {
                    case COLLAPSED:
                        slidePanel.setPanelState(ANCHORED);
                    break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(tabViewPager);
    }
}
