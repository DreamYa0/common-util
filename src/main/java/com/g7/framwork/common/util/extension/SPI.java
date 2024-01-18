package com.g7.framwork.common.util.extension;

import java.lang.annotation.*;

/**
 * @author dreamyao
 * @description
 * @date 2017/12/13 下午7:46
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * 指定默认扩展点名称
     */
    String value() default "";
}
