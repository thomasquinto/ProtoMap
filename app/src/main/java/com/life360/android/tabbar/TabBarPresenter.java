package com.life360.android.tabbar;

/**
 * Created by thomas on 6/2/17.
 */

public class TabBarPresenter<V extends TabBarPresenterOutput> extends TabBarPresenterInput<V>
    implements TabBarInteractorOutput {

    private TabBarInteractorInput interactor;

    @Override
    public void onAttachView(TabBarPresenterOutput view) {
        interactor.onTabBarInitialized();
    }

    @Override
    public void onDetachView(TabBarPresenterOutput view) {
        interactor.onTabBarDestroyed();
    }

    // TabBarPresenterInput Implementors

    @Override
    public void tabSelected(TabBarPresenterOutput.TabType tabType) {
        interactor.onTabSelected(tabType);
    }

    // TabBarInteractorOutput Implementors

    @Override
    public void setInteractorInput(TabBarInteractorInput interactor) {
        this.interactor = interactor;
    }

    @Override
    public void selectTab(TabBarPresenterOutput.TabType tabType) {
        if (getView() != null) {
            getView().selectTab(tabType);
        }
    }

    @Override
    public void setBadgeCount(TabBarPresenterOutput.TabType tabType, int badgeCount) {
        if (getView() != null) {
            getView().setBadgeCount(tabType, badgeCount);
        }
    }

}
