package com.life360.android.tabbar;

import dagger.Subcomponent;

/**
 * Created by thomas on 6/2/17.
 */

@TabBarScope
@Subcomponent(modules={ TabBarModule.class })
public interface TabBarComponent {

    void inject(TabBarBuilder builder);

}
