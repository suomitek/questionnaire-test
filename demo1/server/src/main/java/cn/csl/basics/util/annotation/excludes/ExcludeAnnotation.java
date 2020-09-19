package cn.csl.basics.util.annotation.excludes;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeAnnotation {
    boolean flag() default false;
}
