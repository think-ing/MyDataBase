package com.mzw.mydatabase.annotaions;

import android.icu.util.RangeValueIterator;
import android.sax.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 * @Target 元注解关键字，说明此自定义注解应用在哪里 一下是常用的：
 * ElementType.TYPE     类上
 * ElementType.FIELD    属性上
 * ElementType.METHOD  方法上
 * ElementType.ANNOTATION_TYPE 注解上
 *
 *@Retention 元注解关键字，说明此自定义注解 生效时间
 * RetentionPolicy.SOURCE
 * RetentionPolicy.CLASS    编译时
 * RetentionPolicy.RUNTIME  运行时
 *
 * 注解消耗性能，提高效率
 * Created by think on 2018/6/6.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBField {
    String value();
}
