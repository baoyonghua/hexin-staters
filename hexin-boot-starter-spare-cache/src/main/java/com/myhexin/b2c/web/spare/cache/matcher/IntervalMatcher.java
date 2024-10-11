package com.myhexin.b2c.web.spare.cache.matcher;

import com.myhexin.b2c.web.common.spi.Spi;

import java.util.Properties;

/**
 * 基于时间区间的采样器实现
 *
 * @author baoyh
 * @since 2024/10/9
 */
@Spi("interval")
public class IntervalMatcher implements Matcher{
    @Override
    public boolean match(String key) {
        return false;
    }

    @Override
    public void init(Properties props) {

    }
}
