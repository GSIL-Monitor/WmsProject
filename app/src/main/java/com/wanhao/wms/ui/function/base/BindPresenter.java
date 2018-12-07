package com.wanhao.wms.ui.function.base;

import android.support.annotation.StringRes;

import com.wanhao.wms.base.bind.BindView;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述：
 * 邮箱 email:strive_bug@yeah.net
 * 创建时间 2018/12/6
 *
 * @author ql
 */
@Documented
@Inherited
//该注解可以作用于方法,类与接口
@Target({ElementType.METHOD, ElementType.TYPE})
//JVM会读取注解,所以利用反射可以获得注解
@Retention(RetentionPolicy.RUNTIME)
public @interface BindPresenter {
    String title() default "-1";

    int titleRes() default -1;
}
