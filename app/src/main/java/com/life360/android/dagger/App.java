package com.life360.android.dagger;

import android.app.Application;

import com.life360.android.tabbar.TabBarComponent;
import com.life360.android.tabbar.TabBarModule;

/**
 * Created by thomas on 6/2/17.
 */

public class App extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    private TabBarComponent TabBarComponent;

    public TabBarComponent getTabBarComponent() {
        if (TabBarComponent == null) {
            TabBarComponent = appComponent.newTabBarComponent(new TabBarModule());
        }

        return TabBarComponent;
    }
}