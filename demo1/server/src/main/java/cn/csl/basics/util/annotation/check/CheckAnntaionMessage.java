package cn.csl.basics.util.annotation.check;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAnntaionMessage {
    String message();//提示信息
}
