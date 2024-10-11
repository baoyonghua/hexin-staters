package com.myhexin.b2c.web.spare.cache.matcher;

import com.myhexin.b2c.web.common.spi.Spi;

import java.util.Properties;

/**
 * 基于滑动窗口实现的匹配器
 *
 * @author baoyh
 * @since 2024/10/9
 */
@Spi(value = "slidingWindow")
public class SlidingWindowMatcher implements Matcher {
    @Override
    public boolean match(String key) {
        return false;
    }

    @Override
    public void init(Properties props) {

    }
}
