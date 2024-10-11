package com.myhexin.b2c.web.spare.cache.store;

import com.myhexin.b2c.web.common.spi.Spi;
import com.myhexin.b2c.web.spare.cache.autoconfigure.properties.SpareCacheConfigProperties;

/**
 * @author baoyh
 * @since 2024/10/10
 */
@Spi(value = "redis")
public class RedisStore implements KvStore {
    @Override
    public void put(String key, Object value) {

    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public void init(SpareCacheConfigProperties.StoreConfig config) {

    }
}
