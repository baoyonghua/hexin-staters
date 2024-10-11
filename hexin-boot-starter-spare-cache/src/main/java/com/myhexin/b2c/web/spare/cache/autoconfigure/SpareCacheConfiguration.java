package com.myhexin.b2c.web.spare.cache.autoconfigure;

import com.myhexin.b2c.web.spare.cache.annotations.EnableSpareCache;
import com.myhexin.b2c.web.spare.cache.autoconfigure.properties.DefaultCacheManager;
import com.myhexin.b2c.web.spare.cache.autoconfigure.properties.SpareCacheConfigProperties;
import com.myhexin.b2c.web.spare.cache.manager.SpareCacheManager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

/**
 * @author baoyh
 * @since 2024/9/8
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SpareCacheConfigProperties.class)
public class SpareCacheConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes enableSpareCache;

    @Resource
    private SpareCacheConfigProperties properties;

    /**
     * 初始化默认的SpareCache管理器
     *
     * @return
     */
    @Bean
    public SpareCacheManager spareCacheManager() {
        DefaultCacheManager defaultCacheManager = new DefaultCacheManager();
        defaultCacheManager.init(properties);
        return defaultCacheManager;
    }

    /**
     * 注册 BeanPostProcessor 以支持生产代理对象，从而实现缓存兜底
     *
     * @return
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public SpareCacheAnnotationBeanPostProcessor spareCachePostProcessor() {
        Assert.notNull(this.enableSpareCache, "@EnableSpareCache annotation metadata was not injected");
        SpareCacheAnnotationBeanPostProcessor bpp = new SpareCacheAnnotationBeanPostProcessor();

        // 如果存在用户自定义的异步注解，则使用自定义注解来替换默认的@SpareCache注解
        Class<? extends Annotation> customAsyncAnnotation = this.enableSpareCache.getClass("annotation");
        if (customAsyncAnnotation != AnnotationUtils.getDefaultValue(EnableAsync.class, "annotation")) {
            bpp.setSpareCacheAnnotationType(customAsyncAnnotation);
        }
        // 确定是使用Jdk动态代理创建代理对象，还是使用CGLIB来创建代理对象
        bpp.setProxyTargetClass(this.enableSpareCache.getBoolean("proxyTargetClass"));
        // 设置 BeanPostProcessor 的优先级，这通常是最低优先级，以在所有其他后处理器之后运行，
        // 以便它可以向现有代理添加 advisor 而不是双重代理
        bpp.setOrder(this.enableSpareCache.<Integer>getNumber("order"));
        return bpp;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableSpareCache = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableSpareCache.class.getName(), false));
        if (this.enableSpareCache == null) {
            throw new IllegalArgumentException(
                    "@EnableSpareCache is not present on importing class " + importMetadata.getClassName());
        }
    }
}
