package cn.csl.basics.util.annotation.unique;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueAnnotation {
    String uniqueMsg();
}
