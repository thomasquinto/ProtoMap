package com.life360.android.tabbar;

import com.life360.android.baseui.Presenter;

/**
 * Created by thomas on 6/2/17.
 */

public abstract class TabBarPresenterInput<V extends TabBarPresenterOutput> extends Presenter<V> {

    abstract void tabSelected(int tabType);

}
