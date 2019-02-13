package com.life360.android.protomap.example;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.life360.android.protomap.R;
import com.life360.android.protomap.model.Locatable;
import com.life360.android.protomap.model.Member;
import com.life360.android.protomap.model.Place;
import com.life360.android.protomap.ui.HorizontalCardsFragment;
import com.life360.android.protomap.ui.TabPagerAdapter;
import com.life360.android.protomap.util.UiUtils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.life360.android.protomap.ui.MapActivity.MAP_INIT_LOCATION;
import static com.life360.android.protomap.ui.MapActivity.MAP_INIT_ZOOM_LEVEL;
import static com.life360.android.protomap.ui.MapActivity.MAP_RADIUS_METERS;
import static com.life360.android.protomap.ui.TabPagerAdapter.TAB_ICON_SELECTED_FONT_SIZE_DP;

public class MaterialUpConceptActivity extends AppCompatActivity
	implements AppBarLayout.OnOffsetChangedListener, OnMapReadyCallback, View.OnTouchListener {

	private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 80;
	private boolean mIsAvatarShown = true;

	private ImageView mProfileImage;
	private int mMaxScrollSize;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_material_up_concept);
		ButterKnife.bind(this);

		final AppBarLayoutTouchIntercept appbarLayout = (AppBarLayoutTouchIntercept) findViewById(R.id.materialup_appbar);
		mProfileImage = (ImageView) findViewById(R.id.materialup_profile_image);

		appbarLayout.addOnOffsetChangedListener(this);
		mMaxScrollSize = appbarLayout.getTotalScrollRange();

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

		/*
		TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
		ViewPager viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
		viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        */

		// TQ Added
		System.out.println("");
		generateDummyData();
		setupMapFragment();
		setupTabs();
		setupFab();
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

	private boolean mEnableMapScrolling = false;

	@BindView(R.id.tab_view_pager)
	ViewPager tabViewPager;
	@BindView(R.id.tab_layout)
	TabLayout tabLayout;

	// Model Data
	private List<Member> members;
	private List<Place> places;

	private GoogleMap map;
	private List<Marker> markers;

	private void setupMapFragment() {
		final TouchableMapFragment mapFragment = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		// Will asynchronously invoke onMapReady
		mapFragment.getMapAsync(this);
		mapFragment.setOnTouchListener(this);


		final View view = mapFragment.getView();
		final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					if (map != null) {
						view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						addMapMarkers();
					}
				}
			});
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MAP_INIT_LOCATION, MAP_INIT_ZOOM_LEVEL));

		final AtomicBoolean toggle = new AtomicBoolean(true);
		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				toggle.set(!toggle.get());
			}
		});
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

	private void addMapMarkers() {
		ArrayList<Locatable> locatables = new ArrayList<>();
		locatables.addAll(members);
		locatables.addAll(places);

		markers = Locatable.addMarkersToMap(this, locatables, map);
		zoomForAllLocatables();
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
				tabLayout.getTabAt(tab.getPosition()).setText(TabPagerAdapter.getTabText(MaterialUpConceptActivity.this, tab.getPosition(), true));
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				tabLayout.getTabAt(tab.getPosition()).setText(TabPagerAdapter.getTabText(MaterialUpConceptActivity.this, tab.getPosition(), false));
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
	}

	private void zoomOnLocatable(Locatable locatable) {
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(locatable.getLatLng(), 15), 1000, null);
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

	private void generateDummyData() {
		members = Member.generateDummyData(MAP_INIT_LOCATION, MAP_RADIUS_METERS);
		places = Place.generateDummyData(MAP_INIT_LOCATION, MAP_RADIUS_METERS);
	}

	private void setupFab() {

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setImageDrawable(new IconicsDrawable(this)
				.icon(GoogleMaterial.Icon.gmd_people)
				.color(Color.WHITE)
				.sizeDp(TAB_ICON_SELECTED_FONT_SIZE_DP));

		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				zoomForAllLocatables();

				mEnableMapScrolling = !mEnableMapScrolling;
			}
		});
	}
}
