package com.myhexin.b2c.web.idempontent.annotations;

import com.myhexin.b2c.web.idempontent.exceptions.RepeatOperateException;
import com.myhexin.b2c.web.idempontent.types.RepeatType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 幂等注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Repeat {

    /**
     * 控制范围
     * 默认时间300ms
     *
     * @return long
     */
    @AliasFor("value")
    long scope() default 300;

    /**
     * 控制范围
     * 默认时间300ms
     *
     * @return long
     */
    @AliasFor("scope")
    long value() default 300;

    /**
     * 当出现幂等生效时抛出指定异常
     *
     * @return Class
     */
    Class<? extends Exception> throwable() default RepeatOperateException.class;

    /**
     * 当幂等生效时会后返回的错误提示
     *
     * @return 错误提示
     */
    String message() default "repeat submit";

    /**
     * 拦截策略(默认滑动窗口)
     *
     * @return RepeatType
     * @see RepeatType
     */
    RepeatType type() default RepeatType.SLIDING_WINDOW;

    /**
     * 方法锁时间(ms)
     *
     * @return Long
     */
    long methodLock() default 60 * 1000;

    /**
     * 从请求头中取值. 【web环境内生效】
     * 当需要从请求头中获取幂等键的时候,可以在这里指定请求头。
     * 此时会从Request的请求中获取数据
     *
     * @return String
     */
    String headValue() default "";
}
