package com.myhexin.b2c.web.spare.cache.support;

import com.myhexin.b2c.web.spare.cache.manager.SpareCacheManager;
import com.myhexin.b2c.web.spare.cache.matcher.Matcher;
import com.myhexin.b2c.web.spare.cache.store.KvStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * @author baoyh
 * @since 2024/9/8
 */
@Slf4j
public abstract class SpareCacheAspectSupport implements BeanFactoryAware {

    /**
     * 缓存兜底管理器
     */
    private SpareCacheManager spareCacheManager;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        spareCacheManager = beanFactory.getBean(SpareCacheManager.class);
    }

    /**
     * 获取采样器
     *
     * @param method
     * @return
     */
    protected Matcher determineSample(Method method) {
        String sampleName = getSampleName(method);
        return spareCacheManager.getMatcher(sampleName);
    }

    protected KvStore determineKvStore(Method method) {
        String storeName = getKvStoreName(method);
        return spareCacheManager.getStore(storeName);
    }

    /**
     * 异常兜底
     *
     * @param e
     * @param method
     * @param arguments
     * @return
     * @throws Throwable 缓存兜底失败/无需缓存兜底则直接抛出原始异常
     */
    protected Object handlerError(Throwable e, Method method, Object[] arguments) throws Throwable {
        if (matchException(method, e)) {
            log.error("[spare cache] An exception was detected during the execution of the method [{}#{}]," +
                            " and a cache spare was attempted.",
                    method.getClass().getSimpleName(), method.getName());
            try {
                // custom method
                KvStore store = determineKvStore(method);
                String methodKey = getMethodKey(method);
                return store.get(methodKey);
            } catch (Exception ex) {
                log.error("[spare cache]An exception occurred during handler error. " +
                        "current method: [{}#{}]", method.getClass().getSimpleName(), method.getName(), ex);
            }
        }
        throw e;
    }

    /**
     * 采样，符合采样算法的结果会进行缓存
     *
     * @param method
     * @param result
     */
    protected void sample(Method method, Object result) {
        if (result == null && !sampleNullResult(method)) {
            return;
        }
        try {
            Matcher sample = determineSample(method);
            String methodKey = getMethodKey(method);
            if (sample.match(methodKey)) {
                KvStore store = determineKvStore(method);
                store.put(methodKey, store);
                log.info("[spare cache]Sampling success, current sampling algorithm: {}, current method: [{}#{}]",
                        sample, method.getClass().getSimpleName(), method.getName());
            }
        } catch (Exception e) {
            log.error("[spare cache]An exception occurred during sampling and storage, and sampling failed. " +
                    "current method: [{}#{}]", method.getClass().getSimpleName(), method.getName(), e);
        }
    }

    protected abstract String getMethodKey(Method method);

    @Nullable
    protected abstract String getSampleName(Method method);

    @Nullable
    protected abstract String getKvStoreName(Method method);

    /**
     * 如果方法的返回结果为null，是否需要进行采样
     *
     * @param method
     * @return
     */
    protected abstract boolean sampleNullResult(Method method);

    /**
     * 判断是否能够匹配此异常
     *
     * @param method
     * @param e
     * @return
     */
    protected abstract boolean matchException(Method method, Throwable e);
}
