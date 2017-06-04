package com.life360.android.dagger;

import android.app.Application;

import com.life360.android.tabbar.TabBarComponent;
import com.life360.android.tabbar.TabBarModule;

/**
 * Created by thomas on 6/3/17.
 */

public class ComponentManager {

    private AppComponent appComponent;

    public ComponentManager(Application app) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(app))
                .build();
    }

    private TabBarComponent tabBarComponent;

    public TabBarComponent getTabBarComponent() {
        if (tabBarComponent == null) {
            tabBarComponent = appComponent.newTabBarComponent(new TabBarModule());
        }

        return tabBarComponent;
    }

    // Deletes the object graph for all objects in TabBarScope
    public void endTabBarScope() {
        tabBarComponent = null;
    }
}
