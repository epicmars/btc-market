package com.fafabtc.app.ui.base;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jastrelax on 2017/8/19.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindLayout {
    /**
     * 布局资源
     * @return
     */
    @LayoutRes int value() default 0;

    /**
     * 与视图绑定的数据类型。
     * @return
     */
    Class<?>[] dataTypes() default {};

    /**
     * 是否允许依赖注入。
     * @return
     */
    boolean injectable() default true;
}
