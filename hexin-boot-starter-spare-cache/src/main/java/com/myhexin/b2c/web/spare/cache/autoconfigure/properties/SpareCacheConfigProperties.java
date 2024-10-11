package com.myhexin.b2c.web.spare.cache.autoconfigure.properties;

import com.myhexin.b2c.web.spare.cache.consts.SpareCacheConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Properties;

/**
 * @author baoyh
 * @since 2024/9/8
 */
@Data
@ConfigurationProperties(prefix = "hexin-boot.spare-cache")
public class SpareCacheConfigProperties {

    /**
     * 默认匹配器
     */
    private String defaultMatcher = SpareCacheConstants.DEFAULT_MATCHER_CACHE_NAME;

    /**
     * 默认存储器
     */
    private String defaultStore = SpareCacheConstants.DEFAULT_STORE_CACHE_NAME;

    /**
     * 匹配器配置
     */
    private List<MatcherProps> matchers;

    /**
     * 存储器配置
     */
    private List<StoreProps> stores;

    /**
     * 采样配置
     */
    @Data
    public static class MatcherProps {
        /**
         * 采样器类型
         */
        private String type;

        /**
         * 采样器属性配置
         */
        private Properties props;
    }

    @Data
    public static class StoreProps {
        /**
         * 存储器类型
         */
        private String type;

        private StoreConfig storeConfig;
    }

    @Data
    public static class StoreConfig {
        /**
         * 缓存的最大容量
         */
        private Integer maxCapacity;

        /**
         * 缓存过期时间 单位 ms
         */
        private Long expireAfterWrite;

        /**
         * 缓存引用类型
         */
        private String referenceType;

        /**
         * 是否使用LRU算法
         */
        private Boolean lru;

        /**
         * 存储器额外的属性配置
         */
        private Properties props;
    }
}
