package com.myhexin.b2c.web.spare.cache.autoconfigure;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author baoyh
 * @since 2024/10/9
 */
public class SphereCacheConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{SpareCacheConfiguration.class.getName()};
    }
}
