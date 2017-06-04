package com.life360.android.protomap.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.life360.android.protomap.R;
import com.life360.android.protomap.example.MaterialUpConceptActivity;
import com.life360.android.protomap.model.Locatable;
import com.life360.android.protomap.model.Member;
import com.life360.android.protomap.model.Place;
import com.life360.android.protomap.tab.BottomBarActivity;
import com.life360.android.protomap.tab.BottomNavigationActivity;
import com.life360.android.protomap.util.UiUtils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.life360.android.protomap.ui.TabPagerAdapter.TAB_ICON_SELECTED_FONT_SIZE_DP;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.HIDDEN;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final LatLng MAP_INIT_LOCATION = new LatLng(37.7749, -122.4194); // SF
    public static final int MAP_INIT_ZOOM_LEVEL = 1;
    public static final int MAP_RADIUS_METERS = 100000;

    private GoogleMap map;
    private List<Marker> markers;
    private int mapHeight;

    private static final int STATUS_BAR_HEIGHT_DP = 24;
    private static final int NAV_BAR_HEIGHT_DP = 48;
    private static final int SLIDE_PANEL_RESTING_HEIGHT_DP = NAV_BAR_HEIGHT_DP * 2 + 8;
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
    @BindView(R.id.logo_icon)
    ImageView logoIcon;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private SlidingUpPanelLayout.PanelState slidePanelPreviousState;

    // Model Data
    private List<Member> members;
    private List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(AppCompatDelegate.create(this, null)));

        generateDummyData();

        setupMapFragment();
        setupSlidePanel();
        setupTabs();
        setupWindowLayout();
        setupProgressSpinner();
        setupFab();
    }

    public static void start(Context c) {
        c.startActivity(new Intent(c, MapActivity.class));
    }

    private void generateDummyData() {
        members = Member.generateDummyData(MAP_INIT_LOCATION, MAP_RADIUS_METERS);
        places = Place.generateDummyData(MAP_INIT_LOCATION, MAP_RADIUS_METERS);
    }

    private void setupWindowLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            /*
            setBackgroundColor(statusBar, getStatusBarColor(SLIDE_PANEL_ANCHOR_POINT_RATIO));
            setBackgroundColor(navBar, getStatusBarColor(SLIDE_PANEL_ANCHOR_POINT_RATIO));
            */
            setBackgroundColor(statusBar, ContextCompat.getColor(this, R.color.primary_main_grape_500));
            setBackgroundColor(navBar, ContextCompat.getColor(this, R.color.primary_main_grape_500));
        }
    }

    private void setupMapFragment() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Will asynchronously invoke onMapReady
        mapFragment.getMapAsync(this);

        final View view = mapFragment.getView();
        final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mapHeight = view.getHeight() - SLIDE_PANEL_PARALLAX_OFFSET_PX - UiUtils.dpToPixels(MapActivity.this, STATUS_BAR_HEIGHT_DP);
                    System.out.println("MAP HEIGHT: " + mapHeight);

                    addMapMarkers();
                }
            });
        }
    }

    private void addMapMarkers() {
        ArrayList<Locatable> locatables = new ArrayList<>();
        locatables.addAll(members);
        locatables.addAll(places);

        markers = Locatable.addMarkersToMap(MapActivity.this, locatables, map);
        zoomForAllLocatables();
    }

    private void zoomForAllLocatables() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = UiUtils.dpToPixels(this, 50);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
    }

    private void zoomOnLocatable(Locatable locatable) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(locatable.getLatLng(), 15), 1000, null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(MAP_INIT_LOCATION, MAP_INIT_ZOOM_LEVEL));

        final AtomicBoolean toggle = new AtomicBoolean(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (toggle.get()) {
                    slidePanelPreviousState = slidePanel.getPanelState();
                    slidePanel.setPanelState(HIDDEN);

                    setBackgroundColor(statusBar, getStatusBarColor(SLIDE_PANEL_ANCHOR_POINT_RATIO));
                    setBackgroundColor(navBar, getStatusBarColor(SLIDE_PANEL_ANCHOR_POINT_RATIO));
                } else {
                    slidePanel.setPanelState(slidePanelPreviousState);

                    setBackgroundColor(statusBar, ContextCompat.getColor(MapActivity.this, R.color.primary_main_grape_500));
                    setBackgroundColor(navBar, ContextCompat.getColor(MapActivity.this, R.color.primary_main_grape_500));
                }
                toggle.set(!toggle.get());
            }
        });

        map.setPadding(0, UiUtils.dpToPixels(this, STATUS_BAR_HEIGHT_DP), 0, 0);
    }

    private void setupSlidePanel() {
        slidePanel.setPanelHeight(UiUtils.dpToPixels(this, SLIDE_PANEL_RESTING_HEIGHT_DP));
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
                    int paddingTop = UiUtils.dpToPixels(MapActivity.this, STATUS_BAR_HEIGHT_DP) + (int) (paddingBottom * .45f);
                    map.setPadding(0, paddingTop, 0, paddingBottom);
                } else {
                    /*
                    Integer statusBarColor = getStatusBarColor(slideOffset);
                    setBackgroundColor(statusBar, statusBarColor);
                    setBackgroundColor(navBar, statusBarColor);
                    */
                    logoIcon.setAlpha(1.0f - slideOffset);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == HIDDEN) {
                    showProgressSpinner();
                } else {
                    hideProgressSpinner();
                }
            }
        });
    }

    private void setBackgroundColor(View view, Integer color) {
        if (color != null) {
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

    private void setupTabs() {
        TabPagerAdapter tabPageAdapter = new TabPagerAdapter(getSupportFragmentManager(), this);

        final ViewPager.OnPageChangeListener onMemberChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Member member = members.get(position);
                zoomOnLocatable(member);
            }
        };

        final ViewPager.OnPageChangeListener onPlaceChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Place place = places.get(position);
                zoomOnLocatable(place);
            }
        };

        //VerticalCardFragment memberFragment = new VerticalCardFragment(this, new MemberAdapter(this, members));
        HorizontalCardsFragment memberFragment = new HorizontalCardsFragment(this, members, onMemberChangeListener);
        tabPageAdapter.addFragment(0, memberFragment);

        //VerticalCardFragment placeFragment = new VerticalCardFragment(this, new PlaceAdapter(this, places));
        HorizontalCardsFragment placeFragment = new HorizontalCardsFragment(this, places, onPlaceChangeListener);
        tabPageAdapter.addFragment(1, placeFragment);

        tabViewPager.setAdapter(tabPageAdapter);

        tabLayout.setupWithViewPager(tabViewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                resetSlidePanelToAnchorPosition();
                tabLayout.getTabAt(tab.getPosition()).setText(TabPagerAdapter.getTabText(MapActivity.this, tab.getPosition(), true));

                if (tab.getPosition() == 3) {
                    askToOpenDemo();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).setText(TabPagerAdapter.getTabText(MapActivity.this, tab.getPosition(), false));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                resetSlidePanelToAnchorPosition();

                if (tab.getPosition() == 3) {
                    askToOpenDemo();
                }
            }
        });
    }

    private void askToOpenDemo() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_NEUTRAL:
                        MaterialUpConceptActivity.start(MapActivity.this);
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        BottomBarActivity.start(MapActivity.this);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        BottomNavigationActivity.start(MapActivity.this);
                        //TabBarActivity.start(MapActivity.this);
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("See Another Demo?")
                .setNeutralButton("Map #2", dialogClickListener)
                .setPositiveButton("Tabs #2", dialogClickListener)
                .setNegativeButton("Tabs #1", dialogClickListener)
                .show();
    }

    private void resetSlidePanelToAnchorPosition() {
        switch (slidePanel.getPanelState()) {
            case COLLAPSED:
                slidePanel.setPanelState(ANCHORED);
                break;
        }
    }

    private void setupProgressSpinner() {
        logoIcon.setBackground(ContextCompat.getDrawable(this, R.drawable.life360_logo_color_00045));
    }

    private void showProgressSpinner() {
        final Drawable logoDrawable = logoIcon.getBackground();
        if (!(logoDrawable instanceof AnimationDrawable)) {
            logoIcon.setBackgroundResource(R.drawable.logo_on_trans);
            final AnimationDrawable logoAnimation = (AnimationDrawable) logoIcon.getBackground();
            logoIcon.post(new Runnable() {
                @Override
                public void run() {
                    logoAnimation.start();
                }
            });
        }
    }

    private void hideProgressSpinner() {
        Drawable logoAnimation = logoIcon.getBackground();
        if (logoAnimation instanceof AnimationDrawable) {
            ((AnimationDrawable)logoAnimation).stop();
            logoIcon.setBackground(ContextCompat.getDrawable(this, R.drawable.life360_logo_color_00045));
        }
    }

    private void setupFab() {
        fab.setImageDrawable(new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_people)
                .color(Color.WHITE)
                .sizeDp(TAB_ICON_SELECTED_FONT_SIZE_DP));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomForAllLocatables();
                if (slidePanel.getPanelState() == EXPANDED) {
                    slidePanel.setPanelState(COLLAPSED);
                }
            }
        });
    }

}
