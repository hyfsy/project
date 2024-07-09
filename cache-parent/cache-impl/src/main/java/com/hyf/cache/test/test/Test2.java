package com.hyf.cache.test.test;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author baB_hyf
 * @date 2022/01/19
 */
@Test2.C("ddd")
public class Test2 {

    public static void main(String[] args) {
        Annotation[] annotations = Test2.class.getAnnotations();
        for (Annotation annotation : annotations) {
            A mergedAnnotationa = AnnotatedElementUtils.getMergedAnnotation(Test2.class, A.class);
            B mergedAnnotationb = AnnotatedElementUtils.getMergedAnnotation(Test2.class, B.class);
            C mergedAnnotationc = AnnotatedElementUtils.getMergedAnnotation(Test2.class, C.class);

            System.out.println();
        }
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    public @interface A {
        String ddd() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface B {
        String ddd() default "";
    }

    @A(ddd = "xxx")
    @B
    @Retention(RetentionPolicy.RUNTIME)
    public @interface C {

        // @AliasFor(annotation = A.class)
        @AliasFor(annotation = B.class, attribute = "ddd")
        String value() default "";
    }
}
