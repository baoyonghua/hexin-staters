package com.myhexin.b2c.web.spare.cache.autoconfigure;

import com.myhexin.b2c.web.spare.cache.interceptor.SpareCacheAnnotationAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * @author baoyh
 * @since 2024/10/9
 */
@Slf4j
public class SpareCacheAnnotationBeanPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor {

    @Nullable
    private Class<? extends Annotation> spareCacheAnnotationType;

    public SpareCacheAnnotationBeanPostProcessor() {
        // 如果Bean已经被代理，则始终将Advisor添加到最后一个，即：最后执行缓存兜底的逻辑
        setBeforeExistingAdvisors(false);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        // 【核心】创建异步功能增强器Advisor，用于为原始功能进行增强，添加缓存兜底特性
        SpareCacheAnnotationAdvisor advisor = new SpareCacheAnnotationAdvisor();
        if (this.spareCacheAnnotationType != null) {
            // 为增强器Advisor设置注解，默认为@SpareCache
            advisor.setSpareCacheAnnotationType(this.spareCacheAnnotationType);
        }
        advisor.setBeanFactory(beanFactory); // Advisor并没有注入到容器中，所以这里手动设置下BeanFactory
        this.advisor = advisor;
    }

    public void setSpareCacheAnnotationType(Class<? extends Annotation> spareCacheAnnotationType) {
        Assert.notNull(spareCacheAnnotationType, "'spareCacheAnnotationType' must not be null");
        this.spareCacheAnnotationType = spareCacheAnnotationType;
    }
}
