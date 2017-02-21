package com.life360.android.protomap;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
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
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.HIDDEN;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final LatLng MAP_INIT_LOCATION = new LatLng(59.145833, 10.223611);
    public static final int MAP_INIT_ZOOM_LEVEL = 10;
    private GoogleMap map;
    private int mapHeight;

    private static final int STATUS_BAR_HEIGHT_DP = 24;
    private static final int NAV_BAR_HEIGHT_DP = 48;
    private static final int SLIDE_PANEL_RESTING_HEIGHT_DP = NAV_BAR_HEIGHT_DP * 2;
    private static final float SLIDE_PANEL_ANCHOR_POINT_RATIO = 0.25f;
    private static final int SLIDE_PANEL_PARALLAX_OFFSET_PX = 500;
    private static final int STATUS_BAR_COLOR = R.color.primary_main_grape_500;

    @BindView(R.id.slide_panel)
    SlidingUpPanelLayout slidePanel;
    @BindView(R.id.tab_view_pager)
    ViewPager tabViewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.fake_status_bar)
    View statusBar;
    @BindView(R.id.fake_nav_bar)
    View navBar;

    private SlidingUpPanelLayout.PanelState slidePanelPreviousState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        ButterKnife.bind(this);

        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(AppCompatDelegate.create(this, null)));

        setupMapFragment();
        setupSlidePanel();
        setupTabs();
        setupWindowLayout();
    }

    private void setupWindowLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            setBackgroundColor(statusBar, getStatusBarColor(SLIDE_PANEL_ANCHOR_POINT_RATIO));
            setBackgroundColor(navBar, getStatusBarColor(SLIDE_PANEL_ANCHOR_POINT_RATIO));
        }
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
                    mapHeight = view.getHeight() - SLIDE_PANEL_PARALLAX_OFFSET_PX - dpToPixels(STATUS_BAR_HEIGHT_DP);
                    System.out.println("MAP HEIGHT: " + mapHeight);
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(MAP_INIT_LOCATION));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MAP_INIT_LOCATION, MAP_INIT_ZOOM_LEVEL));

        final AtomicBoolean toggle = new AtomicBoolean(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (toggle.get()) {
                    slidePanelPreviousState = slidePanel.getPanelState();
                    slidePanel.setPanelState(HIDDEN);
                } else {
                    slidePanel.setPanelState(slidePanelPreviousState);
                }
                toggle.set(!toggle.get());
            }
        });

        map.setPadding(0, dpToPixels(STATUS_BAR_HEIGHT_DP), 0, 0);
    }

    private void setupSlidePanel() {
        slidePanel.setPanelHeight(dpToPixels(SLIDE_PANEL_RESTING_HEIGHT_DP));
        slidePanel.setAnchorPoint(SLIDE_PANEL_ANCHOR_POINT_RATIO);
        slidePanel.setParallaxOffset(SLIDE_PANEL_PARALLAX_OFFSET_PX);
        //slidePanel.setOverlayed(true);
        slidePanel.setCoveredFadeColor(android.R.color.transparent); // disable scroll fading
        slidePanel.setPanelState(COLLAPSED);

        slidePanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset <= SLIDE_PANEL_ANCHOR_POINT_RATIO) {
                    int paddingBottom = (int) (slideOffset * mapHeight);
                    int paddingTop = dpToPixels(STATUS_BAR_HEIGHT_DP) + (int) (paddingBottom * .45f);
                    map.setPadding(0, paddingTop, 0, paddingBottom);
                } else {
                    setBackgroundColor(statusBar, getStatusBarColor(slideOffset));
                    setBackgroundColor(navBar, getStatusBarColor(slideOffset));
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });
    }

    private void setBackgroundColor(View view, Integer color) {
        if(color != null) {
            view.setBackgroundColor(color);
        }
    }

    private Integer getStatusBarColor(float offset) {
        if (offset >= 0 && offset <= 1) {
            int color = ContextCompat.getColor(MapActivity.this, STATUS_BAR_COLOR);
            float min = SLIDE_PANEL_ANCHOR_POINT_RATIO * 255f;
            float max = 255f;
            int alpha = (int) (min + offset * (max - min));
            color = ColorUtils.setAlphaComponent(color, alpha);
            return color;
        }

        return null;
    }

    private int dpToPixels(int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private void setupTabs() {
        tabViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(tabViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                resetSlidePanelToAnchorPosition();
                tabLayout.getTabAt(tab.getPosition()).setText(TabPagerAdapter.getTabText(MapActivity.this, tab.getPosition(), true));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).setText(TabPagerAdapter.getTabText(MapActivity.this, tab.getPosition(), false));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                resetSlidePanelToAnchorPosition();
            }
        });
    }

    private void resetSlidePanelToAnchorPosition() {
        switch(slidePanel.getPanelState()) {
            case COLLAPSED:
                slidePanel.setPanelState(ANCHORED);
                break;
        }
    }

}
