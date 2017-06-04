package com.life360.android.tabbar;

import com.life360.android.scaffold.Interactor;
import com.life360.android.dagger.JobScheduler;
import com.life360.android.dagger.MainScheduler;

import javax.inject.Inject;

import io.reactivex.Scheduler;

import static com.life360.android.tabbar.TabBarPresenterOutput.TabType.TAB_PROFILE;

/**
 * Created by thomas on 6/2/17.
 */

public class TabBarInteractor extends Interactor implements TabBarInteractorInput {

    private final TabBarRouter router;
    private final TabBarInteractorOutput presenter;

    @Inject
    TabBarInteractor(@JobScheduler Scheduler subscribeOn, @MainScheduler Scheduler observeOn,
                     TabBarRouter router, TabBarInteractorOutput presenter) {
        super(subscribeOn, observeOn);

        this.router = router;
        this.presenter = presenter;
    }

    // TabBarInteractorInput Implementors

    @Override
    public void onTabBarInitialized() {
        //presenter.selectTab(TAB_PLACES);
        presenter.setBadgeCount(TAB_PROFILE, 5);
    }

    @Override
    public void onTabBarDestroyed() {
        dispose();
    }

    @Override
    public void onTabSelected(TabBarPresenterOutput.TabType tabType) {
        System.out.println("Tab Selected for type: " + tabType);
    }
}
