package com.life360.android.baseui;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Contains the business logic as specified by a use case
 */

public abstract class Interactor implements Disposable {
    private final Scheduler subscribeScheduler;
    private final Scheduler observeScheduler;
    private final CompositeDisposable disposables;

    /**
     * @param subscribeScheduler
     *     Scheduler to perform asynchronous requests
     * @param observeScheduler
     *     Scheduler that handles response
     */
    protected Interactor(@NonNull Scheduler subscribeScheduler, @NonNull Scheduler observeScheduler) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.disposables = new CompositeDisposable();
    }

    /**
     * Should be called when Presenter no longer needs any data or responses from Interactor
     * (i.e. View associated with Presenter was dismissed)
     */
    @Override
    public final void dispose() {
        // call clear() instead of dispose() to be able to manage new disposables
        disposables.clear();
    }

    @Override
    public final boolean isDisposed() {
        return disposables.size() == 0;
    }

    public void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

}
