package com.life360.android.tabbar;

import android.content.Context;

import javax.inject.Inject;

/**
 * Created by thomas on 6/2/17.
 */

public class TabBarRouterImpl implements TabBarRouter {

    private final Context context;

    @Inject
    public TabBarRouterImpl(Context context) {
        this.context = context;
    }
}
