package com.hyf.frame;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author baB_hyf
 * @date 2022/02/09
 */
@T3.B(ttt = "测试")
public class T3 {

    public static void main(String[] args) {
        A mergedAnnotationA = AnnotatedElementUtils.getMergedAnnotation(T3.class, A.class);
        B mergedAnnotationB = AnnotatedElementUtils.getMergedAnnotation(T3.class, B.class);
        System.out.println();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface A {
        String ttt() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    // @A
    @interface B {
        @AliasFor(annotation = A.class, attribute = "ttt")
        String ttt() default "";
    }
}
