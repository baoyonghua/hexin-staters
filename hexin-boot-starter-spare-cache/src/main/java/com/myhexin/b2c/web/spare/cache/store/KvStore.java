package com.myhexin.b2c.web.spare.cache.store;

import com.myhexin.b2c.web.spare.cache.autoconfigure.properties.SpareCacheConfigProperties;

/**
 * @author baoyh
 * @since 2024/9/8
 */
public interface KvStore {

    void put(String key, Object value);

    Object get(String key);

    void init(SpareCacheConfigProperties.StoreConfig config);
}
