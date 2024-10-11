package com.myhexin.b2c.web.spare.cache.manager;

import com.myhexin.b2c.web.spare.cache.autoconfigure.properties.SpareCacheConfigProperties;
import com.myhexin.b2c.web.spare.cache.matcher.Matcher;
import com.myhexin.b2c.web.spare.cache.store.KvStore;

/**
 * @author baoyh
 * @since 2024/9/8
 */
public interface SpareCacheManager {

    void init(SpareCacheConfigProperties properties);

    KvStore getStore(String name);

    Matcher getMatcher(String name);
}
