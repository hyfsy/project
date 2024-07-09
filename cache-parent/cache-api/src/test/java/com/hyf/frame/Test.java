package com.hyf.frame;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

/**
 * @author baB_hyf
 * @date 2022/02/05
 */
// @Test.A("111")
@Test.A("222")
// @Test.B("bbb")
public class Test {

    public static void main(String[] args) {
        Set<A> mergedRepeatableAnnotations = AnnotatedElementUtils.findMergedRepeatableAnnotations(Test.class, A.class);
        // @Inherit
        AnnotatedElementUtils.getMergedAnnotation(Test.class, A.class);
        // class all hierarchy
        AnnotatedElementUtils.findMergedAnnotation(Test.class, A.class);
        A annotation = AnnotatedElementUtils.findMergedAnnotation(Test.class, A.class);
        System.out.println(annotation.value());
    }

    @Repeatable(AS.class)
    @Retention(RetentionPolicy.RUNTIME)
    @interface A {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface AS {
        A[] value() default {};
    }

    @Retention(RetentionPolicy.RUNTIME)
    @A("aaa")
    @interface B {

        @AliasFor(attribute = "value", annotation = A.class)
        String value();
    }
}
