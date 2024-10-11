package com.myhexin.b2c.web.spare.cache.interceptor;

import com.myhexin.b2c.web.spare.cache.annotations.SpareCache;
import com.myhexin.b2c.web.spare.cache.support.SpareCacheAspectSupport;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于注解的SpareCache通知Advice
 *
 * @author baoyh
 * @since 2024/9/8
 */
@Slf4j
public class AnnotationSpareCacheInterceptor extends SpareCacheAspectSupport implements MethodInterceptor, Ordered {

    private static final Map<Method, SpareCache> cache = new ConcurrentHashMap<>(32);

    /**
     * 拦截业务方法以支持缓存兜底，请确保在查询接口上使用这个缓存兜底，而不是在任何写接口上
     * <p>
     *     todo 这里有个问题，当使用 @Caching 注解时，此时 Cache 中的缓存数据失效了，
     *     并且业务方法报错了，此时兜底成功的话会返回上一次采样到的数据，那么 @Caching 注解会缓存兜底数据，从而影响数据一致性。
     *     我们在这并不会解决这个问题，因为还没找到合适的切入到去干预 @Caching注解 的执行逻辑
     * </p>
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 目标类的Class对象
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        // 目标方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        // 桥接方法
        final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        Object result;
        try {
            result = invocation.proceed();
        } catch (Exception e) {
            // 出现匹配异常时则进行兜底
            return handlerError(e, userDeclaredMethod, invocation.getArguments());
        }
        // 采样处理
        sample(userDeclaredMethod, result);
        return result;
    }

    /**
     * Advice实现Order接口的原因是: 在{@link AbstractPointcutAdvisor}中如果Advisor未设置order则尝试获取Advisor中Advice的order
     *
     * @return
     */
    @Override
    public int getOrder() {
        // 最低优先级，当前Advice总是应该最后执行的
        // 主要是为了确保与@Caching注解结合使用的情况，让@Caching注解先执行
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    protected String getMethodKey(Method method) {
        return null;
    }

    @Nullable
    @Override
    protected String getSampleName(Method method) {
        SpareCache spareCache = getSpareCache(method);
        return spareCache != null ? spareCache.samplerName() : null;
    }

    @Override
    protected String getKvStoreName(Method method) {
        SpareCache spareCache = getSpareCache(method);
        return spareCache != null ? spareCache.name() : null;
    }

    @Override
    protected boolean sampleNullResult(Method method) {
        SpareCache spareCache = getSpareCache(method);
        return spareCache != null && spareCache.handleWithNullResult();
    }

    @Override
    protected boolean matchException(Method method, Throwable e) {
        SpareCache spareCache = getSpareCache(method);
        if (!spareCache.handleWithException()) {
            return false;
        }
        return exceptionBelongsTo(e, spareCache.exceptionsToTrace())
                && !exceptionBelongsTo(e, spareCache.exceptionsToIgnore());
    }

    private SpareCache getSpareCache(Method method) {
        SpareCache spareCache = cache.get(method);
        if (spareCache == null) {
            spareCache = cache.computeIfAbsent(method, k -> {
                SpareCache sc = AnnotatedElementUtils.findMergedAnnotation(method, SpareCache.class);
                if (sc == null) {
                    sc = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), SpareCache.class);
                }
                return sc;
            });
        }
        return spareCache;
    }

    private boolean exceptionBelongsTo(Throwable ex, Class<? extends Throwable>[] exceptions) {
        if (exceptions == null) {
            return false;
        }
        for (Class<? extends Throwable> exceptionClass : exceptions) {
            if (exceptionClass.isAssignableFrom(ex.getClass())) {
                return true;
            }
        }
        return false;
    }
}
