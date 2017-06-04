package com.life360.android.protomap;

import android.app.Application;

import com.life360.android.dagger.DaggerApp;
import com.life360.android.dagger.ComponentManager;

/**
 * Created by thomas on 6/2/17.
 */

public class App extends Application implements DaggerApp {

    private ComponentManager componentManager;

    @Override
    public void onCreate() {
        super.onCreate();
        componentManager = new ComponentManager(this);
    }

    @Override
    public ComponentManager getComponentManager() {
        return componentManager;
    }

}