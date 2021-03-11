package com.lzz.wangpan.annotation;

import com.lzz.wangpan.enums.InterceptorLevel;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthInterceptor {

    /**
     * 定义拦截级别，默认为用户级别拦截
     *
     * @return {@link InterceptorLevel}
     */
    InterceptorLevel value() default InterceptorLevel.USER;
}
