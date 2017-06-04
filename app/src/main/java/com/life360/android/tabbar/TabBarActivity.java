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
import com.life360.android.dagger.DaggerApp;
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

    @BindView(R.id.tab_bar)
    BottomBar tabBar;

    @BindView(R.id.tab_view_people)
    View tabViewPeople;
    @BindView(R.id.tab_view_places)
    View tabViewPlaces;
    @BindView(R.id.tab_view_safety)
    View tabViewSafety;
    @BindView(R.id.tab_view_profile)
    View tabViewProfile;

    private TabBarPresenterInput presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_bar);
        ButterKnife.bind(this);

        presenter = new TabBarBuilder((DaggerApp) getApplication()).getPresenter();
        presenter.attachView(this);

        setupTabBarListeners();

        // TODO: Move this into a Map Riblet
        setupMapFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(this);
    }

    private void setupTabBarListeners() {
        tabBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switchTab(tabId);
                presenter.tabSelected(getTabTypeForId(tabId));
            }
        }, true); // true means fire tab-selected event for very first tab upon initialization

        // If you want to listen for reselection events, here's how you do it:
        tabBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switchTab(tabId);
                presenter.tabSelected(getTabTypeForId(tabId));
            }
        });
    }

    private void clearTabBarListeners() {
        tabBar.setOnTabSelectListener(null, false);
        tabBar.setOnTabReselectListener(null);
    }

    private void switchTab(int tabId) {
        if (tabId != R.id.tab_people) tabViewPeople.setVisibility(View.GONE);
        if (tabId != R.id.tab_places) tabViewPlaces.setVisibility(View.GONE);
        if (tabId != R.id.tab_safety) tabViewSafety.setVisibility(View.GONE);
        if (tabId != R.id.tab_profile) tabViewProfile.setVisibility(View.GONE);

        int statusBarColor = 0;

        switch (tabId) {
            case R.id.tab_people:
                tabViewPeople.setVisibility(View.VISIBLE);
                statusBarColor = R.color.grape_500;
                break;
            case R.id.tab_places:
                tabViewPlaces.setVisibility(View.VISIBLE);
                statusBarColor = R.color.pink_500;
                break;
            case R.id.tab_safety:
                tabViewSafety.setVisibility(View.VISIBLE);
                statusBarColor = R.color.orange_500;
                break;
            case R.id.tab_profile:
                tabViewProfile.setVisibility(View.VISIBLE);
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
                        animateTabBarHeight(tabBar, tabBar.getHeight());
                    }
                });

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        animateTabBarHeight(tabBar, 0);
                    }
                });
            }
        });
    }

    private int getTabTypeForId(int tabId) {
        switch (tabId) {
            case R.id.tab_people:
                return TAB_PEOPLE;
            case R.id.tab_places:
                return TAB_PLACES;
            case R.id.tab_safety:
                return TAB_SAFETY;
            case R.id.tab_profile:
                return TAB_PROFILE;
            default:
                System.err.println("Unknown Tab ID value: " + tabId);
                return -1;
        }
    }

    private int getTabIdForType(int tabType) {
        switch (tabType) {
            case TAB_PEOPLE:
                return R.id.tab_people;
            case TAB_PLACES:
                return R.id.tab_places;
            case TAB_SAFETY:
                return R.id.tab_safety;
            case TAB_PROFILE:
                return R.id.tab_profile;
            default:
                System.err.println("Unknown Tab Type value: " + tabType);
                return -1;
        }
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
    private ViewPropertyAnimatorCompat translationAnimator;

    private void animateTabBarHeight(final View child, final int offset) {
        ensureOrCancelAnimator(child);
        translationAnimator.translationY(offset).start();
    }

    private void ensureOrCancelAnimator(View child) {
        if (translationAnimator == null) {
            translationAnimator = ViewCompat.animate(child);
            translationAnimator.setDuration(300);
            translationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            translationAnimator.cancel();
        }
    }

    // TabBarPresenterOutput Implementors

    @Override
    public void selectTab(int tabType) {
        // clear listeners to avoid tab selected infinite loop
        clearTabBarListeners();

        int tabId = getTabIdForType(tabType);
        tabBar.selectTabWithId(tabId);
        switchTab(tabId); // call this manually here since listeners have been cleared

        setupTabBarListeners();
    }

    @Override
    public void setBadgeCount(int tabType, int badgeCount) {

        BottomBarTab tab = tabBar.getTabWithId(getTabIdForType(tabType));

        if (tab != null) {
            if (badgeCount <= 0) {
                tab.removeBadge();
            } else {
                tab.setBadgeCount(badgeCount);
            }
        }
    }

}