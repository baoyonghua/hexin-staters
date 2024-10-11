package com.myhexin.b2c.web.spare.cache.annotations;

import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author baoyh
 * @since 2024/10/9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableSpareCache {

    /**
     * 注解，默认为SpareCache
     *
     * @return
     */
    Class<? extends Annotation> annotation() default SpareCache.class;

    /**
     * 是否启用CGLIB代理，默认为JDK
     *
     * @return
     */
    boolean proxyTargetClass() default false;

    /**
     * 设置优先级
     * <p>
     * 默认是最低优先级，为了在所有其他BeanPostProcessor之后运行，以便它可以向现有代理添加 advisor 而不是双重代理
     *
     * @return
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
