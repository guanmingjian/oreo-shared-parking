package cn.oreo.common.core.annotation;

import cn.oreo.common.core.validator.MobileValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class)
public @interface IsMobile {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
