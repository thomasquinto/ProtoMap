package com.life360.android.tabbar;

/**
 * Created by thomas on 6/2/17.
 */

public interface TabBarInteractorOutput {

    void setInteractorInput(TabBarInteractorInput interactor);
    void selectTab(int tabType);
    void setBadgeCount(int tabType, int badgeCount);

}
