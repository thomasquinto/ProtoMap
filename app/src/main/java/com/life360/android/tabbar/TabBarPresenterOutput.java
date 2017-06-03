package com.life360.android.tabbar;

import com.life360.android.baseui.PresenterOutput;

/**
 * Created by thomas on 6/2/17.
 */

public interface TabBarPresenterOutput extends PresenterOutput {

    int TAB_PEOPLE = 0;
    int TAB_PLACES = 1;
    int TAB_SAFETY = 2;
    int TAB_PROFILE = 3;

    void selectTab(int tabType);
    void setBadgeCount(int tabType, int badgeCount);

}
