package com.life360.android.protomap.tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.life360.android.protomap.R;

/**
 * Created by thomas on 4/30/17.
 */

public class BottomNavigationActivity extends Activity {

    private static boolean changeColor = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        //Initializing the bottomNavigationView
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (changeColor) {
                        switch (item.getItemId()) {
                            case R.id.action_people:
                                bottomNavigationView.setItemBackgroundResource(R.color.main_kale_500);
                                return true;
                            case R.id.action_places:
                                bottomNavigationView.setItemBackgroundResource(R.color.main_mango_500);
                                return true;
                            case R.id.action_safety:
                                bottomNavigationView.setItemBackgroundResource(R.color.main_blueberry_500);
                                return true;
                            case R.id.action_profile:
                                bottomNavigationView.setItemBackgroundResource(R.color.main_kiwi_500);
                                return true;
                        }
                    }
                    return true;
                }
            });
    }

    public static void start(Context c) {
        c.startActivity(new Intent(c, BottomNavigationActivity.class));
    }
}