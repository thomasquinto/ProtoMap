package com.life360.android.dagger;

import com.life360.android.tabbar.TabBarComponent;
import com.life360.android.tabbar.TabBarModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by thomas on 5/8/17.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    // factory method to instantiate the subcomponent defined here (passing in the module instance)
    TabBarComponent newTabBarComponent(TabBarModule mapModule);

}