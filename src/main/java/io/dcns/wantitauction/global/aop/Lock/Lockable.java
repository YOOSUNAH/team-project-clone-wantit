package io.dcns.wantitauction.global.aop.Lock;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Lockable {
    String value() default "lock";

    long waitTime() default 5000;

    long leaseTime() default 500;
}
