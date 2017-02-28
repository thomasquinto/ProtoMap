package com.life360.android.protomap.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.life360.android.protomap.R;

import static com.life360.android.protomap.ui.MapActivity.MAP_INIT_LOCATION;

public class MaterialUpConceptActivity extends AppCompatActivity
	implements AppBarLayout.OnOffsetChangedListener, OnMapReadyCallback, View.OnTouchListener {

	private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 80;
	private boolean mIsAvatarShown = true;

	private ImageView mProfileImage;
	private int mMaxScrollSize;

	private boolean mEnableMapScrolling = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material_up_concept);

		TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
		ViewPager viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
		final AppBarLayoutTouchIntercept appbarLayout = (AppBarLayoutTouchIntercept) findViewById(R.id.materialup_appbar);
		mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);

		appbarLayout.addOnOffsetChangedListener(this);
		mMaxScrollSize = appbarLayout.getTotalScrollRange();

		viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
		tabLayout.setupWithViewPager(viewPager);

		setupMapFragment();

		CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbarLayout.getLayoutParams();
		AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
		behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
			@Override
			public boolean canDrag(AppBarLayout appBarLayout) {
				System.out.println("CAN DRAG: " + !appbarLayout.getDisregardTouches());
				return !appbarLayout.getDisregardTouches();
				//System.out.println("CAN DRAG: " + !mEnableMapScrolling);
				//return !mEnableMapScrolling;
			}
		});
		params.setBehavior(behavior);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mEnableMapScrolling = !mEnableMapScrolling;
			}
		});
	}

	public static void start(Context c) {
		c.startActivity(new Intent(c, MaterialUpConceptActivity.class));
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
		if (mMaxScrollSize == 0)
			mMaxScrollSize = appBarLayout.getTotalScrollRange();

		int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

		if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
			mIsAvatarShown = false;
			mProfileImage.animate().scaleY(0).scaleX(0).setDuration(200).start();
		}

		if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
			mIsAvatarShown = true;

			mProfileImage.animate()
				.scaleY(1).scaleX(1)
				.start();
		}
	}

	class TabsAdapter extends FragmentPagerAdapter {
		public TabsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int i) {
			switch(i) {
				case 0: return MaterialUpConceptFakePage.newInstance();
				case 1: return MaterialUpConceptFakePage.newInstance();
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch(position) {
				case 0: return "Tab 1";
				case 1: return "Tab 2";
			}
			return "";
		}
	}

	///////// TQ ADDED BELOW
	private GoogleMap map;

	private void setupMapFragment() {
		final TouchableMapFragment mapFragment = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		// Will asynchronously invoke onMapReady
		mapFragment.getMapAsync(this);
		//mapFragment.setOnTouchListener(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MAP_INIT_LOCATION, 15));
	}

	// OnTouchListener
	@Override
	public boolean onTouch(View view, MotionEvent ev) {
		System.out.println("Touch Event: " + ev.getAction());
		switch (ev.getAction()) {
			case MotionEvent.ACTION_UP:
				System.out.println("TOUCH UP");
				mEnableMapScrolling = false;
				break;
			default:
				System.out.println("TOUCH DOWN");
				mEnableMapScrolling = true;
				break;
		}

		return false;
	}
}
