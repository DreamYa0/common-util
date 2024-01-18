package com.g7.framwork.common.util.concurrent;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

/**
 * @author dreamyao
 * @title
 * @date 2021/8/3 11:22 上午
 * @since 1.0.0
 */
public final class UnsafeUtils {

    private static final Logger logger = LoggerFactory.getLogger(UnsafeUtils.class);

    private UnsafeUtils() {

    }

    public static Unsafe getUnsafe() {

        try {

            return (Unsafe) FieldUtils.readDeclaredStaticField(Unsafe.class, "theUnsafe", true);

        } catch (IllegalAccessException e) {
            logger.error("Get Unsafe object failed.", e);
            throw new RuntimeException("Get Unsafe object failed.", e);
        }
    }
}
