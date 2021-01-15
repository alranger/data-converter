package com.dyl.data.convert;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class AnnotationTest {
    public static void main(String[] args) throws ReflectiveOperationException {
        MyAnnotation myAnnotation = AnnotationP.class.getAnnotation(MyAnnotation.class);
        System.out.println(myAnnotation);
        // 输出：
        // @com.doctor.spring.core.AnnotationPractice$MyAnnotation(value=AnnotationP,
        // num=12, address=[1, 2])

        for (Method method : myAnnotation.annotationType().getDeclaredMethods()) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Object invoke = method.invoke(myAnnotation);
            System.out.println("invoke methd " + method.getName() + " result:" + invoke);
            if (invoke.getClass().isArray()) {
                Object[] temp = (Object[]) invoke;
                for (Object o : temp) {
                    System.out.println(o);
                }
            }
        }
        // 输出：
        // invoke methd num result:12
        // invoke methd value result:AnnotationP
        // invoke methd address result:[Ljava.lang.String;@74a14482
        // 1
        // 2

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Documented
    private @interface MyAnnotation {
        String value() default "";

        int num() default 100;

        String[] address() default {};
    }

    @MyAnnotation(value = "AnnotationP", num = 12, address = { "1", "2" })
    private static class AnnotationP {

    }
}
