package com.life360.android.tabbar;

/**
 * Created by thomas on 6/2/17.
 */

public interface TabBarInteractorInput {

    void onTabBarInitialized();
    void onTabBarDestroyed();
    void onTabSelected(TabBarPresenterOutput.TabType tabType);

}
