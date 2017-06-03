package com.life360.android.tabbar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.life360.android.dagger.App;
import com.life360.android.protomap.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thomas on 4/30/17.
 */

public class TabBarActivity extends FragmentActivity implements TabBarPresenterOutput {

    public static void start(Context c) {
        c.startActivity(new Intent(c, TabBarActivity.class));
    }

    @BindView(R.id.tabBar)
    BottomBar tabBar;

    @BindView(R.id.tab_people)
    View tabPeople;
    @BindView(R.id.tab_places)
    View tabPlaces;
    @BindView(R.id.tab_safety)
    View tabSafety;
    @BindView(R.id.tab_profile)
    View tabProfile;

    private TabBarPresenterInput presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_bar);
        ButterKnife.bind(this);

        tabBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switchTab(tabId);
            }
        });

        // If you want to listen for reselection events, here's how you do it:
        tabBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switchTab(tabId);
            }
        });

        // TODO: Move this into a Map Riblet
        setupMapFragment();

        presenter = new TabBarBuilder((App) getApplicationContext()).getPresenter();
        presenter.attachView(this);
    }

    private void switchTab(int tabId) {
        tabPeople.setVisibility(View.GONE);
        tabPlaces.setVisibility(View.GONE);
        tabSafety.setVisibility(View.GONE);
        tabProfile.setVisibility(View.GONE);

        int statusBarColor = 0;

        switch (tabId) {
            case R.id.tab_people:
                tabPeople.setVisibility(View.VISIBLE);
                statusBarColor = R.color.grape_500;
                break;
            case R.id.tab_places:
                tabPlaces.setVisibility(View.VISIBLE);
                statusBarColor = R.color.pink_500;
                break;
            case R.id.tab_safety:
                tabSafety.setVisibility(View.VISIBLE);
                statusBarColor = R.color.orange_500;
                break;
            case R.id.tab_profile:
                tabProfile.setVisibility(View.VISIBLE);
                statusBarColor = R.color.blue_500;
                break;
        }

        if (statusBarColor != 0) {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, statusBarColor));
            }
            if (getOrientation() == LANDSCAPE) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    window.setNavigationBarColor(ContextCompat.getColor(this, statusBarColor));
                }
            }
        }
    }

    private void setupMapFragment() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // Will asynchronously invoke onMapReady
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                    @Override
                    public void onCameraMoveStarted(int i) {
                        animateOffset(tabBar, tabBar.getHeight());
                    }
                });

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        animateOffset(tabBar, 0);
                    }
                });
            }
        });
    }

    private static final int PORTRAIT = 1;
    private static final int LANDSCAPE = 2;
    private int getOrientation() {
        WindowManager windowService = (WindowManager)getSystemService(Context.WINDOW_SERVICE);

        int rotation = windowService.getDefaultDisplay().getRotation();

        if (Surface.ROTATION_0 == rotation) {
            rotation = PORTRAIT;
        } else if(Surface.ROTATION_180 == rotation) {
            rotation = PORTRAIT;
        } else if(Surface.ROTATION_90 == rotation) {
            rotation = LANDSCAPE;
        } else if(Surface.ROTATION_270 == rotation) {
            rotation = LANDSCAPE;
        }
        return rotation;
    }

    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private ViewPropertyAnimatorCompat mTranslationAnimator;

    private void animateOffset(final View child, final int offset) {
        ensureOrCancelAnimator(child);
        mTranslationAnimator.translationY(offset).start();
    }

    private void ensureOrCancelAnimator(View child) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(child);
            mTranslationAnimator.setDuration(300);
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mTranslationAnimator.cancel();
        }
    }

    // TabBarPresenterOutput Implementors

    @Override
    public void selectTab(int tabType) {

        switch(tabType) {
            case TAB_PEOPLE:
                switchTab(R.id.tab_people);
                break;
            case TAB_PLACES:
                switchTab(R.id.tab_places);
                break;
            case TAB_SAFETY:
                switchTab(R.id.tab_safety);
                break;
            case TAB_PROFILE:
                switchTab(R.id.tab_profile);
                break;
            default:
                System.err.println("Unknown Tab Type value: " + tabType);
        }
    }

    @Override
    public void setBadgeCount(int tabType, int badgeCount) {

        BottomBarTab tab = null;

        switch(tabType) {
            case TAB_PEOPLE:
                tab = tabBar.getTabWithId(R.id.tab_people);
                break;
            case TAB_PLACES:
                tab = tabBar.getTabWithId(R.id.tab_places);
                break;
            case TAB_SAFETY:
                tab = tabBar.getTabWithId(R.id.tab_safety);
                break;
            case TAB_PROFILE:
                tab = tabBar.getTabWithId(R.id.tab_profile);
                break;
            default:
                System.err.println("Unknown Tab Type value: " + tabType);
        }

        if (tab != null) {
            tab.setBadgeCount(badgeCount);
        }
    }

}