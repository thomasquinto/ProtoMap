package com.life360.android.tabbar;

/**
 * Created by thomas on 6/2/17.
 */

public class TabBarPresenter<V extends TabBarPresenterOutput> extends TabBarPresenterInput<V>
    implements TabBarInteractorOutput {

    private TabBarInteractorInput interactor;

    // TabBarPresenterInput Implementors

    @Override
    public void onTabBarInitialized() {

    }

    @Override
    public void tabSelected(int tabType) {

    }

    // TabBarInteractorOutput Implementors

    @Override
    public void setInteractorInput(TabBarInteractorInput interactor) {
        this.interactor = interactor;
    }

    @Override
    public void selectTab(int tabType) {

    }

}
