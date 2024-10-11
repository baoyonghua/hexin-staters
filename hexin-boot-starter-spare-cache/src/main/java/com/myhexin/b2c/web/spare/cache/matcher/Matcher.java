package com.myhexin.b2c.web.spare.cache.matcher;

import java.util.Properties;

/**
 * 采样者
 *
 * @author baoyh
 * @since 2024/9/8
 */
public interface Matcher {

    /**
     * 是否匹配
     *
     * @param key
     * @return
     */
    boolean match(String key);

    /**
     * 初始化
     *
     * @param props
     */
    void init(Properties props);
}
