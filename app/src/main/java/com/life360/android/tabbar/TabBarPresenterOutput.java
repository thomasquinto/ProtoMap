package com.life360.android.tabbar;

import com.life360.android.scaffold.PresenterOutput;

/**
 * Created by thomas on 6/2/17.
 */

public interface TabBarPresenterOutput extends PresenterOutput {

    enum TabType {
        TAB_PEOPLE,
        TAB_PLACES,
        TAB_SAFETY,
        TAB_PROFILE
    }

    void selectTab(TabType tabType);
    void setBadgeCount(TabType tabType, int badgeCount);

}
