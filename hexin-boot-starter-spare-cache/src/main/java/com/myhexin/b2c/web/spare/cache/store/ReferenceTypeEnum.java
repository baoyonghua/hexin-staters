package com.myhexin.b2c.web.spare.cache.store;

import cn.hutool.core.util.StrUtil;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * @author baoyh
 * @since 2024/10/10
 */
public enum ReferenceTypeEnum {

    /**
     * 强引用，gc不回收
     */
    STRONG {
        @Override
        public Object wrapper(Object o) {
            return o;
        }

        @Override
        public Object unWrapper(Object o) {
            return o;
        }
    },
    /**
     * 软引用，gc内存不足时回收
     */
    SOFT {
        @Override
        public Object wrapper(Object o) {
            return new SoftReference<>(o);
        }

        @Override
        public Object unWrapper(Object o) {
            if (o instanceof SoftReference) {
                return ((SoftReference) o).get();
            }
            return null;
        }
    },
    /**
     * 弱引用，gc时回收(不推荐使用)
     */
    WEAK {
        @Override
        public Object wrapper(Object o) {
            return new WeakReference<>(o);
        }

        @Override
        public Object unWrapper(Object o) {
            if (o instanceof WeakReference) {
                return ((WeakReference) o).get();
            }
            return null;
        }
    };

    /**
     * 对象包装
     */
    public abstract Object wrapper(Object o);

    /**
     * 对象拆包
     */
    public abstract Object unWrapper(Object o);

    /**
     * 获取enum对象
     *
     * @param name
     * @return
     */
    public static ReferenceTypeEnum getReferenceType(String name) {
        for (ReferenceTypeEnum typeEnum : ReferenceTypeEnum.values()) {
            if (StrUtil.equalsIgnoreCase(typeEnum.name(), name)) {
                return typeEnum;
            }
        }
        return null;
    }
}
