package com.kentchiu.spring.base.domain;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OptionValidator.class)
@Documented
public @interface Option {

    String message() default "must one of {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

//    @Target({ ElementType.FIELD,ElementType. METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
//    @Retention(RetentionPolicy.RUNTIME)
//    @Documented
//    @interface List {
//        Option[] value();
//    }
}