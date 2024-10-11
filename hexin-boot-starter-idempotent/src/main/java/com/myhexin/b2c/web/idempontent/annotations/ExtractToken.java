package com.myhexin.b2c.web.idempontent.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 提取 token，支持 Spel 表达式
 *
 * @author baoyh
 * @since 2024/10/10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface ExtractToken {

    /**
     * 支持使用el表达式解析token
     *
     * @return String
     */
    @AliasFor("token")
    String value() default "";

    /**
     * 支持使用spel表达式解析token
     *
     * @return
     */
    @AliasFor("value")
    String token() default "";

    /**
     * 固定的 token
     *
     * @return String
     */
    String fixed() default "";
}
