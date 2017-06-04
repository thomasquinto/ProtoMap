package com.life360.android.tabbar;

import com.life360.android.baseui.Builder;
import com.life360.android.dagger.DaggerApp;

import javax.inject.Inject;

/**
 * Created by thomas on 6/2/17.
 */

public class TabBarBuilder implements Builder {

    @Inject
    TabBarInteractorInput interactor;
    @Inject
    TabBarPresenterInput presenter;

    public TabBarBuilder(DaggerApp app) {
        app.getComponentManager().getTabBarComponent().inject(this);
    }

    public TabBarPresenterInput getPresenter() {
        return presenter;
    }
}
