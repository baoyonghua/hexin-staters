package com.myhexin.b2c.web.spare.cache.interceptor;

import com.myhexin.b2c.web.spare.cache.annotations.SpareCache;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 基于注解的SpareCacheAdvisor增强器
 * <p>
 * 注意：这里的Advisor不能注入到Bean中，以防止在两个BeanPostProcessor中都会将此Advisor织入到Bean
 *     <ol>
 *         <li>{@link org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator}</li>
 *         <li>{@link com.myhexin.b2c.web.spare.cache.autoconfigure.SpareCacheAnnotationBeanPostProcessor}</li>
 *     </ol>
 * </p>
 *
 * @author baoyh
 * @since 2024/9/8
 */
public class SpareCacheAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {
    /**
     * 通知
     */
    private final Advice advice;


    /**
     * 切入点（基于注解的Pointcut）
     */
    private Pointcut pointcut;

    /**
     * 注解类型
     */
    private Class<? extends Annotation> spareCacheAnnotationType;


    public SpareCacheAnnotationAdvisor() {
        this.pointcut = buildPointcut(SpareCache.class);
        this.advice = buildAdvice();
    }

    private Advice buildAdvice() {
        return new AnnotationSpareCacheInterceptor();
    }


    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        // 当前的advice并不会注入到容器中，所以在这里手动设置下BeanFactory
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    public void setSpareCacheAnnotationType(Class<? extends Annotation> spareCacheAnnotationType) {
        Assert.notNull(spareCacheAnnotationType, "'asyncAnnotationType' must not be null");
        this.pointcut = buildPointcut(spareCacheAnnotationType);
    }

    private Pointcut buildPointcut(Class<? extends Annotation> spareCacheAnnotationType) {
        this.spareCacheAnnotationType = spareCacheAnnotationType;
        return new SpareCacheAnnotationPointcut();
    }

    public class SpareCacheAnnotationPointcut extends StaticMethodMatcherPointcut {
        /**
         * 基于注解的方法匹配器
         */
        public final AnnotationMethodMatcher matcher = new AnnotationMethodMatcher(spareCacheAnnotationType, true);

        @Override
        public boolean matches(Method method, Class<?> clz) {
            return matcher.matches(method, clz);
        }
    }
}
