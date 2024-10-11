package com.myhexin.b2c.web.spare.cache.autoconfigure.properties;

import com.myhexin.b2c.web.common.spi.SpiLoader;
import com.myhexin.b2c.web.spare.cache.consts.SpareCacheConstants;
import com.myhexin.b2c.web.spare.cache.manager.SpareCacheManager;
import com.myhexin.b2c.web.spare.cache.matcher.Matcher;
import com.myhexin.b2c.web.spare.cache.store.KvStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author baoyh
 * @since 2024/10/10
 */
@Component
public class DefaultCacheManager implements SpareCacheManager {

    private final Map<String, KvStore> storeCache = new HashMap<>(8);

    private final Map<String, Matcher> matcherCache = new HashMap<>(8);

    @Override
    public void init(SpareCacheConfigProperties properties) {
        for (SpareCacheConfigProperties.MatcherProps matcherProps : properties.getMatchers()) {
            matcherCache.computeIfAbsent(matcherProps.getType(), k -> {
                Matcher matcher = SpiLoader.of(Matcher.class)
                        .loadInstance(matcherProps.getType());
                matcher.init(matcherProps.getProps());
                return matcher;
            });
        }
        if (!matcherCache.containsKey(SpareCacheConstants.DEFAULT_MATCHER_CACHE_NAME)) {
            Matcher defaultMatcher = SpiLoader.of(Matcher.class)
                    .loadDefaultInstance();
            matcherCache.put(SpareCacheConstants.DEFAULT_MATCHER_CACHE_NAME, defaultMatcher);
        }

        for (SpareCacheConfigProperties.StoreProps storeProps : properties.getStores()) {
            storeCache.computeIfAbsent(storeProps.getType(), k -> {
                KvStore kvStore = SpiLoader.of(KvStore.class)
                        .loadInstance(storeProps.getType());
                kvStore.init(storeProps.getStoreConfig());
                return kvStore;
            });
        }
        if (!storeCache.containsKey(SpareCacheConstants.DEFAULT_STORE_CACHE_NAME)) {
            KvStore defaultStore = SpiLoader.of(KvStore.class)
                    .loadDefaultInstance();
            storeCache.put(SpareCacheConstants.DEFAULT_STORE_CACHE_NAME, defaultStore);
        }
    }

    @Override
    public KvStore getStore(String name) {
        KvStore store = storeCache.get(name);
        if (store == null) {
            throw new IllegalStateException("No store found for name: " + name);
        }
        return store;
    }

    @Override
    public Matcher getMatcher(String name) {
        Matcher matcher = matcherCache.get(name);
        if (matcher == null) {
            throw new IllegalStateException("No matcher found for name: " + name);
        }
        return matcher;
    }
}
