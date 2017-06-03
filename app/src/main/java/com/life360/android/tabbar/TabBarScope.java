package com.life360.android.tabbar;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by thomas on 6/2/17.
 */

@Scope
@Documented
@Retention(value= RetentionPolicy.RUNTIME)
public @interface TabBarScope {
}
