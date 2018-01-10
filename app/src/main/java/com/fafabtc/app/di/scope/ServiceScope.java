package com.fafabtc.app.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by jastrelax on 2018/1/8.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceScope {
}
