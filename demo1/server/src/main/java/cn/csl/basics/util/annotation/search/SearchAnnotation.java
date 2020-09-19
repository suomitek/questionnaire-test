package cn.csl.basics.util.annotation.search;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SearchAnnotation {
    String searchMsg();
}
