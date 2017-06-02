package com.life360.android.baseui;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Presenters manage communication between Interactors and Views.
 *
 * From Interactors to Views, the Presenter translates business models into view model objects that
 * the View can display.
 *
 * From Views to Interactors, Presenters translate user interaction events, such as clicking a button,
 * into appropriate actions in Interactors
 */

public abstract class Presenter<V extends PresenterOutput> {
    private WeakReference<V> viewRef;

    /**
     * Detach the view from this presenter.
     *
     * @param view
     *     view to detach
     */
    public final void detachView(@NonNull V view) {
        if (getView() == view) {
            onDetachView(view);
            viewRef.clear();
        }
    }

    /**
     * Checks if a view is attached to this presenter.
     *
     * @return {@code true} if presenter has an attached view
     */
    public final boolean hasView() {
        return viewRef != null && viewRef.get() != null;
    }

    /**
     * Attach the view to this presenter.
     *
     * @param view
     *     view to attach
     */
    public final void attachView(@NonNull V view) {
        final V currentView = getView();
        if (currentView != view) {
            if (currentView != null) {
                detachView(currentView);
            }
            viewRef = new WeakReference<>(view);
            onAttachView(view);
        }
    }

    /**
     * Returns the attached view.
     *
     * @return {@code null}, if view is not attached, otherwise the concrete view instance.
     */
    protected final V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    /**
     * Called before view is detached.
     *
     * @param view
     *     view is going to be detached
     *
     * @see #detachView(PresenterOutput)
     */
    protected void onDetachView(final V view) {
    }

    /**
     * Called after view is attached.
     *
     * @param view
     *     attached to this presenter
     *
     * @see #attachView(PresenterOutput)
     */
    protected void onAttachView(V view) {
    }
}

