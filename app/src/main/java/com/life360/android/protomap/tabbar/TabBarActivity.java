package com.life360.android.protomap.tabbar;

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
import com.life360.android.protomap.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thomas on 4/30/17.
 */

public class TabBarActivity extends FragmentActivity {

    public static void start(Context c) {
        c.startActivity(new Intent(c, TabBarActivity.class));
    }

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @BindView(R.id.tab1)
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
        setContentView(R.layout.activity_tab_bar);
        ButterKnife.bind(this);

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

        bottomBar.setBadgeBackgroundColor(ContextCompat.getColor(this, R.color.orange_100));
        bottomBar.getTabAtPosition(3).setBadgeCount(3);

        setupMapFragment();
    }

    private void switchTab(int tabId) {
        if (tab1 != null) tab1.setVisibility(View.GONE);
        tab2.setVisibility(View.GONE);
        tab3.setVisibility(View.GONE);
        tab4.setVisibility(View.GONE);

        int statusBarColor = 0;

        switch (tabId) {
            case R.id.tab_people:
                tab1.setVisibility(View.VISIBLE);
                statusBarColor = R.color.grape_500;
                break;
            case R.id.tab_places:
                tab2.setVisibility(View.VISIBLE);
                statusBarColor = R.color.pink_500;
                break;
            case R.id.tab_safety:
                tab3.setVisibility(View.VISIBLE);
                statusBarColor = R.color.orange_500;
                break;
            case R.id.tab_profile:
                tab4.setVisibility(View.VISIBLE);
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
            if (getOrientation() == LAND) {
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
                        animateOffset(bottomBar, bottomBar.getHeight());
                    }
                });

                googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        animateOffset(bottomBar, 0);
                    }
                });
            }
        });
    }

    private static final int PORT = 1;
    private static final int LAND = 2;
    private int getOrientation() {
        WindowManager windowService = (WindowManager)getSystemService(Context.WINDOW_SERVICE);

        int rotation = windowService.getDefaultDisplay().getRotation();

        if (Surface.ROTATION_0 == rotation) {
            rotation = PORT;
        } else if(Surface.ROTATION_180 == rotation) {
            rotation = PORT;
        } else if(Surface.ROTATION_90 == rotation) {
            rotation = LAND;
        } else if(Surface.ROTATION_270 == rotation) {
            rotation = LAND;
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

}