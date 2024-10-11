package com.myhexin.b2c.web.spare.cache.annotations;

import com.myhexin.b2c.web.spare.cache.consts.SpareCacheConstants;

import java.lang.annotation.*;

/**
 * 缓存兜底注解
 *
 * @author baoyh
 * @since 2024/9/8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SpareCache {
    /**
     * 兜底缓存名称，用于标识当前方法使用的缓存兜底实现
     *
     * @return
     */
    String name() default SpareCacheConstants.DEFAULT_MATCHER_CACHE_NAME;

    String samplerName() default "";

    /**
     * 缓存key, 支持SpEL表达式, eg. #root.methodName+'_'#name
     *
     * @return
     */
    String key();

    /**
     * 符合条件的进行缓存， 默认：都缓存，支持SpEL表达式
     */
    String condition() default "";

    /**
     * 符合条件的不进行缓存， 默认：都缓存，支持SpEL表达式
     */
    String unless() default "";

    /**
     * 方法的返回结果null是否需要处理
     * <p>
     * 如果为true，则方法返回为null，也会进行缓存
     * </p>
     *
     * @return
     */
    boolean handleWithNullResult() default false;

    /**
     * 异常是否需要处理
     *
     * @return
     */
    boolean handleWithException() default true;

    /**
     * 需要处理的异常列表
     *
     * @returndlx
     */
    Class<? extends Throwable>[] exceptionsToTrace() default {Throwable.class};

    /**
     * 需要进行忽略的异常
     *
     * @RETURN
     */
    Class<? extends Throwable>[] exceptionsToIgnore() default {};

    /**
     * 自定义处理类
     *
     * @return
     */
    Class<?> customHandlerClass() default Void.class;

    /**
     * 自定义处理方法, 当方法执行出现异常/方法返回为空时，则默认调用此方法来进行兜底
     * like: public <T> static T handle(String key, T ret, Throwable e);
     *
     * @return
     */
    String customHandlerMethod() default "";
}
