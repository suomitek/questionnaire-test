package cn.csl.basics.util.annotation.status;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface StatusAnnotation {
    boolean flag() default false;
}
