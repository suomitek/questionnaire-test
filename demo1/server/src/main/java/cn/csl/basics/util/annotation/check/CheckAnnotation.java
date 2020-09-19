package cn.csl.basics.util.annotation.check;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAnnotation {
	String type() default "required";//验证类型
    String message() default "";//提示信息
	String attribute() default "";
}
