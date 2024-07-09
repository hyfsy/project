package com.hyf.cache.test.entity;

import java.lang.annotation.*;

/**
 * @author baB_hyf
 * @date 2022/02/10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity
{

    String table();

    String[] id();
}
