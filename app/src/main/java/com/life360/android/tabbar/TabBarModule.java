package com.life360.android.tabbar;

import android.content.Context;

import com.life360.android.dagger.JobScheduler;
import com.life360.android.dagger.MainScheduler;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

/**
 * Created by thomas on 6/2/17.
 */

@Module
public class TabBarModule {

    TabBarPresenter presenter;

    public TabBarModule() {}

    @Provides
    @TabBarScope
    TabBarInteractorInput provideInteractorInput(@JobScheduler Scheduler subscribeOn,
                                              @MainScheduler Scheduler observeOn,
                                              TabBarRouter router,
                                              TabBarInteractorOutput presenter) {

        TabBarInteractor interactor = new TabBarInteractor(subscribeOn, observeOn, router, presenter);
        presenter.setInteractorInput(interactor); // Cannot use dagger here since this is a cyclic reference
        return interactor;
    }

    @Provides
    @TabBarScope
    TabBarInteractorOutput provideInteractorOutput() {
        presenter = new TabBarPresenter();
        return presenter;
    }

    @Provides
    @TabBarScope
    TabBarPresenterInput providePresenterInput() {
        return presenter;
    }

    @Provides
    @TabBarScope
    TabBarRouter provideRouter(Context context) {
        return new TabBarRouterImpl(context);
    }
}
