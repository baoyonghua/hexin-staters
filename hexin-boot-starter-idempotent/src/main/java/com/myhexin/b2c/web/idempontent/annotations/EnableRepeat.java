package com.myhexin.b2c.web.idempontent.annotations;

import org.springframework.core.Ordered;

/**
 * 项目开启幂等
 * @author baoyh
 * @since 2024/10/10
 */
public @interface EnableRepeat {

    /**
     * 是否启用CGLIB代理，默认为JDK
     *
     * @return
     */
    boolean proxyTargetClass() default false;

    int order() default Ordered.LOWEST_PRECEDENCE;
}
